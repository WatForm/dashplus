package ca.uwaterloo.watform.utils;

import static ca.uwaterloo.watform.utils.CommonStrings.dpOutput;

import ca.uwaterloo.watform.alloyinterface.AlloyInterface;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.ast.Command;
import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.translator.A4Options;
import edu.mit.csail.sdg.translator.A4Solution;
import edu.mit.csail.sdg.translator.TranslateAlloyToKodkod;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.OptionalInt;

public final class XmlDumper {

  private XmlDumper() {}

  /**
   * Dumps a native Alloy XML instance for the model's default scope and for every command.
   * Unsatisfiable executions are reported but do not produce files.
   */
  public static void dumpInstances(AlloyModel alloyModel, String fileNamePrefix, Path dumpDir) {
    try {
      Files.createDirectories(dumpDir);
    } catch (IOException error) {
      throw UtilsUserError.outputDirectory(dumpDir.toString(), error.getMessage());
    }

    CompModule defaultModule =
        AlloyInterface.parse(alloyModel.toStringNoCmds() + System.lineSeparator() + "run {}");
    executeAndDump(
        defaultModule,
        defaultModule.getAllCommands().getFirst(),
        dumpDir.resolve(fileNamePrefix + "-instance.xml"),
        "default scope (no commands in file)");

    if (alloyModel.getNumCmds() == 0) return;

    CompModule commandModule = AlloyInterface.parse(alloyModel.toString());
    for (int commandIndex = 0; commandIndex < alloyModel.getNumCmds(); commandIndex++) {
      executeAndDump(
          commandModule,
          commandModule.getAllCommands().get(commandIndex),
          dumpDir.resolve(fileNamePrefix + "-instance-cmd" + commandIndex + ".xml"),
          "command " + commandIndex);
    }
  }

  // Dump exactly one execution
  public static void dumpInstance(
      AlloyModel model, String prefix, Path dumpDir, OptionalInt index) {
    try {
      Files.createDirectories(dumpDir);
    } catch (IOException error) {
      throw UtilsUserError.outputDirectory(dumpDir.toString(), error.getMessage());
    }
    if (index.isEmpty()) {
      CompModule module =
          AlloyInterface.parse(model.toStringNoCmds() + System.lineSeparator() + "run {}");
      executeAndDump(
          module,
          module.getAllCommands().getFirst(),
          dumpDir.resolve(prefix + "-instance.xml"),
          "default scope");
    } else {
      CompModule module = AlloyInterface.parse(model.toString());
      executeAndDump(
          module,
          module.getAllCommands().get(index.getAsInt()),
          dumpDir.resolve(prefix + "-instance-cmd" + index.getAsInt() + ".xml"),
          "command " + index.getAsInt());
    }
  }

  private static void executeAndDump(
      CompModule alloyModule, Command command, Path outputPath, String context) {
    dpOutput("Executing " + context + ": " + command);
    long start = System.nanoTime();
    try {
      A4Solution solution =
          TranslateAlloyToKodkod.execute_command(
              new A4Reporter(), alloyModule.getAllReachableSigs(), command, new A4Options());

      CollectionTelemetry.emit("instance", Map.of("bitwidth", solution.getBitwidth()));
      if (!solution.satisfiable()) {
        CollectionTelemetry.emit("result", Map.of("status", "unsat"));
        dpOutput("UNSAT — no instance to dump for: " + context);
        return;
      }

      solution.writeXML(outputPath.toString(), alloyModule.getAllFunc(), Collections.emptyMap());
      dpOutput("SAT — dumped instance to: " + outputPath);
      CollectionTelemetry.emit("result", Map.of("status", "sat", "xml", outputPath.toString()));
    } finally {
      CollectionTelemetry.emit(
          "timing", Map.of("phase", "generation", "seconds", (System.nanoTime() - start) / 1e9));
    }
  }
}
