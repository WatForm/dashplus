package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_DOWN;
import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_UP;
import static ca.uwaterloo.watform.parser.AlloyParser.parseExpr;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class EvaluatorRelationalOperationsTest {
  private static SetEvaluator evaluator() {
    String xml =
        "<alloy><instance bitwidth=\"4\" maxseq=\"4\"><sig label=\"univ\" ID=\"0\" builtin=\"yes\"/></instance></alloy>";
    var model = new AlloyModel("relational-operations-test.als");
    model.resolve();
    var table = new EvaluationTable(new Instance(xml), model);
    var formula = new FormulaEvaluator(table, false);
    return new SetEvaluator(table, false, formula);
  }

  @Test
  public void dotJoinSupportsUnaryAndMultiArityOperands() {
    assertSet(tuples(tuple(1), tuple(3)), evaluate("(0 + 2).((0 -> 1) + (2 -> 3))"));
    assertSet(tuples(tuple(0, 2, 3)), evaluate("(0 -> 1).(1 -> 2 -> 3)"));
    assertSet(tuples(tuple(0)), evaluate("(0 -> 1).1"));
  }

  @Test
  public void boxJoinUsesTheSameLeftToRightJoinSemantics() {
    assertSet(tuples(tuple(1)), evaluate("((0 -> 1) + (2 -> 3))[0]"));
    assertSet(tuples(tuple(2)), evaluate("(0 -> 1 -> 2)[0, 1]"));
    assertSet(tuples(tuple(2)), evaluate("((0 -> 1 -> 2)[0])[1]"));
  }

  @Test
  public void arrowBuildsProductsOfArbitraryTupleArity() {
    assertSet(
        tuples(tuple(0, 2), tuple(0, 3), tuple(1, 2), tuple(1, 3)),
        evaluate("(0 + 1) -> (2 + 3)"));
    assertSet(
        tuples(tuple(0, 1, 2, 3)),
        evaluate("(0 -> 1) -> (2 -> 3)"));
  }

  @Test
  public void transposeReversesRelationColumns() {
    assertSet(
        tuples(tuple(1, 0), tuple(3, 2)),
        evaluate("~((0 -> 1) + (2 -> 3))"));
  }

  @Test
  public void domainAndRangeRestrictionFilterOnTheOuterColumns() {
    String relation = "((0 -> 1) + (0 -> 2) + (2 -> 3))";

    assertSet(tuples(tuple(0, 1), tuple(0, 2)), evaluate("0 <: " + relation));
    assertSet(tuples(tuple(0, 2), tuple(2, 3)), evaluate(relation + " :> (2 + 3)"));

    String ternary = "((0 -> 1 -> 2) + (3 -> 1 -> 4))";
    assertSet(tuples(tuple(0, 1, 2)), evaluate("0 <: " + ternary));
    assertSet(tuples(tuple(3, 1, 4)), evaluate(ternary + " :> 4"));
  }

  @Test
  public void overrideReplacesEveryTupleWithAKeyInTheRightDomain() {
    String left = "((0 -> 1) + (0 -> 2) + (1 -> 3))";
    String right = "((0 -> 4) + (2 -> 5))";
    assertSet(
        tuples(tuple(0, 4), tuple(1, 3), tuple(2, 5)),
        evaluate(left + " ++ " + right));

    String ternaryLeft = "((0 -> 1 -> 2) + (0 -> 3 -> 4) + (1 -> 5 -> 6))";
    assertSet(
        tuples(tuple(0, -1, -2), tuple(1, 5, 6)),
        evaluate(ternaryLeft + " ++ (0 -> -1 -> -2)"));
  }

  @Test
  public void closuresReachAFixedPointForChainsBranchesAndCycles() {
    assertSet(
        tuples(tuple(0, 1), tuple(1, 2), tuple(0, 2)),
        evaluate("^((0 -> 1) + (1 -> 2))"));
    assertSet(
        tuples(tuple(0, 1), tuple(0, 2), tuple(1, 3), tuple(2, 3), tuple(0, 3)),
        evaluate("^((0 -> 1) + (0 -> 2) + (1 -> 3) + (2 -> 3))"));
    assertSet(
        tuples(tuple(0, 0), tuple(0, 1), tuple(1, 0), tuple(1, 1)),
        evaluate("^((0 -> 1) + (1 -> 0))"));

    assertSet(
        evaluate("iden + (0 -> 1)"),
        evaluate("*(0 -> 1)"));
  }

  @Test
  public void ambiguousTupleMatchesNeverProduceAConcreteRelation() {
    assertUnspecified("(0 -> 8).(9 -> 1)");
    assertUnspecified("(9 -> 1)[8]");
    assertUnspecified("8 <: (9 -> 1)");
    assertUnspecified("(0 -> 8) :> 9");
    assertUnspecified("(8 -> 1) ++ (9 -> 2)");
    assertUnspecified("^((0 -> 8) + (9 -> 1))");
  }

  @Test
  public void oppositeOverflowDirectionsRemainDefinitivelyDistinct() {
    assertSet(TupleSet.emptySet(), evaluate("(0 -> 8).(-9 -> 1)"));
    assertSet(TupleSet.emptySet(), evaluate("8 <: (-9 -> 1)"));
    assertSet(TupleSet.emptySet(), evaluate("(0 -> 8) :> -9"));
    assertStructuralSet(
        tuples(
            tuple(new OverflowAtom(OVERFLOW_UP), new IntegerAtom(1)),
            tuple(new OverflowAtom(OVERFLOW_DOWN), new IntegerAtom(2))),
        evaluate("(8 -> 1) ++ (-9 -> 2)"));
  }

  @Test
  public void emptyOperandsCanDetermineResultsDespiteAnUnspecifiedOtherOperand() {
    String unspecified = "((0 + 8) & (0 + 8))";

    assertSet(TupleSet.emptySet(), evaluate("none -> " + unspecified));
    assertSet(TupleSet.emptySet(), evaluate("none." + unspecified));
    assertSet(TupleSet.emptySet(), evaluate(unspecified + ".none"));
    assertSet(TupleSet.emptySet(), evaluate("none <: " + unspecified));
    assertSet(TupleSet.emptySet(), evaluate(unspecified + " :> none"));
  }

  @Test
  public void unspecifiedRelationsPropagateWhenTheyCanAffectTheResult() {
    String unspecifiedSet = "((0 + 8) & (0 + 8))";
    String unspecifiedRelation = "(0 -> " + unspecifiedSet + ")";

    assertUnspecified("1 -> " + unspecifiedSet);
    assertUnspecified("~" + unspecifiedRelation);
    assertUnspecified("0." + unspecifiedRelation);
    assertUnspecified("0 <: " + unspecifiedRelation);
    assertUnspecified(unspecifiedRelation + " :> 0");
    assertUnspecified("(0 -> 1) ++ " + unspecifiedRelation);
    assertUnspecified("^" + unspecifiedRelation);
    assertUnspecified("*" + unspecifiedRelation);
  }

  @Test
  public void operationsWithoutTupleMatchingPreserveConcreteOverflowStructure() {
    assertTrue(evaluate("8 -> 9").isConcrete());
    assertTrue(evaluate("~(8 -> 9)").isConcrete());
  }

  private static TupleSet evaluate(String source) {
    return parseExpr(source).accept(evaluator());
  }

  private static void assertUnspecified(String source) {
    assertTrue(evaluate(source).isUnspecified(), source);
  }

  private static void assertSet(TupleSet expected, TupleSet actual) {
    assertEquals(
        TRUE,
        TupleSet.threeEquals(expected, actual),
        "expected " + expected + " but got " + actual);
  }

  private static void assertStructuralSet(TupleSet expected, TupleSet actual) {
    assertTrue(actual.isConcrete());
    assertEquals(expected.size(), actual.size());
    for (AtomTuple expectedTuple : expected) {
      boolean found = false;
      for (AtomTuple actualTuple : actual) {
        found |= AtomTuple.structurallyIdentical(expectedTuple, actualTuple);
      }
      assertTrue(found, "missing " + expectedTuple + " from " + actual);
    }
  }

  private static TupleSet tuples(AtomTuple... tuples) {
    return TupleSet.of(Arrays.asList(tuples));
  }

  private static AtomTuple tuple(int... values) {
    return new AtomTuple(
        Arrays.stream(values).mapToObj(IntegerAtom::new).map(Atom.class::cast).toList());
  }

  private static AtomTuple tuple(Atom... values) {
    return new AtomTuple(List.of(values));
  }
}
