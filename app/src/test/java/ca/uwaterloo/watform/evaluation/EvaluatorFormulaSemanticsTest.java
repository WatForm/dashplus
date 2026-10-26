package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.FALSE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.UNKNOWN;
import static ca.uwaterloo.watform.parser.AlloyParser.parseExpr;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import org.junit.jupiter.api.Test;

public class EvaluatorFormulaSemanticsTest {
  private static final String TRUE_EXPR = "0 = 0";
  private static final String FALSE_EXPR = "0 = 1";
  private static final String UNKNOWN_EXPR = "8 = 9";
  private static final String THROWS = "(1 fun/div 0) = 0";

  private static FormulaEvaluator evaluator() {
    String xml =
        "<alloy><instance bitwidth=\"4\" maxseq=\"4\"><sig label=\"univ\" ID=\"0\" builtin=\"yes\"/></instance></alloy>";
    var model = new AlloyModel("formula-semantics-test.als");
    model.resolve();
    return new FormulaEvaluator(new EvaluationTable(new Instance(xml), model), false);
  }

  @Test
  public void equalityAndInequalityCompareWholeRelations() {
    assertFormula(TRUE, "(0 + 1) = (1 + 0)");
    assertFormula(FALSE, "(0 + 1) = (0 + 2)");
    assertFormula(TRUE, "(0 -> 1) != (1 -> 0)");
    assertFormula(FALSE, "(0 -> 1) != (0 -> 1)");

    assertFormula(UNKNOWN, "8 = 9");
    assertFormula(UNKNOWN, "8 != 9");
    assertFormula(UNKNOWN, "((0 + 8) & (0 + 8)) = 0");
  }

  @Test
  public void membershipUsesRelationalSubsetSemantics() {
    assertFormula(TRUE, "0 in (0 + 1)");
    assertFormula(TRUE, "(0 + 1) in (0 + 1 + 2)");
    assertFormula(FALSE, "(0 + 2) in (0 + 1)");
    assertFormula(TRUE, "0 not in (1 + 2)");
    assertFormula(FALSE, "0 not in (0 + 1)");

    assertFormula(UNKNOWN, "8 in 9");
    assertFormula(FALSE, "8 in -9");
    assertFormula(TRUE, "none in ((0 + 8) & (0 + 8))");
    assertFormula(FALSE, "none not in ((0 + 8) & (0 + 8))");
  }

  @Test
  public void setMultiplicitiesHandleEmptySingletonAndLargerSets() {
    assertFormula(TRUE, "no none");
    assertFormula(FALSE, "some none");
    assertFormula(FALSE, "one none");
    assertFormula(TRUE, "lone none");

    assertFormula(FALSE, "no 0");
    assertFormula(TRUE, "some 0");
    assertFormula(TRUE, "one 0");
    assertFormula(TRUE, "lone 0");

    assertFormula(FALSE, "no (0 + 1)");
    assertFormula(TRUE, "some (0 + 1)");
    assertFormula(FALSE, "one (0 + 1)");
    assertFormula(FALSE, "lone (0 + 1)");
  }

  @Test
  public void multiplicitiesDoNotGuessOverflowCardinality() {
    assertFormula(FALSE, "no 8");
    assertFormula(TRUE, "some 8");
    assertFormula(UNKNOWN, "one 8");
    assertFormula(UNKNOWN, "lone 8");

    assertFormula(FALSE, "one (0 + 8)");
    assertFormula(FALSE, "lone (0 + 8)");
    assertFormula(UNKNOWN, "one ((0 + 8) & (0 + 8))");
  }

  @Test
  public void logicalOperatorsFollowThreeValuedTruthTablesThroughTheEvaluator() {
    String[] expressions = {TRUE_EXPR, FALSE_EXPR, UNKNOWN_EXPR};
    ThreeVal[] values = {TRUE, FALSE, UNKNOWN};

    for (int left = 0; left < values.length; left++) {
      for (int right = 0; right < values.length; right++) {
        assertFormula(
            values[left].and(values[right]),
            parenthesize(expressions[left]) + " and " + parenthesize(expressions[right]));
        assertFormula(
            values[left].or(values[right]),
            parenthesize(expressions[left]) + " or " + parenthesize(expressions[right]));
        assertFormula(
            values[left].impl(values[right]),
            parenthesize(expressions[left]) + " => " + parenthesize(expressions[right]));
        assertFormula(
            values[left].iff(values[right]),
            parenthesize(expressions[left]) + " <=> " + parenthesize(expressions[right]));
      }
      assertFormula(values[left].not(), "not " + parenthesize(expressions[left]));
    }
  }

  @Test
  public void definitiveLeftOperandsShortCircuitThrowingRightOperands() {
    assertFormula(FALSE, parenthesize(FALSE_EXPR) + " and " + parenthesize(THROWS));
    assertFormula(TRUE, parenthesize(TRUE_EXPR) + " or " + parenthesize(THROWS));
    assertFormula(TRUE, parenthesize(FALSE_EXPR) + " => " + parenthesize(THROWS));
    assertFormula(UNKNOWN, parenthesize(UNKNOWN_EXPR) + " <=> " + parenthesize(THROWS));
  }

  @Test
  public void potentiallyRelevantRightOperandsAreStillEvaluated() {
    assertThrowsFormula(parenthesize(TRUE_EXPR) + " and " + parenthesize(THROWS));
    assertThrowsFormula(parenthesize(FALSE_EXPR) + " or " + parenthesize(THROWS));
    assertThrowsFormula(parenthesize(TRUE_EXPR) + " => " + parenthesize(THROWS));
    assertThrowsFormula(parenthesize(TRUE_EXPR) + " <=> " + parenthesize(THROWS));
    assertThrowsFormula(parenthesize(UNKNOWN_EXPR) + " and " + parenthesize(THROWS));
    assertThrowsFormula(parenthesize(UNKNOWN_EXPR) + " or " + parenthesize(THROWS));
    assertThrowsFormula(parenthesize(UNKNOWN_EXPR) + " => " + parenthesize(THROWS));
  }

  @Test
  public void unknownValuesOnlyDisappearUnderDominatingOperands() {
    assertFormula(FALSE, parenthesize(UNKNOWN_EXPR) + " and " + parenthesize(FALSE_EXPR));
    assertFormula(TRUE, parenthesize(UNKNOWN_EXPR) + " or " + parenthesize(TRUE_EXPR));
    assertFormula(TRUE, parenthesize(UNKNOWN_EXPR) + " => " + parenthesize(TRUE_EXPR));

    assertFormula(UNKNOWN, parenthesize(UNKNOWN_EXPR) + " and " + parenthesize(TRUE_EXPR));
    assertFormula(UNKNOWN, parenthesize(UNKNOWN_EXPR) + " or " + parenthesize(FALSE_EXPR));
    assertFormula(UNKNOWN, parenthesize(UNKNOWN_EXPR) + " => " + parenthesize(FALSE_EXPR));
    assertFormula(UNKNOWN, "not " + parenthesize(UNKNOWN_EXPR));
  }

  private static String parenthesize(String expression) {
    return "(" + expression + ")";
  }

  private static void assertFormula(ThreeVal expected, String source) {
    assertEquals(expected, parseExpr(source).accept(evaluator()), source);
  }

  private static void assertThrowsFormula(String source) {
    assertThrows(AlloyEvaluatorError.class, () -> parseExpr(source).accept(evaluator()), source);
  }
}
