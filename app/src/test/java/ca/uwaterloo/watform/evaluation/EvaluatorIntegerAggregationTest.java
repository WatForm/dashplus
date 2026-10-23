package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_DOWN;
import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_UNKNOWN;
import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_UP;
import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.UNKNOWN;
import static ca.uwaterloo.watform.parser.AlloyParser.parsePara;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyEqualsExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyFunAddExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyIntersExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyUnionExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyNumIntExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyNumSumExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNoneExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNumExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyFactPara;
import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import org.junit.jupiter.api.Test;

public class EvaluatorIntegerAggregationTest {
  private enum Aggregation {
    INT_CAST,
    SUM
  }

  private static EvaluationTable table() {
    String xml =
        "<alloy><instance bitwidth=\"4\" maxseq=\"4\"><sig label=\"univ\" ID=\"0\" builtin=\"yes\"/></instance></alloy>";
    var model = new AlloyModel("integer-aggregation-test.als");
    model.resolve();
    return new EvaluationTable(new Instance(xml), model);
  }

  private static FormulaEvaluator evaluator() {
    return new FormulaEvaluator(table(), false);
  }

  private static SetEvaluator setEvaluator() {
    return new SetEvaluator(table(), false, null);
  }

  private static AlloyExpr integers(int first, int second) {
    return new AlloyUnionExpr(new AlloyNumExpr(first), new AlloyNumExpr(second));
  }

  @Test
  public void intCastAndUnarySumBothAggregateIntegerSets() {
    AlloyExpr values = integers(1, 2);
    AlloyExpr intCast = new AlloyNumIntExpr(values);
    AlloyExpr sum = new AlloyNumSumExpr(values);

    assertEquals(TRUE, new AlloyEqualsExpr(intCast, new AlloyNumExpr(3)).accept(evaluator()));
    assertEquals(TRUE, new AlloyEqualsExpr(sum, new AlloyNumExpr(3)).accept(evaluator()));
  }

  @Test
  public void emptyIntegerSetSumsToZero() {
    AlloyExpr intCast = new AlloyNumIntExpr(new AlloyNoneExpr());
    AlloyExpr sum = new AlloyNumSumExpr(new AlloyNoneExpr());

    assertEquals(TRUE, new AlloyEqualsExpr(intCast, new AlloyNumExpr(0)).accept(evaluator()));
    assertEquals(TRUE, new AlloyEqualsExpr(sum, new AlloyNumExpr(0)).accept(evaluator()));
  }

  @Test
  public void unspecifiedInputProducesAnUnspecifiedAggregation() {
    AlloyExpr values = integers(1, 8);
    AlloyExpr unspecified = new AlloyIntersExpr(values, values);

    for (Aggregation aggregation : Aggregation.values()) {
      assertTrue(
          aggregate(aggregation, unspecified).isUnspecified(),
          aggregation + " must preserve an unspecified input");
    }
  }

  @Test
  public void unaryAggregationsDetectConcreteTotalsThatCrossEitherBoundary() {
    assertAggregations(new OverflowAtom(OVERFLOW_UP), integers(1, 7));
    assertAggregations(new OverflowAtom(OVERFLOW_DOWN), integers(-8, -1));
    assertAggregations(new IntegerAtom(-1), integers(-8, 7));
  }

  @Test
  public void unaryAggregationsPreserveOverflowDirectionWhenItIsGuaranteed() {
    assertAggregations(new OverflowAtom(OVERFLOW_UP), integers(0, 8));
    assertAggregations(new OverflowAtom(OVERFLOW_UP), integers(1, 8));
    assertAggregations(new OverflowAtom(OVERFLOW_DOWN), integers(0, -9));
    assertAggregations(new OverflowAtom(OVERFLOW_DOWN), integers(-1, -9));
  }

  @Test
  public void unaryAggregationsBecomeUnknownWhenOverflowCouldCrossABoundary() {
    assertAggregations(new OverflowAtom(OVERFLOW_UNKNOWN), integers(-1, 8));
    assertAggregations(new OverflowAtom(OVERFLOW_UNKNOWN), integers(1, -9));
    assertAggregations(new OverflowAtom(OVERFLOW_UNKNOWN), integers(8, -9));

    AlloyExpr unknownOverflow =
        new AlloyFunAddExpr(new AlloyNumExpr(8), new AlloyNumExpr(-9));
    assertAggregations(new OverflowAtom(OVERFLOW_UNKNOWN), unknownOverflow);
  }

  @Test
  public void sumQuantificationAddsTheBodyForEveryBinding() {
    assertEquals(TRUE, evaluateFact("(sum value: 1 + 2 | value) = 3"));
  }

  @Test
  public void sumQuantificationEvaluatesArithmeticForEveryBinding() {
    assertEquals(TRUE, evaluateFact("(sum value: 1 + 2 | value fun/add 1) = 5"));
  }

  @Test
  public void sumQuantificationCountsDuplicateBodyResultsPerBinding() {
    assertEquals(TRUE, evaluateFact("(sum value: 1 + 2 | 3) = 6"));
  }

  @Test
  public void sumQuantificationHandlesMultipleDeclarationBlocks() {
    assertEquals(TRUE, evaluateFact("(sum x: 0 + 1, y: 0 + 1 | x fun/add y) = 4"));
  }

  @Test
  public void sumQuantificationHonoursDisjointNamesInOneDeclaration() {
    assertEquals(TRUE, evaluateFact("(sum disj x, y: 1 + 2 | x fun/add y) = 6"));
  }

  @Test
  public void sumQuantificationDoesNotHideOverflowInItsBody() {
    assertEquals(TRUE, evaluateFact("(sum value: 1 + 2 | value fun/add 7) > 0"));
  }

  @Test
  public void sumQuantificationPreservesDirectionalOverflowFromItsDomain() {
    assertEquals(TRUE, evaluateFact("(sum value: 0 + 8 | value) > 7"));
    assertEquals(TRUE, evaluateFact("(sum value: 0 + -9 | value) < -8"));
  }

  @Test
  public void sumQuantificationDetectsOverflowAccumulatedAcrossBindings() {
    assertEquals(TRUE, evaluateFact("(sum value: 1 + 2 | 7) > 7"));
    assertEquals(TRUE, evaluateFact("(sum value: 1 + 2 | -8) < -8"));
  }

  @Test
  public void sumQuantificationMakesOpposingOverflowDirectionsUnknown() {
    assertEquals(UNKNOWN, evaluateFact("(sum value: 8 + -9 | value) = 0"));
    assertEquals(UNKNOWN, evaluateFact("(sum value: 1 + 2 | 8 fun/add -9) = 0"));
  }

  @Test
  public void sumQuantificationPropagatesAnUnspecifiedDomain() {
    assertEquals(
        UNKNOWN,
        evaluateFact("(sum value: (1 + 8) & (1 + 8) | value) = 0"));
  }

  @Test
  public void sumQuantificationMakesUnknownDisjointnessUnknown() {
    assertEquals(UNKNOWN, evaluateFact("(sum disj x, y: 8 | 0) = 0"));
  }

  @Test
  public void sumQuantificationPropagatesAnUnspecifiedBody() {
    assertEquals(
        UNKNOWN,
        evaluateFact("(sum value: 1 + 2 | sum ((1 + 8) & (1 + 8))) = 0"));
  }

  private static ThreeVal evaluateFact(String expression) {
    AlloyFactPara fact = (AlloyFactPara) parsePara("fact { " + expression + " }");
    return fact.block.accept(evaluator());
  }

  private static TupleSet aggregate(Aggregation aggregation, AlloyExpr values) {
    AlloyExpr expression =
        switch (aggregation) {
          case INT_CAST -> new AlloyNumIntExpr(values);
          case SUM -> new AlloyNumSumExpr(values);
        };
    return expression.accept(setEvaluator());
  }

  private static void assertAggregations(Atom expected, AlloyExpr values) {
    for (Aggregation aggregation : Aggregation.values()) {
      Atom actual = aggregate(aggregation, values).getScalar();
      assertTrue(
          Atom.structurallyIdentical(expected, actual),
          aggregation + " expected " + expected + " but got " + actual);
    }
  }
}
