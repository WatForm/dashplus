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

  private static void executeAndDump(
      CompModule alloyModule, Command command, Path outputPath, String context) {
    dpOutput("Executing " + context + ": " + command);
    A4Solution solution =
        TranslateAlloyToKodkod.execute_command(
            new A4Reporter(), alloyModule.getAllReachableSigs(), command, new A4Options());

    if (!solution.satisfiable()) {
      dpOutput("UNSAT — no instance to dump for: " + context);
      return;
    }

    solution.writeXML(outputPath.toString(), alloyModule.getAllFunc(), Collections.emptyMap());
    dpOutput("SAT — dumped instance to: " + outputPath);
  }
}
