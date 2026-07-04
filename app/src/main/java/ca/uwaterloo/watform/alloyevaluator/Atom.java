package ca.uwaterloo.watform.alloyevaluator;

import java.util.Objects;

public class Atom {
    private final boolean integer;
    private final String label;
    private final int value;
    private final boolean overflowing;

    public Atom() {
        overflowing = true;
        value = 0;
        label = "";
        integer = true;
    }

    public Atom(int value) {
        this.integer = true;
        this.value = value;
        label = "";
        overflowing = false;
    }

    public Atom(String label) {
        this.integer = false;
        this.label = Objects.requireNonNull(label);
        value = 0;
        overflowing = false;
    }

    public boolean isInteger() {
        return integer;
    }

    public String getLabel() {
        return label;
    }

    public int getValue() {
        return value;
    }

    public boolean isOverflowing() {
        return overflowing;
    }

    @Override
    public String toString() {
        return integer ? String.valueOf(value) : label;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Atom other)) return false;

        if (this.overflowing || other.overflowing) return false;
        if (integer != other.integer) return false;

        return integer ? value == other.value : label.equals(other.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(integer, label, value, overflowing);
    }
}
