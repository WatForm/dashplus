package ca.uwaterloo.watform.evaluation;

import ca.uwaterloo.watform.alloymodel.Qname;
import ca.uwaterloo.watform.utils.ImplementationError;
import ca.uwaterloo.watform.utils.Pos;

public class AlloyEvaluatorImplError extends ImplementationError {

  private AlloyEvaluatorImplError(String msg) {
    super(msg);
  }

  private AlloyEvaluatorImplError(Pos pos, String msg) {
    super(pos, msg);
  }

  public static AlloyEvaluatorImplError missingVisitCase(
      String evaluator, Pos pos, String exprDescription) {
    return new AlloyEvaluatorImplError(
        pos, evaluator + " has no implementation for: " + exprDescription);
  }

  public static AlloyEvaluatorImplError unresolvedQname(Pos pos, String expression) {
    return new AlloyEvaluatorImplError(
        pos, "Cannot evaluate Qname expression without resolved name components: " + expression);
  }

  public static AlloyEvaluatorImplError relationNotInInstance(Pos pos, Qname relation) {
    return new AlloyEvaluatorImplError(
        pos,
        "Resolved relation is absent from the XML instance: "
            + relation
            + " (the model and instance may not match)");
  }

  public static AlloyEvaluatorImplError predicateNotInEvaluationTable(Pos pos, Qname predicate) {
    return new AlloyEvaluatorImplError(
        pos, "Resolved predicate is absent from the evaluation symbol table: " + predicate);
  }

  public static AlloyEvaluatorImplError functionNotInEvaluationTable(Pos pos, Qname function) {
    return new AlloyEvaluatorImplError(
        pos, "Resolved function is absent from the evaluation symbol table: " + function);
  }

  public static AlloyEvaluatorImplError callableNotInEvaluationTable(Pos pos, Qname callable) {
    return new AlloyEvaluatorImplError(
        pos,
        "Resolved function or predicate is absent from the evaluation symbol table: " + callable);
  }

  public static AlloyEvaluatorImplError callableArgumentCount(
      Pos pos, String kind, Qname name, int expected, int actual) {
    return new AlloyEvaluatorImplError(
        pos, kind + " " + name + " expects " + expected + " arguments but received " + actual);
  }

  public static AlloyEvaluatorImplError functionBodyExpressionCount(
      Pos pos, Qname function, int actual) {
    return new AlloyEvaluatorImplError(
        pos, "Function " + function + " must have exactly one body expression; found " + actual);
  }

  public static AlloyEvaluatorImplError sumQuantifierInBooleanContext(Pos pos) {
    return new AlloyEvaluatorImplError(
        pos, "SUM quantification reached FormulaEvaluator even though it produces a set value");
  }

  public static AlloyEvaluatorImplError arithmeticOperandsNotIntegers(
      Pos pos, String operation, Atom first, Atom second) {
    return new AlloyEvaluatorImplError(
        pos,
        "Arithmetic function "
            + operation
            + " requires integer operands; received "
            + first.getClass().getSimpleName()
            + " and "
            + second.getClass().getSimpleName());
  }

  public static AlloyEvaluatorImplError tupleIndexOutOfBounds(int index, int arity) {
    return new AlloyEvaluatorImplError(
        "Tuple index " + index + " is outside the valid range [0, " + arity + ")");
  }

  public static AlloyEvaluatorImplError invalidTupleJoin(
      AtomTuple left, AtomTuple right, ThreeVal sharedAtomsEqual) {
    return new AlloyEvaluatorImplError(
        "AtomTuple.join requires definitely equal shared atoms, but "
            + left.last()
            + " and "
            + right.first()
            + " compared as "
            + sharedAtomsEqual
            + " (left="
            + left
            + ", right="
            + right
            + ")");
  }

  public static AlloyEvaluatorImplError scalarRequired(int tupleCount, Integer tupleArity) {
    return new AlloyEvaluatorImplError(
        "A scalar value requires exactly one unary tuple; found "
            + tupleCount
            + " tuple(s)"
            + (tupleArity == null ? "" : " with arity " + tupleArity));
  }

  public static AlloyEvaluatorImplError negativeCallableArity(
      String kind, Qname callable, int arity) {
    return new AlloyEvaluatorImplError(
        kind + " call for " + callable + " was created with negative arity " + arity);
  }

  public static AlloyEvaluatorImplError nonConcreteTupleAccess(TupleSet.Kind kind) {
    return new AlloyEvaluatorImplError(
        "Concrete tuples were requested from a TupleSet of kind " + kind);
  }

  public static AlloyEvaluatorImplError partialFunctionMisuse() {
    return new AlloyEvaluatorImplError(
        "An incomplete function call may only be used as the right operand of a join");
  }

  public static AlloyEvaluatorImplError excessCallableArguments(
      String kind, Qname callable, int expected, int actual) {
    return new AlloyEvaluatorImplError(
        kind + " " + callable + " expects " + expected + " arguments but received " + actual);
  }

  public static AlloyEvaluatorImplError incompletePredicateCall(
      Qname predicate, int expected, int actual) {
    return new AlloyEvaluatorImplError(
        "Predicate "
            + predicate
            + " cannot be evaluated until all "
            + expected
            + " arguments are supplied; received "
            + actual);
  }

  public static AlloyEvaluatorImplError predicateCallMisuse(
      Qname predicate, int actual, int expected) {
    return new AlloyEvaluatorImplError(
        "Predicate call "
            + predicate
            + " ("
            + actual
            + "/"
            + expected
            + " arguments) may only receive arguments through joins or be evaluated as a formula");
  }

  public static AlloyEvaluatorImplError predicateValueRequired(TupleSet.Kind actual) {
    return new AlloyEvaluatorImplError(
        "Predicate evaluation requires a predicate call, but received a TupleSet of kind "
            + actual);
  }

  public static AlloyEvaluatorImplError partialFunctionResult(Qname function) {
    return new AlloyEvaluatorImplError(
        "Function "
            + function
            + " still produced an incomplete function call after receiving all arguments");
  }

  public static AlloyEvaluatorImplError baseEvaluationFrameRemoval() {
    return new AlloyEvaluatorImplError("Attempted to remove the permanent base evaluation frame");
  }

  public static AlloyEvaluatorImplError validatedCliWithoutMode() {
    return new AlloyEvaluatorImplError(
        "Evaluation CLI passed argument validation without selecting an execution mode");
  }

  public static AlloyEvaluatorImplError orderedComparisonOnLabel(Atom first, Atom second) {
    return new AlloyEvaluatorImplError(
        "Ordered comparison requires integer atoms; received " + first + " and " + second);
  }

  public static AlloyEvaluatorImplError unexpectedOverflowDirection(Atom atom) {
    return new AlloyEvaluatorImplError(
        "Expected an overflow direction while comparing atom " + atom);
  }

  public static AlloyEvaluatorImplError unexpectedAtomSubtype(Atom atom) {
    return new AlloyEvaluatorImplError(
        "Atom implementation is not handled by the evaluator: " + atom.getClass().getName());
  }

  public static AlloyEvaluatorImplError unexpectedOverflowEnumValue(
      OverflowAtom.OverflowDirection direction) {
    return new AlloyEvaluatorImplError(
        "Overflow direction is not handled by the evaluator: " + direction.name());
  }
}
