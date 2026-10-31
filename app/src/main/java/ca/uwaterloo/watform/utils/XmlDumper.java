package ca.uwaterloo.watform.utils;

import static ca.uwaterloo.watform.utils.CommonStrings.dpOutput;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.ast.Command;
import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.parser.CompUtil;
import edu.mit.csail.sdg.translator.A4Options;
import edu.mit.csail.sdg.translator.A4Solution;
import edu.mit.csail.sdg.translator.TranslateAlloyToKodkod;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

public final class XmlDumper {

  private XmlDumper() {}

  /** Parse original source with a final synthetic default run, retaining the import base. */
  public static CompModule parseForDump(Path modelPath) throws IOException {
    // Alloy canonicalizes paths before looking up source overrides (including macOS /var).
    String filename = modelPath.toRealPath().toString();
    Map<String, String> sources = new HashMap<>();
    // The newline also terminates any trailing line comment in the original source.
    sources.put(filename, Files.readString(Path.of(filename)) + "\nrun {}\n");
    return CompUtil.parseEverything_fromFile(new A4Reporter(), sources, filename);
  }

  /** Exclude the synthetic default command appended by parseForDump. */
  public static int commandCount(CompModule module) {
    return module.getAllCommands().size() - 1;
  }

  /** Dump the default run followed by each original command, keeping their original indices. */
  public static void dumpInstances(CompModule module, String prefix, Path dumpDir) {
    dumpInstance(module, prefix, dumpDir, OptionalInt.empty());
    for (int index = 0; index < commandCount(module); index++) {
      dumpInstance(module, prefix, dumpDir, OptionalInt.of(index));
    }
  }

  /** Dump exactly one execution from a module returned by parseForDump. */
  public static void dumpInstance(
      CompModule module, String prefix, Path dumpDir, OptionalInt index) {
    if (index.isPresent() && (index.getAsInt() < 0 || index.getAsInt() >= commandCount(module))) {
      throw new IllegalArgumentException("Command index out of range: " + index.getAsInt());
    }
    try {
      Files.createDirectories(dumpDir);
    } catch (IOException error) {
      throw UtilsUserError.outputDirectory(dumpDir.toString(), error.getMessage());
    }
    if (index.isEmpty()) {
      executeAndDump(
          module,
          module.getAllCommands().getLast(),
          dumpDir.resolve(prefix + "-instance.xml"),
          "default scope");
    } else {
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
