package ca.uwaterloo.watform.evaluation;

import ca.uwaterloo.watform.utils.UserError;

public final class EvaluationCliError extends UserError {
  private EvaluationCliError(String message) {
    super(message);
  }

  public static EvaluationCliError missingMode() {
    return new EvaluationCliError("Specify either -dumpInstance or -evalFacts.");
  }

  public static EvaluationCliError conflictingModes() {
    return new EvaluationCliError("-dumpInstance and -evalFacts cannot be combined.");
  }

  public static EvaluationCliError invalidModel(String modelPath) {
    return new EvaluationCliError("Input model must be an .als file: " + modelPath);
  }

  public static EvaluationCliError fileNotFound(String description, String filePath) {
    return new EvaluationCliError(description + " does not exist or is not a file: " + filePath);
  }

  public static EvaluationCliError missingInstance() {
    return new EvaluationCliError("-evalFacts requires -xml=<instance.xml>.");
  }

  public static EvaluationCliError instanceNotAllowed() {
    return new EvaluationCliError("-xml can only be used with -evalFacts.");
  }
}
