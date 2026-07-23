package ca.uwaterloo.watform.alloymodel;

import java.util.*;

public class SigScope {

    // optional b/c imports set exactly without a value
    Optional<Integer> max;
    Boolean isExact;

    private SigScope(int max, boolean isExact) {
        this.max = Optional.of(max);
        this.isExact = isExact;
    }

    private SigScope(boolean isExact) {
        this.max = Optional.empty();
        this.isExact = isExact;
    }

    public boolean isExact() {
        return this.isExact;
    }

    public boolean hasValue() {
        return this.max.isPresent();
    }

    public Integer max() {
        return this.max.get();
    }

    public static SigScope ExactScope(int max) {
        return new SigScope(max, true);
    }

    public static SigScope ExactNoValue() {
        return new SigScope(true);
    }

    public static SigScope NonExactScope(int max) {
        return new SigScope(max, false);
    }

    @Override
    public String toString() {
        return (isExact ? "e" : "") + (max.isPresent() ? max.get() : "?");
    }

    @Override
    public boolean equals(Object obj) {
        // written by ChatGPT
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SigScope)) {
            return false;
        }

        SigScope other = (SigScope) obj;
        return isExact == other.isExact && Objects.equals(max, other.max);
    }

    @Override
    public int hashCode() {
        // written by ChatGPT
        return Objects.hash(max, isExact);
    }
}
