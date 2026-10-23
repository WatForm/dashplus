package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_DOWN;
import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_UNKNOWN;
import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_UP;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyFunAddExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyIntersExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyShAExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyShLExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyShRExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyUnionExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNumExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyStepsExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyStrLiteralExpr;
import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection;
import org.junit.jupiter.api.Test;

public class SetEvaluatorShiftTest {
  private enum Operation {
    LEFT,
    ARITHMETIC_RIGHT,
    LOGICAL_RIGHT
  }

  private static SetEvaluator evaluator() {
    return evaluator(4);
  }

  private static SetEvaluator evaluator(int bitWidth) {
    String xml =
        "<alloy><instance bitwidth=\""
            + bitWidth
            + "\" maxseq=\"4\"><sig label=\"univ\" ID=\"0\" builtin=\"yes\"/></instance></alloy>";
    var instance = new Instance(xml);
    var model = new AlloyModel("shift-test.als");
    model.resolve();
    var table = new EvaluationTable(instance, model);
    return new SetEvaluator(table, false, null);
  }

  @Test
  public void shiftsCoverEveryPairOfConcreteFourBitIntegers() {
    assertAllConcreteShifts(4);
  }

  @Test
  public void shiftsCoverEveryPairOfConcreteFiveBitIntegers() {
    // Five bits are important here: shift distances use three low-order bits and can
    // therefore select amounts 5, 6, and 7, which exceed the value's bitwidth.
    assertAllConcreteShifts(5);
  }

  @Test
  public void oneBitIntegersHaveOnlyTheZeroShiftDistance() {
    assertAllConcreteShifts(1);
  }

  @Test
  public void overflowingValuesPreserveOnlyGuaranteedInformation() {
    SetEvaluator evaluator = evaluator();

    for (Operation operation : Operation.values()) {
      for (int zeroAmountDistance : new int[] {0, 4, -8}) {
        assertOverflow(
            evaluator, operation, up(4), number(zeroAmountDistance), OVERFLOW_UP);
        assertOverflow(
            evaluator, operation, down(4), number(zeroAmountDistance), OVERFLOW_DOWN);
        assertOverflow(
            evaluator, operation, unknown(4), number(zeroAmountDistance), OVERFLOW_UNKNOWN);
      }
    }

    assertOverflow(evaluator, Operation.LEFT, up(4), number(1), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.LEFT, down(4), number(1), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.LEFT, unknown(4), number(1), OVERFLOW_UNKNOWN);

    assertOverflow(
        evaluator, Operation.ARITHMETIC_RIGHT, up(4), number(1), OVERFLOW_UNKNOWN);
    assertOverflow(
        evaluator, Operation.ARITHMETIC_RIGHT, down(4), number(1), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.LOGICAL_RIGHT, up(4), number(1), OVERFLOW_UNKNOWN);
    assertOverflow(evaluator, Operation.LOGICAL_RIGHT, down(4), number(1), OVERFLOW_UNKNOWN);
  }

  @Test
  public void overflowingShiftDistancesProduceUnknownOverflow() {
    SetEvaluator evaluator = evaluator();
    AlloyExpr[] distances = {up(4), down(4), unknown(4)};

    for (Operation operation : Operation.values()) {
      for (AlloyExpr distance : distances) {
        assertOverflow(evaluator, operation, number(1), distance, OVERFLOW_UNKNOWN);
      }
    }
  }

  @Test
  public void concreteLeftShiftWrapsWithinTheInstanceBitwidth() {
    SetEvaluator fourBitEvaluator = evaluator(4);
    assertResult(fourBitEvaluator, Operation.LEFT, 4, 1, new IntegerAtom(-8));
    assertResult(fourBitEvaluator, Operation.LEFT, 7, 1, new IntegerAtom(-2));
    assertResult(fourBitEvaluator, Operation.LEFT, -8, 1, new IntegerAtom(0));
    assertResult(fourBitEvaluator, Operation.LEFT, 3, 5, new IntegerAtom(6));

    SetEvaluator fiveBitEvaluator = evaluator(5);
    assertResult(fiveBitEvaluator, Operation.LEFT, 1, 5, new IntegerAtom(0));
    assertResult(fiveBitEvaluator, Operation.LEFT, 1, 8, new IntegerAtom(1));
  }

  @Test
  public void shiftsThatDiscardEveryBitUseTheirGuaranteedResult() {
    SetEvaluator evaluator = evaluator(5);
    AlloyExpr[] values = {up(5), down(5), unknown(5)};

    for (AlloyExpr value : values) {
      assertAtom(
          new IntegerAtom(0),
          evaluate(evaluator, Operation.LEFT, value, number(5)).getScalar(),
          "left shift by the bitwidth");
      assertAtom(
          new IntegerAtom(0),
          evaluate(evaluator, Operation.LOGICAL_RIGHT, value, number(5)).getScalar(),
          "logical right shift by the bitwidth");
      assertOverflow(
          evaluator, Operation.ARITHMETIC_RIGHT, value, number(5), OVERFLOW_UNKNOWN);
    }
  }

  @Test
  public void unspecifiedOperandsPropagateThroughEveryShift() {
    SetEvaluator evaluator = evaluator();
    AlloyExpr values = new AlloyUnionExpr(number(1), up(4));
    AlloyExpr unspecified = new AlloyIntersExpr(values, values);

    for (Operation operation : Operation.values()) {
      assertTrue(evaluate(evaluator, operation, unspecified, number(1)).isUnspecified());
      assertTrue(evaluate(evaluator, operation, number(1), unspecified).isUnspecified());
    }
  }

  @Test
  public void labelsCannotBeShiftedOrUsedAsShiftDistances() {
    SetEvaluator evaluator = evaluator();
    AlloyExpr label = new AlloyStrLiteralExpr("not-an-integer");

    for (Operation operation : Operation.values()) {
      assertThrows(
          AlloyEvaluatorImplError.class,
          () -> evaluate(evaluator, operation, label, number(1)));
      assertThrows(
          AlloyEvaluatorImplError.class,
          () -> evaluate(evaluator, operation, number(1), label));
    }
  }

  private static void assertAllConcreteShifts(int bitWidth) {
    SetEvaluator evaluator = evaluator(bitWidth);
    int min = minInt(bitWidth);
    int max = maxInt(bitWidth);

    for (int value = min; value <= max; value++) {
      for (int distance = min; distance <= max; distance++) {
        int amount = normalizedDistance(bitWidth, distance);
        int leftResult = wrappedLeftShift(bitWidth, value, amount);
        int arithmeticResult = value >> amount;
        // A zero-distance logical shift preserves the signed value. Once at least one
        // zero-fill shift occurs, the sign bit is cleared and the unsigned projection
        // gives the ordinary non-negative result.
        int logicalResult = amount == 0 ? value : (value & bitMask(bitWidth)) >>> amount;

        assertResult(
            evaluator, Operation.LEFT, value, distance, new IntegerAtom(leftResult));
        assertResult(
            evaluator,
            Operation.ARITHMETIC_RIGHT,
            value,
            distance,
            new IntegerAtom(arithmeticResult));
        assertResult(
            evaluator,
            Operation.LOGICAL_RIGHT,
            value,
            distance,
            new IntegerAtom(logicalResult));
      }
    }
  }

  private static int wrappedLeftShift(int bitWidth, int value, int amount) {
    long modulus = 1L << bitWidth;
    long mask = modulus - 1;
    long shiftedBits = ((value & mask) << amount) & mask;
    long signBit = 1L << (bitWidth - 1);
    return (int) ((shiftedBits & signBit) == 0 ? shiftedBits : shiftedBits - modulus);
  }

  private static int normalizedDistance(int bitWidth, int distance) {
    int distanceBitCount = Integer.SIZE - Integer.numberOfLeadingZeros(bitWidth - 1);
    return Math.floorMod(distance, 1 << distanceBitCount);
  }

  private static int bitMask(int bitWidth) {
    return (1 << bitWidth) - 1;
  }

  private static int minInt(int bitWidth) {
    return -(1 << (bitWidth - 1));
  }

  private static int maxInt(int bitWidth) {
    return (1 << (bitWidth - 1)) - 1;
  }

  private static void assertResult(
      SetEvaluator evaluator, Operation operation, int value, int distance, Atom expected) {
    Atom actual = evaluate(evaluator, operation, number(value), number(distance)).getScalar();
    assertAtom(expected, actual, value + " " + operation + " " + distance);
  }

  private static void assertOverflow(
      SetEvaluator evaluator,
      Operation operation,
      AlloyExpr value,
      AlloyExpr distance,
      OverflowDirection expected) {
    Atom actual = evaluate(evaluator, operation, value, distance).getScalar();
    assertAtom(new OverflowAtom(expected), actual, operation.toString());
  }

  private static TupleSet evaluate(
      SetEvaluator evaluator, Operation operation, AlloyExpr value, AlloyExpr distance) {
    return operation(operation, value, distance).accept(evaluator);
  }

  private static AlloyExpr operation(
      Operation operation, AlloyExpr value, AlloyExpr distance) {
    return switch (operation) {
      case LEFT -> new AlloyShLExpr(value, distance);
      case ARITHMETIC_RIGHT -> new AlloyShAExpr(value, distance);
      case LOGICAL_RIGHT -> new AlloyShRExpr(value, distance);
    };
  }

  private static AlloyExpr number(int value) {
    return new AlloyNumExpr(value);
  }

  private static AlloyExpr up(int bitWidth) {
    return number(maxInt(bitWidth) + 1);
  }

  private static AlloyExpr down(int bitWidth) {
    return number(minInt(bitWidth) - 1);
  }

  private static AlloyExpr unknown(int bitWidth) {
    return new AlloyFunAddExpr(up(bitWidth), down(bitWidth));
  }

  private static void assertAtom(Atom expected, Atom actual, String context) {
    assertTrue(
        Atom.structurallyIdentical(expected, actual),
        () -> context + ": expected " + expected + " but got " + actual);
  }
}
