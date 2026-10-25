package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.FALSE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.UNKNOWN;
import static ca.uwaterloo.watform.parser.AlloyParser.parseExpr;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class EvaluatorQuantifierComprehensionTest {
  private record Evaluators(FormulaEvaluator formula, SetEvaluator set) {}

  private static Evaluators evaluators() {
    String xml =
        "<alloy><instance bitwidth=\"4\" maxseq=\"4\"><sig label=\"univ\" ID=\"0\" builtin=\"yes\"/></instance></alloy>";
    var model = new AlloyModel("quantifier-comprehension-test.als");
    model.resolve();
    var table = new EvaluationTable(new Instance(xml), model);
    var formula = new FormulaEvaluator(table, false);
    return new Evaluators(formula, new SetEvaluator(table, false, formula));
  }

  @Test
  public void everyBooleanQuantifierCountsItsBindings() {
    assertFormula(TRUE, "all x: 0 + 1 | x in 0 + 1");
    assertFormula(FALSE, "all x: 0 + 1 | x = 0");

    assertFormula(TRUE, "some x: 0 + 1 | x = 1");
    assertFormula(FALSE, "some x: 0 + 1 | x = 2");

    assertFormula(TRUE, "no x: 0 + 1 | x = 2");
    assertFormula(FALSE, "no x: 0 + 1 | x = 1");

    assertFormula(TRUE, "one x: 0 + 1 | x = 1");
    assertFormula(FALSE, "one x: 0 + 1 | x in 0 + 1");

    assertFormula(TRUE, "lone x: 0 + 1 | x = 1");
    assertFormula(FALSE, "lone x: 0 + 1 | x in 0 + 1");
  }

  @Test
  public void emptyDomainsUseTheQuantifiersVacuousCases() {
    assertFormula(TRUE, "all x: none | x = x");
    assertFormula(TRUE, "no x: none | x = x");
    assertFormula(TRUE, "lone x: none | x = x");
    assertFormula(FALSE, "some x: none | x = x");
    assertFormula(FALSE, "one x: none | x = x");
  }

  @Test
  public void unusedVariablesPreserveConcreteDomainCardinality() {
    assertFormula(TRUE, "all x: 0 + 1 | 0 = 0");
    assertFormula(TRUE, "some x: 0 + 1 | 0 = 0");
    assertFormula(FALSE, "no x: 0 + 1 | 0 = 0");
    assertFormula(FALSE, "one x: 0 + 1 | 0 = 0");
    assertFormula(FALSE, "lone x: 0 + 1 | 0 = 0");

    assertFormula(TRUE, "all x: none | 0 = 1");
    assertFormula(FALSE, "some x: none | 0 = 0");
    assertFormula(TRUE, "no x: none | 0 = 0");
    assertFormula(FALSE, "one x: none | 0 = 0");
    assertFormula(TRUE, "lone x: none | 0 = 0");
  }

  @Test
  public void unusedEmptyDomainsDoNotEvaluateUnreachableBodies() {
    String throwsIfEvaluated = "(1 fun/div 0) = 0";

    assertFormula(TRUE, "all x: none | " + throwsIfEvaluated);
    assertFormula(FALSE, "some x: none | " + throwsIfEvaluated);
    assertFormula(TRUE, "no x: none | " + throwsIfEvaluated);
    assertFormula(FALSE, "one x: none | " + throwsIfEvaluated);
    assertFormula(TRUE, "lone x: none | " + throwsIfEvaluated);
    assertFormula(TRUE, "(sum x: none | 1 fun/div 0) = 0");
  }

  @Test
  public void unusedVariablesCanMakeUnspecifiedDomainsIrrelevant() {
    String unspecified = "(0 + 8) & (0 + 8)";

    assertFormula(TRUE, "all x: " + unspecified + " | 0 = 0");
    assertFormula(FALSE, "some x: " + unspecified + " | 0 = 1");
    assertFormula(TRUE, "no x: " + unspecified + " | 0 = 1");
    assertFormula(FALSE, "one x: " + unspecified + " | 0 = 1");
    assertFormula(TRUE, "lone x: " + unspecified + " | 0 = 1");

    // These would have concrete results for every nonempty domain, but the vacuous result for an
    // empty domain is different. Skipping the unused variable must therefore retain UNKNOWN.
    assertFormula(UNKNOWN, "all x: " + unspecified + " | 0 = 1");
    assertFormula(UNKNOWN, "some x: " + unspecified + " | 0 = 0");
    assertFormula(UNKNOWN, "no x: " + unspecified + " | 0 = 0");
    assertFormula(UNKNOWN, "one x: " + unspecified + " | 0 = 0");
    assertFormula(UNKNOWN, "lone x: " + unspecified + " | 0 = 0");
  }

  @Test
  public void unspecifiedDomainsIncludeThePossibilityOfNoBindings() {
    String unspecified = "(0 + 8) & (0 + 8)";

    // The same issue applies when an entire disjoint declaration block is unused: there may be
    // no valid assignment for the block.
    assertFormula(UNKNOWN, "all disj x, y: " + unspecified + " | 0 = 1");
    assertFormula(UNKNOWN, "some disj x, y: " + unspecified + " | 0 = 0");
    assertFormula(UNKNOWN, "no disj x, y: " + unspecified + " | 0 = 0");
    assertFormula(UNKNOWN, "lone disj x, y: " + unspecified + " | 0 = 0");
  }

  @Test
  public void declarationBlocksEnumerateAllNamesAndAllBlocks() {
    assertFormula(TRUE, "one x, y: 0 + 1 | x = 0 and y = 1");
    assertFormula(TRUE, "one x: 0 + 1, y: 0 + 1 | x = 0 and y = 1");
    assertFormula(FALSE, "one x, y: 0 + 1 | x != y");
    assertFormula(FALSE, "one x: 0 + 1, y: 0 + 1 | x != y");
  }

  @Test
  public void laterDeclarationDomainsCanUseEarlierBindings() {
    assertFormula(TRUE, "all x: 0 + 1, y: x | y = x");
    assertFormula(TRUE, "one x: 0 + 1, y: x | y = 1");
    assertSet(
        tuples(tuple(0, 0), tuple(1, 1)),
        evaluateSet("{x: 0 + 1, y: x | y = x}"));
  }

  @Test
  public void nestedQuantifiersPreserveOuterBindingsWhenNamesAreShadowed() {
    assertFormula(
        TRUE,
        "all x: 0 + 1 | (some x: 0 + 1 | x = 1) and x in 0 + 1");
    assertFormula(
        TRUE,
        "all x: 0 + 1 | (all y: x | y = x) and x in 0 + 1");
  }

  @Test
  public void usageAnalysisRespectsShadowingAndDeclarationDomains() {
    String unspecified = "(0 + 8) & (0 + 8)";

    assertFormula(
        TRUE,
        "all x: " + unspecified + " | some x: 0 | x = 0");
    assertFormula(
        UNKNOWN,
        "some x: " + unspecified + " | some x: x | 0 = 0");
    assertFormula(
        UNKNOWN,
        "some x: " + unspecified + ", y: x | 0 = 0");
    assertFormula(
        TRUE,
        "all x: " + unspecified + " | let x = 0, y = x | y = 0");
    assertFormula(
        UNKNOWN,
        "some x: " + unspecified + " | let y = x | y = y");
  }

  @Test
  public void quantifierBindingsAreRemovedWhenBodyEvaluationThrows() {
    Evaluators evaluators = evaluators();

    assertThrows(
        AlloyEvaluatorError.class,
        () ->
            parseExpr("some x: 0 + 1 | (1 fun/div 0) = 0")
                .accept(evaluators.formula()));
    assertThrows(
        AlloyEvaluatorImplError.class,
        () -> parseExpr("x").accept(evaluators.set()));
  }

  @Test
  public void disjFiltersOnlyNamesInItsOwnDeclarationBlock() {
    assertFormula(TRUE, "all disj x, y: 0 + 1 | x != y");
    assertFormula(TRUE, "no disj x, y: 0 + 1 | x = y");
    assertFormula(TRUE, "one disj x, y: 0 + 1 | x = 0 and y = 1");

    assertFormula(FALSE, "all x, y: 0 + 1 | x != y");
    assertFormula(FALSE, "no x: 0 + 1, y: 0 + 1 | x = y");
  }

  @Test
  public void partiallyUsedDisjointBlocksStillBindEveryName() {
    assertFormula(TRUE, "one disj x, y: 0 + 1 | x = 0");
    assertFormula(FALSE, "one disj x, y: 0 + 1 | 0 = 0");
    assertFormula(FALSE, "lone disj x, y: 0 + 1 | 0 = 0");
  }

  @Test
  public void unknownBodiesPropagateThroughEveryBooleanQuantifier() {
    assertFormula(UNKNOWN, "all x: 8 | x = x");
    assertFormula(UNKNOWN, "some x: 8 | x = x");
    assertFormula(UNKNOWN, "no x: 8 | x = x");
    assertFormula(UNKNOWN, "one x: 8 | x = x");
    assertFormula(UNKNOWN, "lone x: 8 | x = x");
  }

  @Test
  public void knownQuantifierResultsDominateOtherUnknownBindings() {
    assertFormula(TRUE, "some x: 0 + 8 | x = x");
    assertFormula(FALSE, "all x: 0 + 8 | x != 0");
    assertFormula(FALSE, "no x: 0 + 8 | x = x");
    assertFormula(FALSE, "one x: 0 + 1 + 8 | x = x");
    assertFormula(FALSE, "lone x: 0 + 1 + 8 | x = x");
    assertFormula(UNKNOWN, "one x: 0 + 8 | x = x");
    assertFormula(UNKNOWN, "lone x: 0 + 8 | x = x");

    AlloyExpr domain = parseExpr("(0 + 8) & (0 + 8)");
    assertTrue(domain.accept(evaluators().set()).isUnspecified());
    assertFormula(UNKNOWN, "some x: (0 + 8) & (0 + 8) | x = 0");
  }

  @Test
  public void unknownDisjointnessPropagatesUnlessAResultIsAlreadyCertain() {
    assertFormula(UNKNOWN, "some disj x, y: 8 | 0 = 0");
    assertFormula(UNKNOWN, "one disj x, y: 8 | 0 = 0");
    assertFormula(UNKNOWN, "lone disj x, y: 8 | 0 = 0");

    assertFormula(TRUE, "some disj x, y: 0 + 1 + 8 | x = 0 and y = 1");
    assertFormula(FALSE, "all disj x, y: 0 + 1 + 8 | not (x = 0 and y = 1)");
  }

  @Test
  public void comprehensionsBuildUnaryAndMultiAritySets() {
    assertSet(tuples(tuple(1)), evaluateSet("{x: 0 + 1 | x = 1}"));
    assertSet(
        tuples(tuple(0, 1), tuple(1, 0)),
        evaluateSet("{x, y: 0 + 1 | x != y}"));
    assertSet(
        tuples(tuple(0, 1), tuple(1, 0)),
        evaluateSet("{x: 0 + 1, y: 0 + 1 | x != y}"));
    assertSet(
        tuples(tuple(0, 1), tuple(1, 0)),
        evaluateSet("{disj x, y: 0 + 1 | 0 = 0}"));
    assertSet(tuples(tuple(0), tuple(1)), evaluateSet("{x: 0 + 1}"));
    assertSet(TupleSet.emptySet(), evaluateSet("{x: none | x = x}"));
  }

  @Test
  public void tupleValuedDomainsPreserveTupleArity() {
    String pairs = "(0 -> 1) + (1 -> 0)";
    assertFormula(TRUE, "all pair: " + pairs + " | pair in " + pairs);
    assertSet(tuples(tuple(0, 1), tuple(1, 0)), evaluateSet("{pair: " + pairs + "}"));
  }

  @Test
  public void uncertainComprehensionMembershipMakesTheWholeSetUnspecified() {
    assertTrue(evaluateSet("{x: 0 + 8 | x = x}").isUnspecified());
    assertTrue(evaluateSet("{disj x, y: 8 | 0 = 0}").isUnspecified());
    assertTrue(
        evaluateSet("{x: (0 + 8) & (0 + 8) | x = 0}").isUnspecified());
    assertTrue(
        evaluateSet("{x: (0 + 8) & (0 + 8) | 0 = 0}").isUnspecified());
  }

  @Test
  public void sumSupportsDependentBlocksAndDisjointMultiDeclarationEnumeration() {
    assertFormula(TRUE, "(sum x: 1 + 2, y: x | y) = 3");
    assertFormula(
        TRUE,
        "(sum disj x, y: 0 + 1, z: 0 + 1 | x fun/add y fun/add z) = 6");
  }

  @Test
  public void unusedSumVariablesPreserveMultiplicityAndCanIgnoreUnknownDomainsForZero() {
    String unspecified = "(0 + 8) & (0 + 8)";

    assertFormula(TRUE, "(sum x: 0 + 1 | 1) = 2");
    assertFormula(TRUE, "(sum disj x, y: 0 + 1 | 1) = 2");
    assertFormula(TRUE, "(sum x: " + unspecified + " | 0) = 0");
    assertFormula(UNKNOWN, "(sum x: " + unspecified + " | 1) = 0");
  }

  @Test
  public void unsupportedDeclarationFormsFailExplicitly() {
    assertThrows(
        AlloyEvaluatorError.class,
        () -> parseExpr("all x: set (0 + 1) | x = x").accept(evaluators().formula()));
    assertThrows(
        AlloyEvaluatorError.class,
        () -> parseExpr("{x: disj (0 + 1) | x = x}").accept(evaluators().set()));
  }

  private static void assertFormula(ThreeVal expected, String source) {
    assertEquals(expected, parseExpr(source).accept(evaluators().formula()), source);
  }

  private static TupleSet evaluateSet(String source) {
    return parseExpr(source).accept(evaluators().set());
  }

  private static TupleSet tuples(AtomTuple... tuples) {
    return TupleSet.of(Arrays.asList(tuples));
  }

  private static AtomTuple tuple(int... values) {
    return new AtomTuple(
        Arrays.stream(values).mapToObj(IntegerAtom::new).map(Atom.class::cast).toList());
  }

  private static void assertSet(TupleSet expected, TupleSet actual) {
    assertEquals(
        TRUE,
        TupleSet.threeEquals(expected, actual),
        "expected " + expected + " but got " + actual);
  }
}
