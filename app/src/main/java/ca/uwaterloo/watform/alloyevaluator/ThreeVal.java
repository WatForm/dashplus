package ca.uwaterloo.watform.alloyevaluator;

public enum ThreeVal {
    TRUE,
    FALSE,
    UNKNOWN;

    public ThreeVal and(ThreeVal other) {
        if (this == FALSE || other == FALSE) return FALSE;
        if (this == UNKNOWN || other == UNKNOWN) return UNKNOWN;
        return TRUE;
    }

    public ThreeVal or(ThreeVal other) {
        if (this == TRUE || other == TRUE) return TRUE;
        if (this == UNKNOWN || other == UNKNOWN) return UNKNOWN;
        return FALSE;
    }

    public ThreeVal iff(ThreeVal other) {
        if (this == UNKNOWN || other == UNKNOWN) return UNKNOWN;
        return this == other ? TRUE : FALSE;
    }

    public ThreeVal impl(ThreeVal other) {
        if (this == FALSE || other == TRUE) return TRUE;
        if (this == UNKNOWN || other == UNKNOWN) return UNKNOWN;
        return FALSE;
    }

    public ThreeVal not() {
        if (this == TRUE) return FALSE;
        if (this == FALSE) return TRUE;
        return UNKNOWN;
    }

    public boolean shortCircuitsAnd() {
        return this == FALSE;
    }

    public static ThreeVal shortCircuitAndResult() {
        return FALSE;
    }

    public boolean shortCircuitsOr() {
        return this == TRUE;
    }

    public static ThreeVal shortCircuitOrResult() {
        return TRUE;
    }

    public boolean shortCircuitImpl() {
        return this == FALSE;
    }

    public static ThreeVal shortCircuitImplResult() {
        return TRUE;
    }

    public static ThreeVal convertThree(boolean val) {
        return val ? TRUE : FALSE;
    }

    @Override
    public String toString() {
        return switch (this) {
            case TRUE -> "TRUE";
            case FALSE -> "FALSE";
            case UNKNOWN -> "UNKNOWN";
        };
    }
}
