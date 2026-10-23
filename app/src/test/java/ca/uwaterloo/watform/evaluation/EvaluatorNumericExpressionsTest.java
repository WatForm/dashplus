package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_UNKNOWN;
import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_UP;
import static ca.uwaterloo.watform.evaluation.ThreeVal.FALSE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.UNKNOWN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyCmpExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyIntersExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyUnionExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyCardExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNoneExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNumExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloySigIntExpr;
import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import java.util.List;
import org.junit.jupiter.api.Test;

public class EvaluatorNumericExpressionsTest {
  private static final int MIN = -8;
  private static final int MAX = 7;

  private static EvaluationTable table() {
    String xml =
        "<alloy><instance bitwidth=\"4\" maxseq=\"4\"><sig label=\"univ\" ID=\"0\" builtin=\"yes\"/></instance></alloy>";
    var model = new AlloyModel("numeric-expressions-test.als");
    model.resolve();
    return new EvaluationTable(new Instance(xml), model);
  }

  private static SetEvaluator setEvaluator() {
    return new SetEvaluator(table(), false, null);
  }

  private static FormulaEvaluator formulaEvaluator() {
    return new FormulaEvaluator(table(), false);
  }

  @Test
  public void intContainsEveryRepresentableIntegerAndNoOverflowAtoms() {
    TupleSet integers = new AlloySigIntExpr().accept(setEvaluator());

    assertEquals(16, integers.size());
    assertEquals(TRUE, integers.contains(tuple(new IntegerAtom(MIN))));
    assertEquals(TRUE, integers.contains(tuple(new IntegerAtom(0))));
    assertEquals(TRUE, integers.contains(tuple(new IntegerAtom(MAX))));
    assertFalse(integers.containsOverflow());
  }

  @Test
  public void cardinalityCountsSetTuplesAndRespectsSetDeduplication() {
    SetEvaluator evaluator = setEvaluator();

    assertAtom(new IntegerAtom(0), new AlloyCardExpr(new AlloyNoneExpr()).accept(evaluator));
    assertAtom(
        new IntegerAtom(1),
        new AlloyCardExpr(new AlloyUnionExpr(number(1), number(1))).accept(evaluator));
    assertAtom(
        new IntegerAtom(2),
        new AlloyCardExpr(new AlloyUnionExpr(number(1), number(2))).accept(evaluator));
  }

  @Test
  public void cardinalityPreservesUncertaintyAndReportsNumericOverflow() {
    SetEvaluator evaluator = setEvaluator();
    AlloyExpr valuesWithOverflow = new AlloyUnionExpr(number(1), number(MAX + 1));
    AlloyExpr unspecified = new AlloyIntersExpr(valuesWithOverflow, valuesWithOverflow);

    assertTrue(new AlloyCardExpr(unspecified).accept(evaluator).isUnspecified());
    assertAtom(
        new OverflowAtom(OVERFLOW_UNKNOWN),
        new AlloyCardExpr(valuesWithOverflow).accept(evaluator));
    assertAtom(
        new OverflowAtom(OVERFLOW_UP), new AlloyCardExpr(new AlloySigIntExpr()).accept(evaluator));
  }

  @Test
  public void comparisonVisitorDispatchesEveryNumericComparison() {
    FormulaEvaluator evaluator = formulaEvaluator();

    assertEquals(TRUE, compare(1, false, AlloyCmpExpr.Comp.LESS_THAN, 2).accept(evaluator));
    assertEquals(FALSE, compare(1, false, AlloyCmpExpr.Comp.GREATER_THAN, 2).accept(evaluator));
    assertEquals(TRUE, compare(1, false, AlloyCmpExpr.Comp.LESS_EQUAL, 2).accept(evaluator));
    assertEquals(TRUE, compare(1, false, AlloyCmpExpr.Comp.EQUAL_LESS, 2).accept(evaluator));
    assertEquals(FALSE, compare(1, false, AlloyCmpExpr.Comp.GREATER_EQUAL, 2).accept(evaluator));

    assertEquals(FALSE, compare(1, true, AlloyCmpExpr.Comp.LESS_THAN, 2).accept(evaluator));
    assertEquals(TRUE, compare(1, true, AlloyCmpExpr.Comp.GREATER_THAN, 2).accept(evaluator));
  }

  @Test
  public void comparisonVisitorPropagatesOverflowAndSetUncertainty() {
    FormulaEvaluator evaluator = formulaEvaluator();
    AlloyExpr up = number(MAX + 1);
    AlloyExpr down = number(MIN - 1);
    AlloyExpr valuesWithOverflow = new AlloyUnionExpr(number(1), up);
    AlloyExpr unspecified = new AlloyIntersExpr(valuesWithOverflow, valuesWithOverflow);

    assertEquals(
        UNKNOWN,
        new AlloyCmpExpr(up, false, AlloyCmpExpr.Comp.LESS_THAN, up).accept(evaluator));
    assertEquals(
        TRUE,
        new AlloyCmpExpr(down, false, AlloyCmpExpr.Comp.LESS_THAN, up).accept(evaluator));
    assertEquals(
        TRUE,
        new AlloyCmpExpr(up, false, AlloyCmpExpr.Comp.GREATER_EQUAL, down).accept(evaluator));
    assertEquals(
        UNKNOWN,
        new AlloyCmpExpr(unspecified, false, AlloyCmpExpr.Comp.LESS_THAN, number(0))
            .accept(evaluator));
  }

  private static AlloyCmpExpr compare(
      int left, boolean negated, AlloyCmpExpr.Comp comparison, int right) {
    return new AlloyCmpExpr(number(left), negated, comparison, number(right));
  }

  private static AlloyExpr number(int value) {
    return new AlloyNumExpr(value);
  }

  private static AtomTuple tuple(Atom atom) {
    return new AtomTuple(List.of(atom));
  }

  private static void assertAtom(Atom expected, TupleSet actual) {
    assertTrue(
        Atom.structurallyIdentical(expected, actual.getScalar()),
        () -> "Expected " + expected + " but got " + actual.getScalar());
  }
}
