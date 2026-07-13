package ca.uwaterloo.watform.alloyevaluator;

import static ca.uwaterloo.watform.alloyevaluator.ThreeVal.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class TupleSet {
    private final List<AtomTuple> tuples;
    private final boolean overflows;

    public TupleSet(List<AtomTuple> tuples) {
        Objects.requireNonNull(tuples);
        this.tuples = new ArrayList<>(tuples);
        prune();
        this.overflows = containsMatch(this.tuples, t -> t.containsOverflow());
    }

    // removes concrete duplicates from the set
    private void prune() {
        for (int i = 0; i < tuples.size(); i++) {
            AtomTuple ti = tuples.get(i);
            for (int j = tuples.size() - 1; j > i; j--) {
                if (AtomTuple.threeEqual(ti, tuples.get(j)) == TRUE) {
                    tuples.remove(j);
                }
            }
        }
    }

    // Note: can give false positives if overflows are present
    private boolean isScalar() {
        return tuples.size() == 1 && firstElement(tuples).arity() == 1;
    }

    public Atom getScalar() {
        if (!isScalar()) {
            throw AlloyEvaluatorImplError.setError("Requesting scalar on a non-scalar set");
        }
        return firstElement(tuples).first();
    }

    public ThreeVal contains(AtomTuple check) {
        ThreeVal returnVal = FALSE;
        for (var tuple : tuples) {
            returnVal = AtomTuple.threeEqual(tuple, check).or(returnVal);
            if (returnVal.shortCircuitsOr()) return returnVal;
        }
        return returnVal;
    }

    public static ThreeVal threeSubset(TupleSet a, TupleSet b) {
        ThreeVal returnVal = TRUE;
        for (var tuple : a.tuples) {
            returnVal = returnVal.and(b.contains(tuple));
            if (returnVal.shortCircuitsAnd()) return returnVal;
        }

        return returnVal;
    }

    public static ThreeVal threeEquals(TupleSet a, TupleSet b) {
        return threeSubset(a, b).and(threeSubset(b, a));
    }

    public boolean containsOverflow() {
        return overflows;
    }

    public boolean isEmpty() {
        return tuples.isEmpty();
    }

    public int size() {
        return tuples.size();
    }

    public static TupleSet union(TupleSet a, TupleSet b) {
        return new TupleSet(concat(a.tuples, b.tuples));
    }

    public static TupleSet intersect(TupleSet a, TupleSet b) {
        return filterBy(a, t -> b.contains(t) == TRUE);
    }

    public static TupleSet diff(TupleSet a, TupleSet b) {
        return filterBy(a, t -> b.contains(t) == FALSE);
    }

    public static TupleSet emptySet() {
        return new TupleSet(new ArrayList<>());
    }

    public static TupleSet crossProduct(TupleSet a, TupleSet b) {
        List<AtomTuple> tuples = new ArrayList<>();

        for (var at : a.tuples) {
            for (var bt : b.tuples) {
                tuples.add(AtomTuple.concat(at, bt));
            }
        }
        return new TupleSet(tuples);
    }

    public static TupleSet join(TupleSet a, TupleSet b) {
        List<AtomTuple> tuples = new ArrayList<>();
        for (var at : a.tuples) {
            for (var bt : b.tuples) {
                if (Atom.threeEqual(at.last(), bt.first()) == TRUE) {
                    tuples.add(AtomTuple.concat(AtomTuple.allButLast(at), AtomTuple.tail(bt)));
                }
            }
        }
        return new TupleSet(tuples);
    }

    @Override
    public String toString() {
        return tuples.toString();
    }

    public static TupleSet filterBy(TupleSet a, Predicate<AtomTuple> filterFn) {
        return new TupleSet(GeneralUtil.filterBy(a.tuples, filterFn));
    }

    public static TupleSet mapBy(TupleSet a, Function<AtomTuple, AtomTuple> mapFn) {
        return new TupleSet(GeneralUtil.mapBy(a.tuples, mapFn));
    }

    public static TupleSet createScalar(Atom a) {
        return new TupleSet(List.of(new AtomTuple(List.of(a))));
    }
}
