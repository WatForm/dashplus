package ca.uwaterloo.watform.evaluation;

import ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection;
import ca.uwaterloo.watform.utils.Pos;
import ca.uwaterloo.watform.utils.Reporter;
import ca.uwaterloo.watform.utils.Reporter.WarningUser;

public class AtomFactory {
  private final int min;
  private final int max;

  public AtomFactory(int min, int max) {
    this.min = min;
    this.max = max;
  }

  public Atom createAtom(String label, boolean isString, Pos pos) {
    if (isString) return new StringAtom(label);
    try {
      return createAtom(Integer.parseInt(label), pos);
    } catch (NumberFormatException e) {
      return new GenericLabelAtom(label);
    }
  }

  public Atom createAtom(int value, Pos pos) {
    if (value > max) return createAtom(OverflowDirection.OVERFLOW_UP, pos);
    if (value < min) return createAtom(OverflowDirection.OVERFLOW_DOWN, pos);
    return new IntegerAtom(value);
  }

  public Atom createAtom(OverflowDirection direction, Pos pos) {
    Reporter.INSTANCE.addWarning(new WarningUser(pos, "Created overflowing atom: " + direction));
    return new OverflowAtom(direction);
  }

  public Atom createAtom(String label, boolean isString) {
    return createAtom(label, isString, null);
  }

  public Atom createAtom(int value) {
    return createAtom(value, null);
  }

  public Atom createAtom(OverflowDirection direction) {
    return createAtom(direction, null);
  }

  public int minInt() {
    return min;
  }

  public int maxInt() {
    return max;
  }
}
