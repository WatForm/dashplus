package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.FALSE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.UNKNOWN;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyEqualsExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyIteExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNumExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyStepsExpr;
import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import org.junit.jupiter.api.Test;

public class EvaluatorIteTest {
  private static FormulaEvaluator evaluator() {
    String xml =
        "<alloy><instance bitwidth=\"4\" maxseq=\"4\"><sig label=\"univ\" ID=\"0\" builtin=\"yes\"/></instance></alloy>";
    var model = new AlloyModel("ite-test.als");
    model.resolve();
    return new FormulaEvaluator(new EvaluationTable(new Instance(xml), model), false);
  }

  private static AlloyEqualsExpr equals(int left, int right) {
    return new AlloyEqualsExpr(new AlloyNumExpr(left), new AlloyNumExpr(right));
  }

  @Test
  public void formulaConditionalSelectsOnlyTheRequiredBranch() {
    FormulaEvaluator evaluator = evaluator();
    AlloyExpr trueConditional = new AlloyIteExpr(equals(1, 1), equals(2, 2), new AlloyStepsExpr());
    AlloyExpr falseConditional =
        new AlloyIteExpr(equals(1, 2), new AlloyStepsExpr(), equals(2, 3));

    assertEquals(TRUE, trueConditional.accept(evaluator));
    assertEquals(FALSE, falseConditional.accept(evaluator));
  }

  @Test
  public void setConditionalCanAppearInsideAFormula() {
    FormulaEvaluator evaluator = evaluator();
    AlloyExpr selectedSet =
        new AlloyIteExpr(equals(1, 1), new AlloyNumExpr(2), new AlloyStepsExpr());
    AlloyExpr comparison = new AlloyEqualsExpr(selectedSet, new AlloyNumExpr(2));

    assertEquals(TRUE, comparison.accept(evaluator));
  }

  @Test
  public void unknownConditionProducesUnknownFormulaAndUnspecifiedSet() {
    FormulaEvaluator evaluator = evaluator();
    AlloyExpr unknownCondition = equals(8, 8);
    AlloyExpr formulaConditional =
        new AlloyIteExpr(unknownCondition, equals(1, 1), equals(1, 2));
    AlloyExpr setConditional =
        new AlloyIteExpr(unknownCondition, new AlloyNumExpr(1), new AlloyNumExpr(2));

    assertEquals(UNKNOWN, formulaConditional.accept(evaluator));
    assertEquals(UNKNOWN, new AlloyEqualsExpr(setConditional, new AlloyNumExpr(1)).accept(evaluator));
  }
}
