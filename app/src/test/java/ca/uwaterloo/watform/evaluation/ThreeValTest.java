package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.FALSE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.UNKNOWN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.BinaryOperator;
import org.junit.jupiter.api.Test;

public class ThreeValTest {
  private static final ThreeVal[] VALUES = {TRUE, FALSE, UNKNOWN};

  @Test
  public void binaryOperatorsFollowThreeValuedTruthTables() {
    assertTruthTable(
        "and",
        ThreeVal::and,
        new ThreeVal[][] {{TRUE, FALSE, UNKNOWN}, {FALSE, FALSE, FALSE}, {UNKNOWN, FALSE, UNKNOWN}});
    assertTruthTable(
        "or",
        ThreeVal::or,
        new ThreeVal[][] {{TRUE, TRUE, TRUE}, {TRUE, FALSE, UNKNOWN}, {TRUE, UNKNOWN, UNKNOWN}});
    assertTruthTable(
        "iff",
        ThreeVal::iff,
        new ThreeVal[][] {{TRUE, FALSE, UNKNOWN}, {FALSE, TRUE, UNKNOWN}, {UNKNOWN, UNKNOWN, UNKNOWN}});
    assertTruthTable(
        "implies",
        ThreeVal::impl,
        new ThreeVal[][] {{TRUE, FALSE, UNKNOWN}, {TRUE, TRUE, TRUE}, {TRUE, UNKNOWN, UNKNOWN}});
  }

  @Test
  public void negationAndBooleanConversionAreConsistent() {
    assertEquals(FALSE, TRUE.not());
    assertEquals(TRUE, FALSE.not());
    assertEquals(UNKNOWN, UNKNOWN.not());

    assertEquals(TRUE, ThreeVal.convertThree(true));
    assertEquals(FALSE, ThreeVal.convertThree(false));
  }

  @Test
  public void shortCircuitChecksMatchTheirDefinitiveResults() {
    assertFalse(TRUE.shortCircuitsAnd());
    assertTrue(FALSE.shortCircuitsAnd());
    assertFalse(UNKNOWN.shortCircuitsAnd());
    assertEquals(FALSE, ThreeVal.shortCircuitAndResult());

    assertTrue(TRUE.shortCircuitsOr());
    assertFalse(FALSE.shortCircuitsOr());
    assertFalse(UNKNOWN.shortCircuitsOr());
    assertEquals(TRUE, ThreeVal.shortCircuitOrResult());

    assertFalse(TRUE.shortCircuitImpl());
    assertTrue(FALSE.shortCircuitImpl());
    assertFalse(UNKNOWN.shortCircuitImpl());
    assertEquals(TRUE, ThreeVal.shortCircuitImplResult());
  }

  private static void assertTruthTable(
      String operator, BinaryOperator<ThreeVal> operation, ThreeVal[][] expected) {
    for (int left = 0; left < VALUES.length; left++) {
      for (int right = 0; right < VALUES.length; right++) {
        assertEquals(
            expected[left][right],
            operation.apply(VALUES[left], VALUES[right]),
            operator + "(" + VALUES[left] + ", " + VALUES[right] + ")");
      }
    }
  }
}
