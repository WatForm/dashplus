package ca.uwaterloo.watform.evaluation;

import ca.uwaterloo.watform.utils.UserError;

public final class EvaluationCliError extends UserError {
  private EvaluationCliError(String message) {
    super(message);
  }

  public static EvaluationCliError missingMode() {
    return new EvaluationCliError("Specify -dumpInstance, -evalConstraints, or --list-commands.");
  }

  public static EvaluationCliError conflictingModes() {
    return new EvaluationCliError(
        "Choose only one mode: -dumpInstance, -evalConstraints, or --list-commands.");
  }

  public static EvaluationCliError invalidModel(String modelPath) {
    return new EvaluationCliError("Input model must be an .als file: " + modelPath);
  }

  public static EvaluationCliError fileNotFound(String description, String filePath) {
    return new EvaluationCliError(description + " does not exist or is not a file: " + filePath);
  }

  public static EvaluationCliError missingInstance() {
    return new EvaluationCliError("-evalConstraints requires -xml=<instance.xml>.");
  }

  public static EvaluationCliError instanceNotAllowed() {
    return new EvaluationCliError("-xml can only be used with -evalConstraints.");
  }

  public static EvaluationCliError commandNotAllowed() {
    return new EvaluationCliError("-cmd can only be used with -dumpInstance or -evalConstraints.");
  }

  public static EvaluationCliError negativeCommandIndex(int commandIndex) {
    return new EvaluationCliError("Command index must be non-negative: " + commandIndex);
  }

  public static EvaluationCliError commandIndexOutOfRange(int commandIndex, int commandCount) {
    return new EvaluationCliError(
        "Command index "
            + commandIndex
            + " is out of range; the model contains "
            + commandCount
            + " command(s)");
  }
}
