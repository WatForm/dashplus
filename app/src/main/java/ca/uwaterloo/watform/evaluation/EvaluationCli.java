package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.parser.AlloyParser.alloyParseToModel;

import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.utils.CollectionTelemetry;
import ca.uwaterloo.watform.utils.ImplementationError;
import ca.uwaterloo.watform.utils.Reporter;
import ca.uwaterloo.watform.utils.UserOrImplError;
import ca.uwaterloo.watform.utils.UtilsUserError;
import ca.uwaterloo.watform.utils.XmlDumper;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.translator.A4Options;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "java -ea -jar evaluation.jar",
    description = "Dump Alloy instances or evaluate Alloy constraints against an instance.",
    mixinStandardHelpOptions = true,
    version = "evaluation 1.0",
    footer = {
      "",
      "@|bold USAGE MODES|@",
      "",
      "  @|bold java -ea -jar evaluation.jar f.als -dumpInstance < -dumpDir=dir > < -d >|@",
      "     (dump a satisfiable instance for the model and every satisfiable command)",
      "",
      "  @|bold java -ea -jar evaluation.jar f.als -evalConstraints -xml=instance.xml < -cmd=N > <"
          + " -d >|@",
      "     (evaluate the constraints of f.als against the given XML instance;",
      "      -cmd optionally includes the selected zero-based model command)",
      ""
    })
public final class EvaluationCli implements Callable<Integer> {

  @Parameters(index = "0", paramLabel = "MODEL", description = "Alloy model (.als)")
  private Path modelPath;

  @Option(names = "-dumpInstance", description = "Dump satisfiable Alloy instances")
  private boolean dumpInstance;

  @Option(
      names = "-evalConstraints",
      description = "Evaluate model constraints against an instance")
  private boolean evalConstraints;

  @Option(
      names = "-dumpDir",
      defaultValue = "out/",
      paramLabel = "<dir>",
      description = "Instance output directory (default: ${DEFAULT-VALUE})")
  private Path dumpDir;

  @Option(
      names = "-xml",
      paramLabel = "<instance.xml>",
      description = "Instance XML used by -evalConstraints")
  private Path instancePath;

  @Option(
      names = {"-cmd", "--command"},
      paramLabel = "<index>",
      description = "Zero-based command index to dump or include in -evalConstraints")
  private Integer commandIndex;

  @Option(
      names = {"-d", "--debug"},
      description = "Enable debug output")
  private boolean debug;

  @Option(
      names = "--list-commands",
      description = "Discover commands without solving (use --telemetry)")
  private boolean listCommands;

  @Option(names = "--default-only", description = "Dump only the synthetic default run")
  private boolean defaultOnly;

  @Option(names = "--telemetry", description = "Write compact JSONL collection telemetry")
  private Path telemetryPath;

  @Override
  public Integer call() {
    try {
      if (telemetryPath != null) CollectionTelemetry.open(telemetryPath);
      A4Options options = new A4Options();
      CollectionTelemetry.emit(
          "settings", Map.of("solver", options.solver.id(), "no_overflow", options.noOverflow));
      return execute();
    } catch (java.io.IOException error) {
      System.err.println("Telemetry I/O error: " + error.getMessage());
      return 4;
    } finally {
      try {
        CollectionTelemetry.close();
      } catch (java.io.IOException error) {
        throw new java.io.UncheckedIOException(error);
      }
    }
  }

  private Integer execute() {
    Path model = modelPath.toAbsolutePath().normalize();
    Reporter.INSTANCE.reset();
    // Reporter.INSTANCE.popPath(); TODO: fix once methods are added back
    // Reporter.INSTANCE.pushPath(model);
    try {
      validateArguments(model);

      AlloyModel alloyModel = alloyParseToModel(model.toString());
      if (listCommands) {
        var commands = new ArrayList<Map<String, Object>>();
        Map<String, Object> defaultCommand = new LinkedHashMap<>();
        defaultCommand.put("index", null);
        defaultCommand.put("text", "run {}");
        defaultCommand.put("kind", "run");
        commands.add(defaultCommand);
        int index = 0;
        for (var paragraph : alloyModel.allCmdParas()) {
          for (var declaration : paragraph.cmdDecls) {
            commands.add(
                Map.of(
                    "index",
                    index++,
                    "text",
                    declaration.toString(),
                    "kind",
                    declaration.cmdType.toString().toLowerCase(java.util.Locale.ROOT)));
          }
        }
        CollectionTelemetry.emit("commands", Map.of("commands", commands));
        return 0;
      }
      if (commandIndex != null && commandIndex >= alloyModel.getNumCmds()) {
        Reporter.INSTANCE.addError(
            EvaluationCliError.commandIndexOutOfRange(commandIndex, alloyModel.getNumCmds()));
        Reporter.INSTANCE.exitIfHasErrors();
      }
      try {
        alloyModel.resolve();
      } catch (UserOrImplError error) {
        Reporter.INSTANCE.addError(error);
        Reporter.INSTANCE.exitIfHasErrors();
      }

      if (dumpInstance) {
        var modelName = model.getFileName().toString();
        var prefix = modelName.substring(0, modelName.length() - ".als".length());
        if (defaultOnly || commandIndex != null) {
          XmlDumper.dumpInstance(
              alloyModel,
              prefix,
              dumpDir.toAbsolutePath().normalize(),
              commandIndex == null ? OptionalInt.empty() : OptionalInt.of(commandIndex));
        } else dumpInstance(alloyModel, prefix, dumpDir);
      } else if (evalConstraints) {
        long evaluationStart = System.nanoTime();
        try {
          OptionalInt selectedCommand =
              commandIndex == null ? OptionalInt.empty() : OptionalInt.of(commandIndex);
          runEvalConstraints(
              alloyModel, instancePath.toAbsolutePath().normalize(), selectedCommand, debug);
        } catch (AlloyEvaluatorError error) {
          Reporter.INSTANCE.addError(error);
          Reporter.INSTANCE.exitIfHasErrors();
        } finally {
          CollectionTelemetry.emit(
              "timing",
              Map.of(
                  "phase", "evaluation", "seconds", (System.nanoTime() - evaluationStart) / 1e9));
        }
      } else {
        throw AlloyEvaluatorImplError.validatedCliWithoutMode();
      }

      Reporter.INSTANCE.print();
      return 0;

    } catch (Reporter.AbortSignal abortSignal) {
      String reason =
          Reporter.INSTANCE.getErrors().stream()
              .map(Throwable::getMessage)
              .collect(java.util.stream.Collectors.joining("; "));
      String status =
          Reporter.INSTANCE.getErrors().stream()
                  .anyMatch(error -> CollectionTelemetry.classify(error).equals("unsupported"))
              ? "unsupported"
              : "error";
      CollectionTelemetry.failure(status, reason);
      // already printed Reporter if it issued an AbortSignal
      return 1;
    } catch (ImplementationError implError) {
      CollectionTelemetry.failure(CollectionTelemetry.classify(implError), implError.getMessage());
      // Implementation Error exit code: 2
      if (debug) implError.printStackTrace();
      else System.err.println(implError);
      return 2;
    } catch (UserOrImplError implError) {
      CollectionTelemetry.failure(CollectionTelemetry.classify(implError), implError.getMessage());
      // bubbled up here so these are ImplementationError
      // see ErrorHandling.md
      if (debug) implError.printStackTrace();
      else System.err.println(implError);
      return 2;
    } catch (Err e) {
      CollectionTelemetry.failure("error", e.getMessage());
      // error that comes from a call the Alloy Analyzer code base
      // probably a user error but might not be
      System.err.println("Error message from Alloy Analyzer (regarding an Alloy model)");
      System.out.println(e.getMessage());
      System.out.println("Line: " + e.pos.y);
      System.out.println("Column: " + e.pos.x);
      return 3;
    } catch (Exception e) {
      CollectionTelemetry.failure(CollectionTelemetry.classify(e), e.getMessage());
      // Unexpected Error exit code: 3
      System.err.println("Unexpected error: ");
      if (debug) e.printStackTrace();
      else System.err.println(e);
      return 4;
    }
  }

  private void validateArguments(Path model) {
    if (!Files.isRegularFile(model)) {
      Reporter.INSTANCE.addError(EvaluationCliError.fileNotFound("Model file", model.toString()));
    }
    if (!model.toString().endsWith(".als")) {
      Reporter.INSTANCE.addError(EvaluationCliError.invalidModel(model.toString()));
    }
    if (!dumpInstance && !evalConstraints && !listCommands) {
      Reporter.INSTANCE.addError(EvaluationCliError.missingMode());
    }
    if ((dumpInstance ? 1 : 0) + (evalConstraints ? 1 : 0) + (listCommands ? 1 : 0) > 1) {
      Reporter.INSTANCE.addError(EvaluationCliError.conflictingModes());
    }
    if (evalConstraints && instancePath == null) {
      Reporter.INSTANCE.addError(EvaluationCliError.missingInstance());
    }
    if (evalConstraints
        && instancePath != null
        && !Files.isRegularFile(instancePath.toAbsolutePath().normalize())) {
      Reporter.INSTANCE.addError(
          EvaluationCliError.fileNotFound(
              "Instance file", instancePath.toAbsolutePath().normalize().toString()));
    }
    if (dumpInstance && instancePath != null) {
      Reporter.INSTANCE.addError(EvaluationCliError.instanceNotAllowed());
    }
    if ((!evalConstraints && !dumpInstance) && commandIndex != null) {
      Reporter.INSTANCE.addError(EvaluationCliError.commandNotAllowed());
    }
    if (commandIndex != null && commandIndex < 0) {
      Reporter.INSTANCE.addError(EvaluationCliError.negativeCommandIndex(commandIndex));
    }
    if (defaultOnly && (!dumpInstance || commandIndex != null)) {
      Reporter.INSTANCE.addError(
          new ca.uwaterloo.watform.utils.UserError(
              "--default-only requires -dumpInstance and cannot be combined with -cmd"));
    }
    if (listCommands && telemetryPath == null) {
      Reporter.INSTANCE.addError(
          new ca.uwaterloo.watform.utils.UserError("--list-commands requires --telemetry=<path>"));
    }
    Reporter.INSTANCE.exitIfHasErrors();
  }

  private static void dumpInstance(AlloyModel model, String prefix, Path dumpDir) {
    try {
      XmlDumper.dumpInstances(model, prefix, dumpDir.toAbsolutePath().normalize());
    } catch (UtilsUserError error) {
      Reporter.INSTANCE.addError(error);
      Reporter.INSTANCE.exitIfHasErrors();
    }
  }

  private static void runEvalConstraints(
      AlloyModel alloyModel, Path instancePath, OptionalInt commandIndex, boolean debug) {
    // dpOutput("Checking instance for " + instancePath);
    CollectionTelemetry.phase("initialization");
    Instance instance;
    try {
      instance = new Instance(Files.readString(instancePath));
    } catch (java.io.IOException error) {
      Reporter.INSTANCE.addError(
          UtilsUserError.fileNotFound(instancePath.toString(), error.getMessage()));
      Reporter.INSTANCE.exitIfHasErrors();
      return;
    }

    if (commandIndex.isPresent() && commandIndex.getAsInt() >= alloyModel.getNumCmds()) {
      Reporter.INSTANCE.addError(
          EvaluationCliError.commandIndexOutOfRange(
              commandIndex.getAsInt(), alloyModel.getNumCmds()));
      Reporter.INSTANCE.exitIfHasErrors();
      return;
    }

    ThreeVal result = evaluateConstraints(alloyModel, instance, commandIndex, debug);
    System.out.println("Satisfied: " + result);
    CollectionTelemetry.emit(
        "result", Map.of("status", result.toString().toLowerCase(java.util.Locale.ROOT)));
  }

  static ThreeVal evaluateConstraints(
      AlloyModel alloyModel, Instance instance, OptionalInt commandIndex, boolean debug) {
    var evaluator = new FormulaEvaluator(new EvaluationTable(instance, alloyModel), debug);
    CollectionTelemetry.phase("constraints");
    var result = ThreeVal.TRUE;
    for (var constraint : alloyModel.allConstraints()) {
      result = result.and(constraint.accept(evaluator));
      if (result.shortCircuitsAnd()) break;
    }

    if (!result.shortCircuitsAnd() && commandIndex.isPresent()) {
      int index = commandIndex.getAsInt();
      ThreeVal commandResult = alloyModel.getCmdFormula(index).accept(evaluator);
      if (alloyModel.isCheckCmd(index)) commandResult = commandResult.not();
      result = result.and(commandResult);
    }
    return result;
  }

  public static void main(String[] args) {
    System.exit(new CommandLine(new EvaluationCli()).execute(args));
  }
}
