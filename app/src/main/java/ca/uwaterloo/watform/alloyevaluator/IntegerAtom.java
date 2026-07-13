package ca.uwaterloo.watform.alloyevaluator;

public record IntegerAtom(int value) implements Atom {
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
