package ca.uwaterloo.watform.alloyinterface;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** Domain wrapper around the relation-to-tuples data that represents an Alloy instance. */
public final class Instance {
    private final Map<String, Set<List<String>>> relations;
    private static final String PREFIX = AlloyStrings.THIS + AlloyStrings.SLASH;
    private final Set<List<String>> univ;
    private final Set<List<String>> iden;

    public Instance(Map<String, Set<List<String>>> relations) {
        this.relations = new HashMap<>();
        this.univ = new HashSet<>();
        this.iden = new HashSet<>();

        for (var entry : relations.entrySet()) {
            var newKey = removeParentSigInfo(entry.getKey());
            this.relations.put(newKey, entry.getValue());

            if (entry.getKey().startsWith(PREFIX) && newKey.equals(entry.getKey())) {
                // top-level sig: no dot was found, key was unchanged
                for (var atom : entry.getValue()) {
                    univ.add(atom); // e.g. [A$1]
                    iden.add(List.of(atom.get(0), atom.get(0))); // e.g. [A$1, A$1]
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

    public Optional<Set<List<String>>> getRelation(String relationName) {
        return Optional.ofNullable(relations.get(normalize(relationName)));
    }

    public Optional<Set<List<String>>> get(String key) {
        return Optional.ofNullable(relations.get(key));
    }

    public Set<String> getRelationMapKeys() {
        Set<String> keys = new HashSet<>(relations.keySet());
        return keys;
    }

    public Set<List<String>> getUniv() {
        return univ;
    }

    public Set<List<String>> getIden() {
        return iden;
    }

    // will have to be edited, does not support imports
    private static String normalize(String relationName) {
        if (relationName.startsWith(PREFIX)) {
            return relationName;
        }
        return PREFIX + relationName;
    }
}
