package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_DOWN;
import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_UNKNOWN;
import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_UP;
import static ca.uwaterloo.watform.evaluation.ThreeVal.FALSE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.UNKNOWN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class AtomTupleTest {
  private static final Atom ONE = new IntegerAtom(1);
  private static final Atom TWO = new IntegerAtom(2);
  private static final Atom THREE = new IntegerAtom(3);
  private static final Atom LABEL = new GenericLabelAtom("A$0");
  private static final Atom UP = new OverflowAtom(OVERFLOW_UP);
  private static final Atom DOWN = new OverflowAtom(OVERFLOW_DOWN);
  private static final Atom UNKNOWN_DIRECTION = new OverflowAtom(OVERFLOW_UNKNOWN);

  @Test
  public void equalityChecksArityAndEveryComponent() {
    assertEquals(TRUE, AtomTuple.threeEqual(tuple(ONE, LABEL), tuple(ONE, LABEL)));
    assertEquals(FALSE, AtomTuple.threeEqual(tuple(ONE, LABEL), tuple(ONE)));
    assertEquals(FALSE, AtomTuple.threeEqual(tuple(ONE, LABEL), tuple(TWO, LABEL)));
    assertEquals(FALSE, AtomTuple.threeEqual(tuple(ONE, LABEL), tuple(ONE, new StringAtom("A$0"))));
  }

  @Test
  public void uncertainComponentsProduceUnknownUnlessAnotherComponentProvesInequality() {
    assertEquals(UNKNOWN, AtomTuple.threeEqual(tuple(UP, ONE), tuple(UP, ONE)));
    assertEquals(UNKNOWN, AtomTuple.threeEqual(tuple(UNKNOWN_DIRECTION), tuple(ONE)));
    assertEquals(FALSE, AtomTuple.threeEqual(tuple(UP), tuple(DOWN)));

    assertEquals(FALSE, AtomTuple.threeEqual(tuple(UP, ONE), tuple(UP, TWO)));
    assertEquals(FALSE, AtomTuple.threeEqual(tuple(ONE, UP), tuple(TWO, UP)));
  }

  @Test
  public void structuralIdentityChecksRepresentationRatherThanPossibleValue() {
    assertTrue(AtomTuple.structurallyIdentical(tuple(ONE, UP), tuple(ONE, UP)));
    assertFalse(AtomTuple.structurallyIdentical(tuple(ONE, UP), tuple(ONE, DOWN)));
    assertFalse(AtomTuple.structurallyIdentical(tuple(ONE, UP), tuple(ONE)));
    assertFalse(
        AtomTuple.structurallyIdentical(
            tuple(new GenericLabelAtom("A$0")), tuple(new StringAtom("A$0"))));

    assertEquals(UNKNOWN, AtomTuple.threeEqual(tuple(ONE, UP), tuple(ONE, UP)));
  }

  @Test
  public void constructionCopiesTheInputAndReportsTupleProperties() {
    List<Atom> source = new ArrayList<>(List.of(ONE, UP));
    AtomTuple tuple = new AtomTuple(source);
    source.clear();

    assertEquals(2, tuple.arity());
    assertAtom(ONE, tuple.first());
    assertAtom(UP, tuple.last());
    assertAtom(UP, tuple.get(1));
    assertTrue(tuple.containsOverflow());
    assertFalse(tuple(ONE, LABEL).containsOverflow());
    assertThrows(NullPointerException.class, () -> new AtomTuple(null));

    AlloyEvaluatorImplError empty =
        assertThrows(AlloyEvaluatorImplError.class, () -> new AtomTuple(List.of()));
    assertTrue(empty.getMessage().contains("at least one atom"));
  }

  @Test
  public void indexedAccessRejectsBothSidesOfTheValidRange() {
    AtomTuple tuple = tuple(ONE, TWO);

    AlloyEvaluatorImplError below =
        assertThrows(AlloyEvaluatorImplError.class, () -> tuple.get(-1));
    AlloyEvaluatorImplError above =
        assertThrows(AlloyEvaluatorImplError.class, () -> tuple.get(2));

    assertTrue(below.getMessage().contains("index -1"));
    assertTrue(above.getMessage().contains("index 2"));
  }

  @Test
  public void tupleTransformationsPreserveOrderAndAtomKinds() {
    AtomTuple tuple = tuple(ONE, LABEL, UP);

    assertTuple(tuple(ONE, LABEL, UP, TWO), AtomTuple.concat(tuple, tuple(TWO)));
    assertTuple(
        tuple(ONE, LABEL, UP, TWO, THREE),
        AtomTuple.concat(List.of(tuple, tuple(TWO), tuple(THREE))));
    assertTuple(tuple(LABEL, UP), AtomTuple.tail(tuple));
    assertTuple(tuple(ONE, LABEL), AtomTuple.allButLast(tuple));
    assertTuple(tuple(UP, LABEL, ONE), AtomTuple.transpose(tuple));
    assertTuple(tuple(ONE), AtomTuple.tupleOfFirst(tuple));
    assertTuple(tuple(UP), AtomTuple.tupleOfLast(tuple));
    assertThrows(AlloyEvaluatorImplError.class, () -> AtomTuple.concat(List.of()));
  }

  @Test
  public void joinRemovesTheDefinitelyEqualSharedAtoms() {
    assertTuple(
        tuple(ONE, TWO, THREE),
        AtomTuple.join(tuple(ONE, LABEL), tuple(LABEL, TWO, THREE)));
    assertTuple(tuple(TWO), AtomTuple.join(tuple(LABEL), tuple(LABEL, TWO)));
    assertThrows(
        AlloyEvaluatorImplError.class, () -> AtomTuple.join(tuple(LABEL), tuple(LABEL)));
  }

  @Test
  public void joinRejectsDefinitelyDifferentAndUncertainSharedAtoms() {
    AlloyEvaluatorImplError different =
        assertThrows(
            AlloyEvaluatorImplError.class,
            () -> AtomTuple.join(tuple(ONE, UP), tuple(DOWN, TWO)));
    AlloyEvaluatorImplError uncertain =
        assertThrows(
            AlloyEvaluatorImplError.class,
            () -> AtomTuple.join(tuple(ONE, UP), tuple(UP, TWO)));

    assertTrue(different.getMessage().contains("compared as FALSE"));
    assertTrue(uncertain.getMessage().contains("compared as UNKNOWN"));
  }

  private static AtomTuple tuple(Atom... atoms) {
    return new AtomTuple(List.of(atoms));
  }

  private static void assertTuple(AtomTuple expected, AtomTuple actual) {
    assertTrue(
        AtomTuple.structurallyIdentical(expected, actual),
        () -> "Expected " + expected + " but got " + actual);
  }

  private static void assertAtom(Atom expected, Atom actual) {
    assertTrue(
        Atom.structurallyIdentical(expected, actual),
        () -> "Expected " + expected + " but got " + actual);
  }
}
