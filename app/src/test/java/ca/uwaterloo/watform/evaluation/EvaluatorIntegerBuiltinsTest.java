package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyDotExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyEqualsExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyFunMaxExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyFunMinExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyFunNextExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNoneExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNumExpr;
import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import org.junit.jupiter.api.Test;

public class EvaluatorIntegerBuiltinsTest {
  private static FormulaEvaluator evaluator() {
    String xml =
        "<alloy><instance bitwidth=\"4\" maxseq=\"4\"><sig label=\"univ\" ID=\"0\" builtin=\"yes\"/></instance></alloy>";
    var model = new AlloyModel("integer-builtins-test.als");
    model.resolve();
    return new FormulaEvaluator(new EvaluationTable(new Instance(xml), model), false);
  }

  @Test
  public void minAndMaxComeFromTheInstanceBitwidth() {
    FormulaEvaluator evaluator = evaluator();

    assertEquals(
        TRUE,
        new AlloyEqualsExpr(new AlloyFunMinExpr(), new AlloyNumExpr(-8)).accept(evaluator));
    assertEquals(
        TRUE, new AlloyEqualsExpr(new AlloyFunMaxExpr(), new AlloyNumExpr(7)).accept(evaluator));
  }

  @Test
  public void nextIsTheNonWrappingIntegerSuccessorRelation() {
    FormulaEvaluator evaluator = evaluator();
    AlloyExpr successorOfMin =
        new AlloyDotExpr(new AlloyNumExpr(-8), new AlloyFunNextExpr());
    AlloyExpr successorOfMax = new AlloyDotExpr(new AlloyNumExpr(7), new AlloyFunNextExpr());

    assertEquals(
        TRUE, new AlloyEqualsExpr(successorOfMin, new AlloyNumExpr(-7)).accept(evaluator));
    assertEquals(TRUE, new AlloyEqualsExpr(successorOfMax, new AlloyNoneExpr()).accept(evaluator));
  }
}
