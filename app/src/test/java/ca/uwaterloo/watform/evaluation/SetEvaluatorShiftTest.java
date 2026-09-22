package ca.uwaterloo.watform.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uwaterloo.watform.alloyast.expr.binary.AlloyShAExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyShLExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyShRExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNumExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyStepsExpr;
import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import org.junit.jupiter.api.Test;

public class SetEvaluatorShiftTest {
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
  public void shiftsUseAlloyFourBitSemantics() {
    SetEvaluator evaluator = evaluator();

    TupleSet left = new AlloyShLExpr(new AlloyNumExpr(3), new AlloyNumExpr(1)).accept(evaluator);
    TupleSet arithmetic =
        new AlloyShAExpr(new AlloyNumExpr(-3), new AlloyNumExpr(1)).accept(evaluator);
    TupleSet logical =
        new AlloyShRExpr(new AlloyNumExpr(-3), new AlloyNumExpr(1)).accept(evaluator);

    assertEquals(new IntegerAtom(6), left.getScalar());
    assertEquals(new IntegerAtom(-2), arithmetic.getScalar());
    assertEquals(new IntegerAtom(6), logical.getScalar());
  }

  @Test
  public void shiftDistanceUsesOnlyBitsNeededForTheInstanceBitwidth() {
    TupleSet result =
        new AlloyShLExpr(new AlloyNumExpr(3), new AlloyNumExpr(5)).accept(evaluator());

    assertEquals(new IntegerAtom(6), result.getScalar());

    // A five-bit integer uses the low three bits of the distance. Thus 13 becomes 5,
    // rather than 13 modulo 5 (which would be 3).
    TupleSet fiveBitResult =
        new AlloyShLExpr(new AlloyNumExpr(1), new AlloyNumExpr(13)).accept(evaluator(5));
    assertTrue(fiveBitResult.containsOverflow());
  }

  @Test
  public void overflowingLeftShiftUsesTheExistingOverflowAbstraction() {
    TupleSet result =
        new AlloyShLExpr(new AlloyNumExpr(4), new AlloyNumExpr(1)).accept(evaluator());

    assertTrue(result.containsOverflow());
  }

  @Test
  public void zeroShiftPreservesAnOverflowingValue() {
    TupleSet result =
        new AlloyShLExpr(new AlloyNumExpr(8), new AlloyNumExpr(0)).accept(evaluator());

    assertTrue(result.containsOverflow());
    assertEquals(OverflowAtom.OverflowDirection.OVERFLOW_UP, Atom.directionOf(result.getScalar()));
  }

  @Test
  public void unsupportedSyntaxIsNotClassifiedAsAnImplementationError() {
    assertThrows(AlloyEvaluatorError.class, () -> new AlloyStepsExpr().accept(evaluator()));
  }
}
