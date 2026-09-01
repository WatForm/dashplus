package ca.uwaterloo.watform.evaluation;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.Pos;
import ca.uwaterloo.watform.utils.UserOrImplError;

/** Errors caused by Alloy constructs that the evaluator does not currently support. */
public final class AlloyEvaluatorError extends UserOrImplError {
  private AlloyEvaluatorError(Pos pos, String message) {
    super(pos, message);
  }

  public static AlloyEvaluatorError unsupportedExpression(
      String evaluator, Pos pos, AlloyExpr expression) {
    return new AlloyEvaluatorError(
        pos,
        evaluator
            + " does not support expression "
            + expression.getClass().getSimpleName()
            + ": "
            + expression);
  }

  public static AlloyEvaluatorError unsupportedDisjOnDomain(Pos pos, String declaration) {
    return new AlloyEvaluatorError(
        pos, "Evaluation does not support 'disj' on a declaration domain: " + declaration);
  }

  public static AlloyEvaluatorError unsupportedDeclarationMultiplicity(
      Pos pos, String declaration, AlloyQtEnum multiplicity) {
    return new AlloyEvaluatorError(
        pos,
        "Evaluation only supports declaration multiplicity 'one'; found '"
            + multiplicity
            + "' in "
            + declaration);
  }

  public static AlloyEvaluatorError arithmeticDivisionByZero(Pos pos, String operation) {
    return new AlloyEvaluatorError(
        pos, "Arithmetic function " + operation + " does not yet support a zero divisor");
  }

  public static AlloyEvaluatorError integerAggregationRequiresUnaryIntegers(
      Pos pos, String operation, AtomTuple tuple) {
    return new AlloyEvaluatorError(
        pos,
        operation
            + " requires a unary set of integers; found tuple "
            + tuple
            + " with arity "
            + tuple.arity());
  }
}
