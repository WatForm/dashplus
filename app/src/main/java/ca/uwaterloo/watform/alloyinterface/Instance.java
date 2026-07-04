package ca.uwaterloo.watform.alloyinterface;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyevaluator.Atom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** Domain wrapper around the relation-to-tuples data that represents an Alloy instance. */
public final class Instance {
    private final Map<String, Set<List<Atom>>> relations;
    private static final String PREFIX = AlloyStrings.THIS + AlloyStrings.SLASH;
    private final Set<List<Atom>> univ;
    private final Set<List<Atom>> iden;

    public Instance(Map<String, Set<List<String>>> relations) {
        this.relations = new HashMap<>();
        this.univ = new HashSet<>();
        this.iden = new HashSet<>();

        int min, max;
        try {
            min = Integer.parseInt(setToList(relations.get("Int/min")).get(0).get(0));
            max = Integer.parseInt(setToList(relations.get("Int/max")).get(0).get(0));
        } catch (Exception e) {
            throw AlloyInterfaceImplError.failedCast("Cant eval to int or sumn");
        }

        Set<List<Atom>> ints = new HashSet<>();
        for (int i = min; i <= max; i++) {
            ints.add(List.of(new Atom(i)));
        }
        this.relations.put(
                "this/Int", ints); // TODO: full revamp, this is not a proper way to do it

        for (var entry : relations.entrySet()) {
            var newKey = removeParentSigInfo(entry.getKey());
            this.relations.put(newKey, convertToAtoms(entry.getValue()));

            if (entry.getKey().startsWith(PREFIX) && newKey.equals(entry.getKey())) {
                // top-level sig: no dot was found, key was unchanged
                for (var tuple : entry.getValue()) {
                    univ.add(convertTupleToAtoms(tuple)); // e.g. [A$1]

                    iden.add(
                            List.of(
                                    convertToAtom(tuple.get(0)),
                                    convertToAtom(tuple.get(0)))); // e.g. [A$1, A$1]
                }
            }
        }
    }

    private static String removeParentSigInfo(String key) {
        if (!key.startsWith(PREFIX)) return key;
        String body = key.substring(PREFIX.length());
        int lastDot = body.lastIndexOf('.');
        if (lastDot == -1) return key;
        return PREFIX + body.substring(lastDot + 1);
    }

    public boolean contains(String relationName) {
        return relations.containsKey(normalize(relationName))
                || relations.containsKey(relationName);
    }

    public Optional<Set<List<Atom>>> getRelation(String relationName) {
        return Optional.ofNullable(relations.get(normalize(relationName)));
    }

    public Optional<Set<List<Atom>>> get(String key) {
        return Optional.ofNullable(relations.get(key));
    }

    public Set<String> getRelationMapKeys() {
        Set<String> keys = new HashSet<>(relations.keySet());
        return keys;
    }

    public Set<List<Atom>> getUniv() {
        return univ;
    }

    public Set<List<Atom>> getIden() {
        return iden;
    }

    // will have to be edited, does not support imports
    private static String normalize(String relationName) {
        if (relationName.startsWith(PREFIX)) {
            return relationName;
        }
        return PREFIX + relationName;
    }

    public static Atom convertToAtom(String value) {
        try {
            int intVal = Integer.parseInt(value);
            return new Atom(intVal);
        } catch (NumberFormatException e) {
            return new Atom(value);
        }
    }

    public static Atom convertToAtom(int value) {
        return new Atom(value);
    }

    private static List<Atom> convertTupleToAtoms(List<String> values) {
        return mapBy(values, Instance::convertToAtom);
    }

    private static Set<List<Atom>> convertToAtoms(Set<List<String>> values) {
        return mapBy(values, Instance::convertTupleToAtoms);
    }
}
