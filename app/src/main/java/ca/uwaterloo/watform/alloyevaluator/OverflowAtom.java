package ca.uwaterloo.watform.alloyevaluator;

import java.util.Objects;

public record OverflowAtom(OverflowDirection direction) implements Atom {
    public enum OverflowDirection {
        OVERFLOW_DOWN,
        OVERFLOW_UP,
        OVERFLOW_UNKNOWN;

        @Override
        public String toString() {
            return switch (this) {
                case OVERFLOW_DOWN -> "Overflow Down";
                case OVERFLOW_UP -> "Overflow Up";
                case OVERFLOW_UNKNOWN -> "Overflow Unknown";
                default ->
                        throw AlloyEvaluatorImplError.atomConstructionError(
                                "Overflowed atom constructed with unexpected value");
            };
        }
    }

    // ensure the direction is not null
    public OverflowAtom {
        Objects.requireNonNull(direction);
    }

    @Override
    public String toString() {
        return direction.toString();
    }
}
