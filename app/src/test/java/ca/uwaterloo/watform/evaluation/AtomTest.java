package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_DOWN;
import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_UNKNOWN;
import static ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection.OVERFLOW_UP;
import static ca.uwaterloo.watform.evaluation.ThreeVal.FALSE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.UNKNOWN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

public class AtomTest {
  private static final Atom LOW = new IntegerAtom(-1);
  private static final Atom HIGH = new IntegerAtom(1);
  private static final Atom DOWN = new OverflowAtom(OVERFLOW_DOWN);
  private static final Atom UP = new OverflowAtom(OVERFLOW_UP);
  private static final Atom UNKNOWN_DIRECTION = new OverflowAtom(OVERFLOW_UNKNOWN);

  private static final List<Atom> NUMERIC = List.of(LOW, HIGH, DOWN, UP, UNKNOWN_DIRECTION);

  private static final ThreeVal[][] EQUALITY = {
    {TRUE, FALSE, FALSE, FALSE, UNKNOWN},
    {FALSE, TRUE, FALSE, FALSE, UNKNOWN},
    {FALSE, FALSE, UNKNOWN, FALSE, UNKNOWN},
    {FALSE, FALSE, FALSE, UNKNOWN, UNKNOWN},
    {UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN}
  };

  private static final ThreeVal[][] LESS_THAN = {
    {FALSE, TRUE, FALSE, TRUE, UNKNOWN},
    {FALSE, FALSE, FALSE, TRUE, UNKNOWN},
    {TRUE, TRUE, UNKNOWN, TRUE, UNKNOWN},
    {FALSE, FALSE, FALSE, UNKNOWN, UNKNOWN},
    {UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN}
  };

  @Test
  public void numericEqualityCoversConcreteAndOverflowCombinations() {
    for (int left = 0; left < NUMERIC.size(); left++) {
      for (int right = 0; right < NUMERIC.size(); right++) {
        assertEquals(
            EQUALITY[left][right],
            Atom.threeEqual(NUMERIC.get(left), NUMERIC.get(right)),
            comparisonMessage("equality", left, right));
      }
    }
  }

  @Test
  public void numericOrderingCoversConcreteAndOverflowCombinations() {
    for (int left = 0; left < NUMERIC.size(); left++) {
      for (int right = 0; right < NUMERIC.size(); right++) {
        Atom a = NUMERIC.get(left);
        Atom b = NUMERIC.get(right);
        ThreeVal equal = EQUALITY[left][right];

        assertEquals(
            LESS_THAN[left][right],
            Atom.threeLessThan(a, b),
            comparisonMessage("less than", left, right));
        assertEquals(
            LESS_THAN[left][right].or(equal),
            Atom.threeLessEqual(a, b),
            comparisonMessage("less than or equal", left, right));
        assertEquals(
            LESS_THAN[right][left],
            Atom.threeGreater(a, b),
            comparisonMessage("greater than", left, right));
        assertEquals(
            LESS_THAN[right][left].or(equal),
            Atom.threeGreaterEqual(a, b),
            comparisonMessage("greater than or equal", left, right));
      }
    }
  }

  @Test
  public void labelEqualityUsesBothTheLabelAndItsKind() {
    Atom generic = new GenericLabelAtom("Node$0");
    Atom sameGeneric = new GenericLabelAtom("Node$0");
    Atom otherGeneric = new GenericLabelAtom("Node$1");
    Atom string = new StringAtom("Node$0");

    assertEquals(TRUE, Atom.threeEqual(generic, sameGeneric));
    assertEquals(FALSE, Atom.threeEqual(generic, otherGeneric));
    assertEquals(FALSE, Atom.threeEqual(generic, string));
    assertEquals(FALSE, Atom.threeEqual(string, generic));
    assertEquals(TRUE, Atom.threeEqual(string, new StringAtom("Node$0")));
    assertEquals(FALSE, Atom.threeEqual(generic, new IntegerAtom(0)));
    assertEquals(FALSE, Atom.threeEqual(new IntegerAtom(0), generic));
    assertEquals(FALSE, Atom.threeEqual(string, UNKNOWN_DIRECTION));
  }

  @Test
  public void labelsCannotBeUsedInOrderedComparisons() {
    Atom label = new GenericLabelAtom("A$0");
    Atom integer = new IntegerAtom(0);

    assertThrows(AlloyEvaluatorImplError.class, () -> Atom.threeLessThan(label, integer));
    assertThrows(AlloyEvaluatorImplError.class, () -> Atom.threeLessEqual(integer, label));
    assertThrows(AlloyEvaluatorImplError.class, () -> Atom.threeGreater(label, integer));
    assertThrows(AlloyEvaluatorImplError.class, () -> Atom.threeGreaterEqual(integer, label));
  }

  @Test
  public void structuralIdentityDoesNotPretendToBeSemanticEquality() {
    assertTrue(Atom.structurallyIdentical(new IntegerAtom(1), new IntegerAtom(1)));
    assertFalse(Atom.structurallyIdentical(new IntegerAtom(1), new IntegerAtom(2)));
    assertTrue(
        Atom.structurallyIdentical(
            new OverflowAtom(OVERFLOW_UP), new OverflowAtom(OVERFLOW_UP)));
    assertFalse(
        Atom.structurallyIdentical(
            new OverflowAtom(OVERFLOW_UP), new OverflowAtom(OVERFLOW_DOWN)));
    assertTrue(
        Atom.structurallyIdentical(
            new GenericLabelAtom("A$0"), new GenericLabelAtom("A$0")));
    assertFalse(
        Atom.structurallyIdentical(new GenericLabelAtom("A$0"), new StringAtom("A$0")));

    assertEquals(
        UNKNOWN, Atom.threeEqual(new OverflowAtom(OVERFLOW_UP), new OverflowAtom(OVERFLOW_UP)));
  }

  @Test
  public void factoryClassifiesBoundariesOverflowLabelsAndStrings() {
    AtomFactory factory = new AtomFactory(-8, 7);

    assertAtom(new IntegerAtom(-8), factory.createAtom(-8));
    assertAtom(new IntegerAtom(7), factory.createAtom(7));
    assertEquals(
        OVERFLOW_DOWN, assertInstanceOf(OverflowAtom.class, factory.createAtom(-9)).direction());
    assertEquals(
        OVERFLOW_UP, assertInstanceOf(OverflowAtom.class, factory.createAtom(8)).direction());
    assertAtom(new IntegerAtom(4), factory.createAtom("4", false));
    assertAtom(new GenericLabelAtom("A$0"), factory.createAtom("A$0", false));
    assertAtom(new StringAtom("4"), factory.createAtom("4", true));
    assertEquals(-8, factory.minInt());
    assertEquals(7, factory.maxInt());
  }

  @Test
  public void directionIsPresentOnlyForOverflowAtoms() {
    assertEquals(OVERFLOW_DOWN, Atom.directionOf(DOWN));
    assertEquals(OVERFLOW_UP, Atom.directionOf(UP));
    assertEquals(OVERFLOW_UNKNOWN, Atom.directionOf(UNKNOWN_DIRECTION));
    assertNull(Atom.directionOf(new IntegerAtom(0)));
    assertNull(Atom.directionOf(new GenericLabelAtom("A$0")));
  }

  @Test
  public void atomConstructorsRejectNullPayloads() {
    assertThrows(NullPointerException.class, () -> new GenericLabelAtom(null));
    assertThrows(NullPointerException.class, () -> new StringAtom(null));
    assertThrows(NullPointerException.class, () -> new OverflowAtom(null));
  }

  private static String comparisonMessage(String operation, int left, int right) {
    return NUMERIC.get(left) + " " + operation + " " + NUMERIC.get(right);
  }

  private static void assertAtom(Atom expected, Atom actual) {
    assertTrue(
        Atom.structurallyIdentical(expected, actual),
        () -> "Expected " + expected + " but got " + actual);
  }
}
