package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.*;

import ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection;

public sealed interface Atom permits LabelAtom, IntegerAtom, OverflowAtom {

  // Use this method to check if values are equal. Standard equals does not handle overflows
  // correctly
  public static ThreeVal threeEqual(Atom a, Atom b) {
    if (a instanceof LabelAtom la) {
      if (b instanceof LabelAtom lb) {
        return convertThree(la.getClass() == lb.getClass() && la.label().equals(lb.label()));
      }
      return FALSE;
    } else if (b instanceof LabelAtom) {
      return FALSE;
    }

    if (a instanceof IntegerAtom ia && b instanceof IntegerAtom ib) {
      return convertThree(ia.value() == ib.value());
    }

    // at least one overflows
    OverflowDirection da = directionOf(a);
    OverflowDirection db = directionOf(b);

    if (da == OverflowDirection.OVERFLOW_UNKNOWN || db == OverflowDirection.OVERFLOW_UNKNOWN) {
      return UNKNOWN;
    }
    // both known directions, or one known-overflow vs. a plain int (null direction)
    return (da == db) ? UNKNOWN : FALSE;
  }

  // Use this method to compare atoms
  public static ThreeVal threeLessThan(Atom a, Atom b) {
    if (a instanceof LabelAtom || b instanceof LabelAtom) {
      throw AlloyEvaluatorImplError.orderedComparisonOnLabel(a, b);
    }

    if (a instanceof IntegerAtom ai) {
      if (b instanceof IntegerAtom bi) {
        return convertThree(ai.value() < bi.value());
      }
      // b must be OverflowAtom here, so directionOf(b) is never null
      OverflowDirection db = directionOf(b);
      return switch (db) {
        case OVERFLOW_DOWN -> FALSE;
        case OVERFLOW_UP -> TRUE;
        case OVERFLOW_UNKNOWN -> UNKNOWN;
        default -> throw AlloyEvaluatorImplError.unexpectedOverflowDirection(b);
      };
    } else if (a instanceof OverflowAtom ao) {
      OverflowDirection da = ao.direction();
      OverflowDirection db = directionOf(b); // null if b is a plain IntegerAtom

      if (da == OverflowDirection.OVERFLOW_UNKNOWN || db == OverflowDirection.OVERFLOW_UNKNOWN) {
        return UNKNOWN;
      }
      if (da == db) {
        return UNKNOWN;
      }
      return convertThree(da == OverflowDirection.OVERFLOW_DOWN);
    } else {
      throw AlloyEvaluatorImplError.unexpectedAtomSubtype(a);
    }
  }

  // This method checks that the atoms have the same structure. It does not mean they are
  // semantically equal
  public static boolean structurallyIdentical(Atom a, Atom b) {
    if (a instanceof LabelAtom la && b instanceof LabelAtom lb) {
      return la.getClass() == lb.getClass() && la.label().equals(lb.label());
    } else if (a instanceof IntegerAtom ia && b instanceof IntegerAtom ib) {
      return ia.value() == ib.value();
    } else if (a instanceof OverflowAtom oa && b instanceof OverflowAtom ob) {
      return oa.direction() == ob.direction();
    } else {
      return false;
    }
  }

  public static ThreeVal threeGreaterEqual(Atom a, Atom b) {
    return threeLessThan(a, b).not();
  }

  public static ThreeVal threeLessEqual(Atom a, Atom b) {
    return threeLessThan(a, b).or(threeEqual(a, b));
  }

  public static ThreeVal threeGreater(Atom a, Atom b) {
    return threeLessThan(a, b).not().and(threeEqual(a, b).not());
  }

  // returns the overflow direction, or null if a is a non-overflowing atom
  public static OverflowDirection directionOf(Atom a) {
    return (a instanceof OverflowAtom oa) ? oa.direction() : null;
  }
}
