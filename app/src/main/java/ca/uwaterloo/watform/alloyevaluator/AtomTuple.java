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
        if (atoms.size() == 0)
            throw AlloyEvaluatorImplError.arityError("Tuple with arity 0 is constructed");
        this.atoms = new ArrayList<>(atoms);
        this.overflows = containsMatch(this.atoms, a -> a instanceof OverflowAtom);
    }

    @Override
    public String toString() {
        return atoms.toString();
    }

    public static ThreeVal threeEqual(AtomTuple a, AtomTuple b) {
        if (a.atoms.size() != b.atoms.size()) return FALSE;

        ThreeVal returnVal = TRUE;
        for (int i = 0; i < a.atoms.size(); i++) {
            returnVal = returnVal.and(Atom.threeEqual(a.atoms.get(i), b.atoms.get(i)));
            if (returnVal.shortCircuitsAnd()) return returnVal;
        }
        return returnVal;
    }

    public Atom first() {
        return firstElement(atoms);
    }

    public Atom last() {
        return lastElement(atoms);
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
}
