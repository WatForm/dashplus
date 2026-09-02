package ca.uwaterloo.watform.evaluation;

import java.util.Objects;

public abstract sealed class LabelAtom implements Atom permits GenericLabelAtom, StringAtom {
  private final String label;

  protected LabelAtom(String label) {
    this.label = Objects.requireNonNull(label);
  }

  public final String label() {
    return label;
  }

  @Override
  public final String toString() {
    return label;
  }

  @Override
  public final boolean equals(Object other) {
    return this == other
        || (other != null
            && getClass() == other.getClass()
            && label.equals(((LabelAtom) other).label));
  }

  @Override
  public final int hashCode() {
    return Objects.hash(getClass(), label);
  }
}
