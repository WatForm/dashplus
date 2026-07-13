package ca.uwaterloo.watform.alloyevaluator;

import java.util.Objects;

public record LabelAtom(String label) implements Atom {
    // Ensure label is not null when initializing
    public LabelAtom {
        Objects.requireNonNull(label);
    }

    @Override
    public String toString() {
        return label;
    }
}
