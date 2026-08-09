package ca.uwaterloo.watform.evaluation;

public record IntegerAtom(int value) implements Atom {
  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
