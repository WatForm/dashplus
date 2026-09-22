package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_UP;
import static ca.uwaterloo.watform.evaluation.ThreeVal.UNKNOWN;
import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uwaterloo.watform.alloymodel.Qname;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

public class TupleSetTest {
  private static final AtomTuple ONE = new AtomTuple(List.of(new IntegerAtom(1)));
  private static final AtomTuple TWO = new AtomTuple(List.of(new IntegerAtom(2)));
  private static final AtomTuple OVERFLOW = new AtomTuple(List.of(new OverflowAtom(OVERFLOW_UP)));

  @Test
  public void uncertainIntersectionAndDifferenceAreUnspecified() {
    TupleSet set = TupleSet.of(List.of(ONE, OVERFLOW));

    TupleSet intersection = TupleSet.intersect(set, set);
    TupleSet difference = TupleSet.diff(set, set);

    assertTrue(intersection.isUnspecified());
    assertTrue(difference.isUnspecified());
    assertEquals(UNKNOWN, TupleSet.threeEquals(intersection, TupleSet.of(List.of(ONE))));
    assertEquals(UNKNOWN, TupleSet.threeEquals(difference, TupleSet.emptySet()));
  }

  @Test
  public void concreteSubtractionRemainsConcrete() {
    TupleSet one = TupleSet.of(List.of(ONE));

    TupleSet intersection = TupleSet.intersect(one, one);
    TupleSet difference = TupleSet.diff(one, TupleSet.emptySet());

    assertFalse(intersection.isUnspecified());
    assertFalse(difference.isUnspecified());
    assertEquals(1, intersection.size());
    assertEquals(1, difference.size());
  }

  @Test
  public void unspecifiedValuesPropagate() {
    TupleSet one = TupleSet.of(List.of(ONE));
    TupleSet unspecified = TupleSet.unspecified();

    assertTrue(TupleSet.union(unspecified, one).isUnspecified());
    assertTrue(TupleSet.crossProduct(one, unspecified).isUnspecified());
    assertTrue(TupleSet.join(unspecified, one).isUnspecified());
    assertEquals(UNKNOWN, TupleSet.threeSubset(one, unspecified));
  }

  @Test
  public void joinsFillAndThenEvaluatePartialFunctionCalls() {
    AtomicInteger evaluations = new AtomicInteger();
    TupleSet partial =
        TupleSet.partialFunction(
            Qname.nameSpaceQname("this", "f"),
            2,
            arguments -> {
              evaluations.incrementAndGet();
              return TupleSet.union(arguments.getFirst(), arguments.getLast());
            });

    TupleSet oneArgument = TupleSet.join(TupleSet.of(List.of(ONE)), partial);
    assertTrue(oneArgument.isPartialFunction());
    assertEquals(0, evaluations.get());

    TupleSet completed = TupleSet.join(TupleSet.of(List.of(TWO)), oneArgument);
    assertTrue(completed.isConcrete());
    assertEquals(2, completed.size());
    assertEquals(1, evaluations.get());
  }

  @Test
  public void unspecifiedArgumentsDoNotCompleteCallsEarly() {
    AtomicInteger evaluations = new AtomicInteger();
    TupleSet partial =
        TupleSet.partialFunction(
            Qname.nameSpaceQname("this", "f"),
            2,
            arguments -> {
              evaluations.incrementAndGet();
              return TupleSet.union(arguments.getFirst(), arguments.getLast());
            });

    TupleSet oneArgument = TupleSet.join(TupleSet.unspecified(), partial);
    assertTrue(oneArgument.isPartialFunction());
    assertEquals(0, evaluations.get());

    TupleSet completed = TupleSet.join(TupleSet.of(List.of(TWO)), oneArgument);
    assertTrue(completed.isUnspecified());
    assertEquals(1, evaluations.get());
  }

  @Test
  public void partialFunctionCallsRejectNonJoinOperations() {
    TupleSet partial =
        TupleSet.partialFunction(
            Qname.nameSpaceQname("this", "f"), 1, arguments -> arguments.getFirst());
    TupleSet one = TupleSet.of(List.of(ONE));

    AlloyEvaluatorImplError misuse =
        assertThrows(AlloyEvaluatorImplError.class, () -> TupleSet.union(partial, one));
    assertTrue(misuse.getMessage().contains("incomplete function call"));
    assertThrows(AlloyEvaluatorImplError.class, () -> TupleSet.intersect(one, partial));
    assertThrows(AlloyEvaluatorImplError.class, () -> TupleSet.threeEquals(partial, one));
    assertThrows(AlloyEvaluatorImplError.class, () -> TupleSet.join(partial, one));
    assertThrows(AlloyEvaluatorImplError.class, partial::size);
  }

  @Test
  public void joinsFillPredicatesButFormulaEvaluationIsExplicit() {
    AtomicInteger evaluations = new AtomicInteger();
    TupleSet predicate =
        TupleSet.predicateCall(
            Qname.nameSpaceQname("this", "p"),
            2,
            arguments -> {
              evaluations.incrementAndGet();
              return TupleSet.threeEquals(arguments.getFirst(), arguments.getLast());
            });

    TupleSet oneArgument = TupleSet.join(TupleSet.of(List.of(ONE)), predicate);
    assertTrue(oneArgument.isPredicateCall());
    assertEquals(0, evaluations.get());
    assertThrows(AlloyEvaluatorImplError.class, oneArgument::evaluatePredicate);

    TupleSet completed = TupleSet.join(TupleSet.of(List.of(ONE)), oneArgument);
    assertTrue(completed.isPredicateCall());
    assertEquals(0, evaluations.get());
    assertEquals(TRUE, completed.evaluatePredicate());
    assertEquals(1, evaluations.get());
  }

  @Test
  public void predicateCallsRejectRelationalOperations() {
    TupleSet predicate =
        TupleSet.predicateCall(
            Qname.nameSpaceQname("this", "p"), 0, arguments -> TRUE);
    TupleSet one = TupleSet.of(List.of(ONE));

    assertThrows(AlloyEvaluatorImplError.class, () -> TupleSet.union(predicate, one));
    assertThrows(AlloyEvaluatorImplError.class, () -> TupleSet.join(predicate, one));
    assertThrows(AlloyEvaluatorImplError.class, predicate::size);
  }

  @Test
  public void concreteTupleErrorsReportTheFailedInvariant() {
    AlloyEvaluatorImplError scalarError =
        assertThrows(
            AlloyEvaluatorImplError.class, () -> TupleSet.of(List.of(ONE, TWO)).getScalar());
    assertTrue(scalarError.getMessage().contains("exactly one unary tuple"));
    assertTrue(scalarError.getMessage().contains("2 tuple(s)"));

    AlloyEvaluatorImplError indexError =
        assertThrows(AlloyEvaluatorImplError.class, () -> ONE.get(1));
    assertTrue(indexError.getMessage().contains("index 1"));
    assertTrue(indexError.getMessage().contains("[0, 1)"));
  }
}
