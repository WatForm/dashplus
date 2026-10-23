package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_DOWN;
import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_UNKNOWN;
import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_UP;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyFunAddExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyFunDivExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyFunMulExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyFunRemExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyFunSubExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNumExpr;
import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection;
import org.junit.jupiter.api.Test;

public class SetEvaluatorArithmeticTest {
  private static final int MIN = -8;
  private static final int MAX = 7;

  private enum Operation {
    ADD,
    SUB,
    MUL,
    DIV,
    REM
  }

  private static SetEvaluator evaluator() {
    String xml =
        "<alloy><instance bitwidth=\"4\" maxseq=\"4\"><sig label=\"univ\" ID=\"0\" builtin=\"yes\"/></instance></alloy>";
    var model = new AlloyModel("arithmetic-test.als");
    model.resolve();
    EvaluationTable table = new EvaluationTable(new Instance(xml), model);
    FormulaEvaluator formulaEvaluator = new FormulaEvaluator(table, false);
    return new SetEvaluator(table, false, formulaEvaluator);
  }

  @Test
  public void additionCoversEveryPairOfConcreteFourBitIntegers() {
    SetEvaluator evaluator = evaluator();
    for (int left = MIN; left <= MAX; left++) {
      for (int right = MIN; right <= MAX; right++) {
        assertConcreteResult(evaluator, Operation.ADD, left, right, left + right);
      }
    }
  }

  @Test
  public void subtractionCoversEveryPairOfConcreteFourBitIntegers() {
    SetEvaluator evaluator = evaluator();
    for (int left = MIN; left <= MAX; left++) {
      for (int right = MIN; right <= MAX; right++) {
        assertConcreteResult(evaluator, Operation.SUB, left, right, left - right);
      }
    }
  }

  @Test
  public void multiplicationCoversEveryPairOfConcreteFourBitIntegers() {
    SetEvaluator evaluator = evaluator();
    for (int left = MIN; left <= MAX; left++) {
      for (int right = MIN; right <= MAX; right++) {
        assertConcreteResult(evaluator, Operation.MUL, left, right, left * right);
      }
    }
  }

  @Test
  public void divisionCoversEveryNonzeroConcreteDivisor() {
    SetEvaluator evaluator = evaluator();
    for (int left = MIN; left <= MAX; left++) {
      for (int right = MIN; right <= MAX; right++) {
        if (right != 0) assertConcreteResult(evaluator, Operation.DIV, left, right, left / right);
      }
    }
  }

  @Test
  public void remainderCoversEveryNonzeroConcreteDivisor() {
    SetEvaluator evaluator = evaluator();
    for (int left = MIN; left <= MAX; left++) {
      for (int right = MIN; right <= MAX; right++) {
        if (right != 0) assertConcreteResult(evaluator, Operation.REM, left, right, left % right);
      }
    }
  }

  @Test
  public void additionPreservesOnlyGuaranteedOverflowDirections() {
    SetEvaluator evaluator = evaluator();

    assertOverflow(evaluator, Operation.ADD, up(), number(0), OVERFLOW_UP);
    assertOverflow(evaluator, Operation.ADD, number(0), down(), OVERFLOW_DOWN);
    assertOverflow(evaluator, Operation.ADD, up(), number(MAX), OVERFLOW_UP);
    assertOverflow(evaluator, Operation.ADD, down(), number(MIN), OVERFLOW_DOWN);
    assertOverflow(evaluator, Operation.ADD, up(), number(-1), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.ADD, down(), number(1), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.ADD, up(), up(), OVERFLOW_UP);
    assertOverflow(evaluator, Operation.ADD, down(), down(), OVERFLOW_DOWN);
    assertOverflow(evaluator, Operation.ADD, up(), down(), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.ADD, unknown(), number(0), OVERFLOW_UNKNOWN);
  }

  @Test
  public void subtractionAccountsForTheAsymmetricIntegerBounds() {
    SetEvaluator evaluator = evaluator();

    assertOverflow(evaluator, Operation.SUB, up(), number(0), OVERFLOW_UP);
    assertOverflow(evaluator, Operation.SUB, down(), number(0), OVERFLOW_DOWN);
    assertOverflow(evaluator, Operation.SUB, up(), number(MIN), OVERFLOW_UP);
    assertOverflow(evaluator, Operation.SUB, down(), number(MAX), OVERFLOW_DOWN);
    assertOverflow(evaluator, Operation.SUB, up(), number(1), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.SUB, down(), number(-1), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.SUB, up(), down(), OVERFLOW_UP);
    assertOverflow(evaluator, Operation.SUB, down(), up(), OVERFLOW_DOWN);
    assertOverflow(evaluator, Operation.SUB, up(), up(), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.SUB, down(), down(), OVERFLOW_UNKNOWN);

    assertOverflow(evaluator, Operation.SUB, number(0), down(), OVERFLOW_UP);
    assertOverflow(evaluator, Operation.SUB, number(-1), down(), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.SUB, number(0), up(), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.SUB, number(-1), up(), OVERFLOW_DOWN);
  }

  @Test
  public void multiplicationHandlesZeroSignsAndBoundaryMagnitude() {
    SetEvaluator evaluator = evaluator();

    assertInteger(evaluator, Operation.MUL, up(), number(0), 0);
    assertInteger(evaluator, Operation.MUL, number(0), down(), 0);
    assertInteger(evaluator, Operation.MUL, unknown(), number(0), 0);
    assertOverflow(evaluator, Operation.MUL, up(), number(1), OVERFLOW_UP);
    assertOverflow(evaluator, Operation.MUL, down(), number(-1), OVERFLOW_UP);
    assertOverflow(evaluator, Operation.MUL, down(), number(1), OVERFLOW_DOWN);
    assertOverflow(evaluator, Operation.MUL, up(), number(-1), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.MUL, up(), number(-2), OVERFLOW_DOWN);
    assertOverflow(evaluator, Operation.MUL, down(), up(), OVERFLOW_DOWN);
    assertOverflow(evaluator, Operation.MUL, down(), down(), OVERFLOW_UP);
    assertOverflow(evaluator, Operation.MUL, up(), up(), OVERFLOW_UP);
    assertOverflow(evaluator, Operation.MUL, unknown(), number(1), OVERFLOW_UNKNOWN);
  }

  @Test
  public void divisionHandlesIdentityBoundaryDivisorsAndUnknownMagnitudes() {
    SetEvaluator evaluator = evaluator();

    assertOverflow(evaluator, Operation.DIV, up(), number(1), OVERFLOW_UP);
    assertOverflow(evaluator, Operation.DIV, down(), number(1), OVERFLOW_DOWN);
    assertOverflow(evaluator, Operation.DIV, down(), number(-1), OVERFLOW_UP);
    assertOverflow(evaluator, Operation.DIV, up(), number(-1), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.DIV, up(), number(2), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.DIV, down(), number(2), OVERFLOW_UNKNOWN);
    assertInteger(evaluator, Operation.DIV, number(0), up(), 0);
    assertInteger(evaluator, Operation.DIV, number(MAX), up(), 0);
    assertOverflow(evaluator, Operation.DIV, number(MIN), up(), OVERFLOW_UNKNOWN);
    assertInteger(evaluator, Operation.DIV, number(MIN), down(), 0);
    assertOverflow(evaluator, Operation.DIV, up(), down(), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.DIV, unknown(), number(1), OVERFLOW_UNKNOWN);
  }

  @Test
  public void remainderHandlesUnitAndOverflowingDivisors() {
    SetEvaluator evaluator = evaluator();

    assertInteger(evaluator, Operation.REM, up(), number(1), 0);
    assertInteger(evaluator, Operation.REM, down(), number(-1), 0);
    assertInteger(evaluator, Operation.REM, unknown(), number(1), 0);
    assertOverflow(evaluator, Operation.REM, up(), number(2), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.REM, down(), number(-2), OVERFLOW_UNKNOWN);
    assertInteger(evaluator, Operation.REM, number(0), up(), 0);
    assertInteger(evaluator, Operation.REM, number(MAX), up(), MAX);
    assertInteger(evaluator, Operation.REM, number(MIN), down(), MIN);
    assertOverflow(evaluator, Operation.REM, number(MIN), up(), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.REM, up(), down(), OVERFLOW_UNKNOWN);
  }

  @Test
  public void divisionAndRemainderAlwaysRejectAConcreteZeroDivisor() {
    SetEvaluator evaluator = evaluator();
    AlloyExpr[] numerators = {number(MIN), number(0), number(MAX), down(), up(), unknown()};

    for (AlloyExpr numerator : numerators) {
      assertThrows(
          AlloyEvaluatorError.class,
          () -> evaluate(evaluator, Operation.DIV, numerator, number(0)));
      assertThrows(
          AlloyEvaluatorError.class,
          () -> evaluate(evaluator, Operation.REM, numerator, number(0)));
    }
  }

  private static void assertConcreteResult(
      SetEvaluator evaluator, Operation operation, int left, int right, int mathematicalResult) {
    Atom expected =
        mathematicalResult > MAX
            ? new OverflowAtom(OVERFLOW_UP)
            : mathematicalResult < MIN
                ? new OverflowAtom(OVERFLOW_DOWN)
                : new IntegerAtom(mathematicalResult);
    Atom actual = evaluate(evaluator, operation, number(left), number(right)).getScalar();
    assertAtom(expected, actual, left + " " + operation + " " + right);
  }

  private static void assertInteger(
      SetEvaluator evaluator,
      Operation operation,
      AlloyExpr left,
      AlloyExpr right,
      int expected) {
    assertAtom(
        new IntegerAtom(expected),
        evaluate(evaluator, operation, left, right).getScalar(),
        operation.toString());
  }

  private static void assertOverflow(
      SetEvaluator evaluator,
      Operation operation,
      AlloyExpr left,
      AlloyExpr right,
      OverflowDirection expected) {
    assertAtom(
        new OverflowAtom(expected),
        evaluate(evaluator, operation, left, right).getScalar(),
        operation.toString());
  }

  private static void assertAtom(Atom expected, Atom actual, String context) {
    assertTrue(
        Atom.structurallyIdentical(expected, actual),
        () -> context + ": expected " + expected + " but got " + actual);
  }

  private static TupleSet evaluate(
      SetEvaluator evaluator, Operation operation, AlloyExpr left, AlloyExpr right) {
    return operation(operation, left, right).accept(evaluator);
  }

  private static AlloyExpr operation(Operation operation, AlloyExpr left, AlloyExpr right) {
    return switch (operation) {
      case ADD -> new AlloyFunAddExpr(left, right);
      case SUB -> new AlloyFunSubExpr(left, right);
      case MUL -> new AlloyFunMulExpr(left, right);
      case DIV -> new AlloyFunDivExpr(left, right);
      case REM -> new AlloyFunRemExpr(left, right);
    };
  }

  private static AlloyExpr number(int value) {
    return new AlloyNumExpr(value);
  }

  private static AlloyExpr up() {
    return number(MAX + 1);
  }

  private static AlloyExpr down() {
    return number(MIN - 1);
  }

  private static AlloyExpr unknown() {
    return new AlloyFunAddExpr(up(), down());
  }
}
