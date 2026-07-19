package ca.uwaterloo.watform.alloyevaluator;

import static ca.uwaterloo.watform.alloyevaluator.ThreeVal.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.*;

public class AtomTuple {
    private final List<Atom> atoms;
    private final boolean overflows;

    public AtomTuple(List<Atom> atoms) {
        Objects.requireNonNull(atoms);
        this.atoms = new ArrayList<>(atoms);
        this.overflows = containsMatch(this.atoms, a -> a instanceof OverflowAtom);
    }

    @Override
    public String toString() {
        return atoms.toString();
    }

    public static ThreeVal threeEqual(AtomTuple a, AtomTuple b) {
        if (a.arity() != b.arity()) return FALSE;

        ThreeVal returnVal = TRUE;
        for (int i = 0; i < a.arity(); i++) {
            returnVal = returnVal.and(Atom.threeEqual(a.atoms.get(i), b.atoms.get(i)));
            if (returnVal.shortCircuitsAnd()) return returnVal;
        }
        return returnVal;
    }

    // This method checks that the tuples have the same structure (will behave identically in every
    // evaluation scenario).
    // Does not necessarily mean they are semantically equal
    public static boolean structurallyIdentical(AtomTuple a, AtomTuple b) {
        if (a.arity() != b.arity()) return false;

        for (int i = 0; i < a.arity(); i++) {
            if (!Atom.structurallyIdentical(a.get(i), b.get(i))) return false;
        }
        return true;
    }

    public Atom first() {
        return firstElement(atoms);
    }

    public Atom last() {
        return lastElement(atoms);
    }

    public Atom get(int idx) {
        if (idx < 0 || idx >= arity())
            throw AlloyEvaluatorImplError.arityError(
                    "Accessing non-existing atom at index: " + idx);
        return atoms.get(idx);
    }

    public boolean containsOverflow() {
        return overflows;
    }

    public int arity() {
        return atoms.size();
    }

    public static AtomTuple concat(AtomTuple a, AtomTuple b) {
        return new AtomTuple(GeneralUtil.concat(a.atoms, b.atoms));
    }

    public static AtomTuple tail(AtomTuple a) {
        return new AtomTuple(GeneralUtil.tail(a.atoms));
    }

    public static AtomTuple allButLast(AtomTuple a) {
        return new AtomTuple(GeneralUtil.allButLast(a.atoms));
    }

    public static AtomTuple transpose(AtomTuple a) {
        return new AtomTuple(reverse(a.atoms));
    }

    public static AtomTuple tupleOfFirst(AtomTuple a) {
        return new AtomTuple(a.atoms.subList(0, 1));
    }

    public static AtomTuple tupleOfLast(AtomTuple a) {
        return new AtomTuple(a.atoms.subList(a.arity() - 1, a.arity()));
    }

    public static AtomTuple join(AtomTuple a, AtomTuple b) {
        if (Atom.threeEqual(a.last(), b.first()) != TRUE)
            throw AlloyEvaluatorImplError.comparisonError("Join occurs on invalid tuples");
        List<Atom> left = GeneralUtil.allButLast(a.atoms);
        List<Atom> right = GeneralUtil.tail(b.atoms);
        return new AtomTuple(GeneralUtil.concat(left, right));
    }
}
