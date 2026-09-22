package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.FALSE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyArrowExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyUnionExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBracketExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNoneExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNumExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyPredTotOrdExpr;
import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import java.util.List;
import org.junit.jupiter.api.Test;

public class EvaluatorTotalOrderTest {
  private static FormulaEvaluator evaluator() {
    String xml =
        "<alloy><instance bitwidth=\"4\" maxseq=\"4\"><sig label=\"univ\" ID=\"0\" builtin=\"yes\"/></instance></alloy>";
    var model = new AlloyModel("total-order-test.als");
    model.resolve();
    return new FormulaEvaluator(new EvaluationTable(new Instance(xml), model), false);
  }

  private static AlloyExpr union(AlloyExpr... expressions) {
    AlloyExpr result = expressions[0];
    for (int i = 1; i < expressions.length; i++) {
      result = new AlloyUnionExpr(result, expressions[i]);
    }
    return result;
  }

  private static AlloyExpr edge(int from, int to) {
    return new AlloyArrowExpr(new AlloyNumExpr(from), new AlloyNumExpr(to));
  }

  private static AlloyExpr totalOrder(AlloyExpr ordered, AlloyExpr first, AlloyExpr next) {
    return new AlloyBracketExpr(new AlloyPredTotOrdExpr(), List.of(ordered, first, next));
  }

  @Test
  public void acceptsAChainContainingEveryElementExactlyOnce() {
    AlloyExpr ordered = union(new AlloyNumExpr(0), new AlloyNumExpr(1), new AlloyNumExpr(2));
    AlloyExpr next = union(edge(0, 1), edge(1, 2));

    assertEquals(TRUE, totalOrder(ordered, new AlloyNumExpr(0), next).accept(evaluator()));
  }

  @Test
  public void acceptsTheSingletonOrder() {
    assertEquals(
        TRUE,
        totalOrder(new AlloyNumExpr(0), new AlloyNumExpr(0), new AlloyNoneExpr())
            .accept(evaluator()));
  }

  @Test
  public void rejectsDisconnectedBranchingAndCyclicRelations() {
    AlloyExpr ordered = union(new AlloyNumExpr(0), new AlloyNumExpr(1), new AlloyNumExpr(2));

    assertEquals(
        FALSE,
        totalOrder(ordered, new AlloyNumExpr(0), edge(0, 1)).accept(evaluator()));
    assertEquals(
        FALSE,
        totalOrder(ordered, new AlloyNumExpr(0), union(edge(0, 1), edge(0, 2)))
            .accept(evaluator()));
    assertEquals(
        FALSE,
        totalOrder(ordered, new AlloyNumExpr(0), union(edge(0, 1), edge(1, 2), edge(2, 0)))
            .accept(evaluator()));
  }

  @Test
  public void rejectsEdgesOutsideTheOrderedSet() {
    AlloyExpr ordered = union(new AlloyNumExpr(0), new AlloyNumExpr(1));
    AlloyExpr next = union(edge(0, 1), edge(2, 3));

    assertEquals(FALSE, totalOrder(ordered, new AlloyNumExpr(0), next).accept(evaluator()));
  }
}
