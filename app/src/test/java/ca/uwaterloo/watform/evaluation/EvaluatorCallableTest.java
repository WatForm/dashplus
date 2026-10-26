package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.FALSE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.UNKNOWN;
import static ca.uwaterloo.watform.parser.AlloyParser.parsePara;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uwaterloo.watform.alloyast.AlloyStrings.Kind;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyFunPara;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPredPara;
import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.alloymodel.Qname;
import java.util.List;
import org.junit.jupiter.api.Test;

public class EvaluatorCallableTest {
  private static final List<String> CALLABLES =
      List.of(
          "fun constantZero[]: Int { 0 }",
          "fun identity[value: Int]: Int { value }",
          "fun chooseFirst[left, right: Int]: Int { left }",
          "fun chooseSecond[left, right: Int]: Int { right }",
          "fun fail[value: Int]: Int { 1 fun/div 0 }",
          "pred alwaysTrue[] { 0 = 0 }",
          "pred same[first, second: Int] { first = second }",
          "pred ordered[left, right: Int] { left = 1 right = 2 }",
          "pred both[value: Int] { value = 1 value in (1 + 2) }",
          "pred unknownThenFalse[value: Int] { 8 = 9 value = 2 }",
          "pred falseThenThrow[value: Int] { value = 2 (1 fun/div 0) = 0 }",
          "pred ignores[value: Int] { 0 = 0 }",
          "pred failPred[value: Int] { (1 fun/div 0) = 0 }");

  private record Context(
      AlloyModel model,
      EvaluationTable table,
      FormulaEvaluator formula) {}

  @Test
  public void zeroOneAndMultipleArgumentFunctionsEvaluate() {
    assertFormula(TRUE, "constantZero = 0");
    assertFormula(TRUE, "identity[2] = 2");
    assertFormula(TRUE, "chooseFirst[1, 2] = 1");
    assertFormula(TRUE, "chooseSecond[1, 2] = 2");
  }

  @Test
  public void dotAndNestedBoxCallsPreserveDeclarationOrder() {
    assertFormula(TRUE, "2.identity = 2");
    assertFormula(TRUE, "1.chooseFirst[2] = 1");
    assertFormula(TRUE, "chooseFirst[1][2] = 1");
    assertFormula(TRUE, "chooseSecond[1][2] = 2");
    assertFormula(TRUE, "2.(1.chooseFirst) = 1");
  }

  @Test
  public void resolvedFunctionReferencesRemainPartialAcrossNestedJoins() {
    assertFormula(TRUE, "chooseFirst[1][2] = 1");
    assertFormula(TRUE, "2.(1.chooseFirst) = 1");
  }

  @Test
  public void predicatesAreEvaluatedOnlyInFormulaContext() {
    assertFormula(TRUE, "alwaysTrue");
    assertFormula(TRUE, "same[1, 1]");
    assertFormula(FALSE, "same[1, 2]");
    assertFormula(TRUE, "1.same[1]");
    assertFormula(TRUE, "same[1][1]");
    assertFormula(TRUE, "ordered[1, 2]");
    assertFormula(FALSE, "ordered[2, 1]");
    assertFormula(TRUE, "1.ordered[2]");
    assertFormula(TRUE, "ordered[1][2]");

  }

  @Test
  public void predicateBlockStatementsAreAndedInOrder() {
    assertFormula(TRUE, "both[1]");
    assertFormula(FALSE, "both[2]");
    assertFormula(FALSE, "unknownThenFalse[1]");
    assertFormula(FALSE, "falseThenThrow[1]");
  }

  @Test
  public void unspecifiedArgumentsPropagateOnlyWhenTheBodyUsesThem() {
    String unspecified = "((0 + 8) & (0 + 8))";

    assertFormula(UNKNOWN, "identity[" + unspecified + "] = 0");
    assertFormula(TRUE, "chooseSecond[" + unspecified + ", 1] = 1");
    assertFormula(UNKNOWN, "same[" + unspecified + ", 1]");
    assertFormula(TRUE, "ignores[" + unspecified + "]");
  }

  @Test
  public void successfulAndFailingFunctionsAlwaysRemoveArgumentFrames() {
    Context successful = context("pred verifyCall[] { identity[1] = 1 }");
    Qname identityArgument =
        qnameOf(function(successful.model, "identity").arguments.getFirst().qnames.getFirst());
    evaluateCheck(successful);
    assertTrue(successful.table.get(identityArgument).isEmpty());

    Context failing = context("pred verifyCall[] { fail[1] = 0 }");
    Qname failArgument =
        qnameOf(function(failing.model, "fail").arguments.getFirst().qnames.getFirst());
    assertThrows(AlloyEvaluatorError.class, () -> evaluateCheck(failing));
    assertTrue(failing.table.get(failArgument).isEmpty());
  }

  @Test
  public void failingPredicatesAlwaysRemoveArgumentFrames() {
    Context context = context("pred verifyCall[] { failPred[1] }");
    AlloyPredPara fail = predicate(context.model, "failPred");
    Qname argument = qnameOf(fail.arguments.getFirst().qnames.getFirst());

    assertThrows(AlloyEvaluatorError.class, () -> evaluateCheck(context));
    assertTrue(context.table.get(argument).isEmpty());
  }

  private static void assertFormula(ThreeVal expected, String expression) {
    Context context = context("pred verifyCall[] { " + expression + " }");
    assertEquals(expected, evaluateCheck(context), expression);
  }

  private static ThreeVal evaluateCheck(Context context) {
    AlloyBlock body =
        (AlloyBlock) context.model.predFunBody(Qname.thisQname("verifyCall"));
    return body.accept(context.formula);
  }

  private static Context context(String... additionalCallables) {
    var model = new AlloyModel("callable-test.als");
    for (String source : CALLABLES) addCallable(model, source);
    for (String source : additionalCallables) addCallable(model, source);
    model.resolve();

    String xml =
        "<alloy><instance bitwidth=\"4\" maxseq=\"4\"><sig label=\"univ\" ID=\"0\" builtin=\"yes\"/></instance></alloy>";
    var table = new EvaluationTable(new Instance(xml), model);
    var formula = new FormulaEvaluator(table, false);
    return new Context(model, table, formula);
  }

  private static void addCallable(AlloyModel model, String source) {
    var paragraph = parsePara(source);
    if (paragraph instanceof AlloyFunPara function) model.addPara(function);
    else if (paragraph instanceof AlloyPredPara predicate) model.addPara(predicate);
    else throw new AssertionError("Expected a function or predicate: " + source);
  }

  private static AlloyFunPara function(AlloyModel model, String name) {
    return model.allFunParas().stream()
        .filter(function -> function.qname.getName().equals(name))
        .findFirst()
        .orElseThrow();
  }

  private static AlloyPredPara predicate(AlloyModel model, String name) {
    return model.allPredParas().stream()
        .filter(predicate -> predicate.qname.getName().equals(name))
        .findFirst()
        .orElseThrow();
  }

  private static Qname qnameOf(AlloyQnameExpr expression) {
    return expression.kind == Kind.UNKNOWN_KIND
        ? Qname.unknownQname(expression.getName())
        : Qname.alloyQnameExprToQname(expression);
  }
}
