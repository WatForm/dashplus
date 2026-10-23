package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_DOWN;
import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_UP;
import static ca.uwaterloo.watform.evaluation.ThreeVal.FALSE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.UNKNOWN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyDiffExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyEqualsExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyIntersExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyUnionExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNoneExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNumExpr;
import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import org.junit.jupiter.api.Test;

public class EvaluatorBasicSetOperationsTest {
  private static EvaluationTable evaluationTable() {
    // Bitwidth is 4, all numbers are in range [-8, 7]
    String xml =
        "<alloy><instance bitwidth=\"4\" maxseq=\"4\"><sig label=\"univ\" ID=\"0\" builtin=\"yes\"/></instance></alloy>";
    var model = new AlloyModel("basic-set-operations-test.als");
    model.resolve();
    return new EvaluationTable(new Instance(xml), model);
  }

  private static FormulaEvaluator evaluator() {
    return new FormulaEvaluator(evaluationTable(), false);
  }

  private static TupleSet evaluateSet(AlloyExpr expression) {
    EvaluationTable table = evaluationTable();
    FormulaEvaluator formulaEvaluator = new FormulaEvaluator(table, false);
    return expression.accept(new SetEvaluator(table, false, formulaEvaluator));
  }

  private static AlloyExpr set(int... values) {
    if (values.length == 0) return new AlloyNoneExpr();

    AlloyExpr result = new AlloyNumExpr(values[0]);
    for (int i = 1; i < values.length; i++) {
      result = new AlloyUnionExpr(result, new AlloyNumExpr(values[i]));
    }
    return result;
  }

  private static ThreeVal equals(AlloyExpr left, AlloyExpr right) {
    return new AlloyEqualsExpr(left, right).accept(evaluator());
  }

  @Test
  public void unionProducesTheExpectedConcreteSet() {
    AlloyExpr union = new AlloyUnionExpr(set(1, 2), set(2, 3));

    assertEquals(TRUE, equals(union, set(1, 2, 3)));
    assertEquals(FALSE, equals(union, set(1, 2)));
  }

  @Test
  public void intersectionProducesTheExpectedConcreteSet() {
    AlloyExpr intersection = new AlloyIntersExpr(set(1, 2), set(2, 3));

    assertEquals(TRUE, equals(intersection, set(2)));
    assertEquals(FALSE, equals(intersection, set(1)));
  }

  @Test
  public void differenceProducesTheExpectedConcreteSet() {
    AlloyExpr difference = new AlloyDiffExpr(set(1, 2, 3), set(2, 3));

    assertEquals(TRUE, equals(difference, set(1)));
    assertEquals(FALSE, equals(difference, set(2)));
  }

  @Test
  public void unionDoesNotClaimEqualityWhenOverflowAtomsMayDiffer() {
    AlloyExpr union = new AlloyUnionExpr(set(1, 8), set(1, 9));

    assertEquals(UNKNOWN, equals(union, set(1, 8)));
    assertEquals(UNKNOWN, equals(union, set(1, 8, 9)));
  }

  @Test
  public void intersectionDoesNotChooseAResultWhenOverflowMembershipIsAmbiguous() {
    AlloyExpr values = set(1, 8);
    AlloyExpr intersection = new AlloyIntersExpr(values, values);

    assertEquals(UNKNOWN, equals(intersection, set(1)));
    assertEquals(UNKNOWN, equals(intersection, values));
  }

  @Test
  public void intersectionRemainsConcreteForOppositeOverflowDirections() {
    AlloyExpr intersection = new AlloyIntersExpr(set(1, 2, -10), set(1, 2, 9));
    TupleSet result = evaluateSet(intersection);

    assertTrue(result.isConcrete());
    assertEquals(2, result.size());
    assertEquals(TRUE, equals(intersection, set(1, 2)));
  }

  @Test
  public void differenceDoesNotChooseAResultWhenOverflowMembershipIsAmbiguous() {
    AlloyExpr values = set(1, 8);
    AlloyExpr difference = new AlloyDiffExpr(values, values);

    assertEquals(UNKNOWN, equals(difference, set()));
    assertEquals(UNKNOWN, equals(difference, values));
  }

  @Test
  public void differenceKeepsOverflowAbsentFromTheOppositeDirection() {
    TupleSet downResult = evaluateSet(new AlloyDiffExpr(set(1, 2, -10), set(1, 2, 9)));
    TupleSet upResult = evaluateSet(new AlloyDiffExpr(set(1, 2, 9), set(1, 2, -10)));

    assertTrue(downResult.isConcrete());
    assertEquals(1, downResult.size());
    assertEquals(
        OVERFLOW_DOWN, assertInstanceOf(OverflowAtom.class, downResult.getScalar()).direction());

    assertTrue(upResult.isConcrete());
    assertEquals(1, upResult.size());
    assertEquals(
        OVERFLOW_UP, assertInstanceOf(OverflowAtom.class, upResult.getScalar()).direction());
  }
}
