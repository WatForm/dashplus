package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.parser.AlloyParser.alloyParseToModel;

import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.utils.ImplementationError;
import ca.uwaterloo.watform.utils.Reporter;
import ca.uwaterloo.watform.utils.UserOrImplError;
import ca.uwaterloo.watform.utils.UtilsUserError;
import ca.uwaterloo.watform.utils.XmlDumper;
import edu.mit.csail.sdg.alloy4.Err;
import java.nio.file.Files;
import java.nio.file.Path;
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
      "  @|bold java -ea -jar evaluation.jar f.als -evalConstraints -xml=instance.xml < -d >|@",
      "     (evaluate the constraints of f.als against the given XML instance)",
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
      names = {"-d", "--debug"},
      description = "Enable debug output")
  private boolean debug;

  @Override
  public Integer call() {
    Path model = modelPath.toAbsolutePath().normalize();
    Reporter.INSTANCE.reset();
    // Reporter.INSTANCE.popPath(); TODO: fix once methods are added back
    // Reporter.INSTANCE.pushPath(model);
    try {
      validateArguments(model);

      AlloyModel alloyModel = alloyParseToModel(model.toString());
      try {
        alloyModel.resolve();
      } catch (UserOrImplError error) {
        Reporter.INSTANCE.addError(error);
        Reporter.INSTANCE.exitIfHasErrors();
      }

      if (dumpInstance) {
        var modelName = model.getFileName().toString();
        var prefix = modelName.substring(0, modelName.length() - ".als".length());
        dumpInstance(alloyModel, prefix, dumpDir);
      } else if (evalConstraints) {
        try {
          runEvalConstraints(alloyModel, instancePath.toAbsolutePath().normalize(), debug);
        } catch (AlloyEvaluatorError error) {
          Reporter.INSTANCE.addError(error);
          Reporter.INSTANCE.exitIfHasErrors();
        }
      } else {
        throw AlloyEvaluatorImplError.validatedCliWithoutMode();
      }

      Reporter.INSTANCE.print();
      return 0;

    } catch (Reporter.AbortSignal abortSignal) {
      // already printed Reporter if it issued an AbortSignal
      return 1;
    } catch (ImplementationError implError) {
      // Implementation Error exit code: 2
      if (debug) implError.printStackTrace();
      else System.err.println(implError);
      return 2;
    } catch (UserOrImplError implError) {
      // bubbled up here so these are ImplementationError
      // see ErrorHandling.md
      if (debug) implError.printStackTrace();
      else System.err.println(implError);
      return 2;
    } catch (Err e) {
      // error that comes from a call the Alloy Analyzer code base
      // probably a user error but might not be
      System.err.println("Error message from Alloy Analyzer (regarding an Alloy model)");
      System.out.println(e.getMessage());
      System.out.println("Line: " + e.pos.y);
      System.out.println("Column: " + e.pos.x);
      return 3;
    } catch (Exception e) {
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
    if (!dumpInstance && !evalConstraints) {
      Reporter.INSTANCE.addError(EvaluationCliError.missingMode());
    }
    if (dumpInstance && evalConstraints) {
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

  private static void runEvalConstraints(AlloyModel alloyModel, Path instancePath, boolean debug) {
    // dpOutput("Checking instance for " + instancePath);
    Instance instance;
    try {
      instance = new Instance(Files.readString(instancePath));
    } catch (java.io.IOException error) {
      Reporter.INSTANCE.addError(
          UtilsUserError.fileNotFound(instancePath.toString(), error.getMessage()));
      Reporter.INSTANCE.exitIfHasErrors();
      return;
    }

    // System.out.println(instance.allFieldQnames());
    // System.out.println(instance.allSigQnames());
    var evaluator = new FormulaEvaluator(new EvaluationTable(instance, alloyModel), debug);
    var result = ThreeVal.TRUE;
    for (var constraint : alloyModel.allConstraints()) {
      result = result.and(constraint.accept(evaluator));
      if (result.shortCircuitsAnd()) break;
    }
    System.out.println("Satisfied: " + result);
    // dpOutput("Satisfied: " + result);
  }

  public static void main(String[] args) {
    System.exit(new CommandLine(new EvaluationCli()).execute(args));
  }
}
