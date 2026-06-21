package ca.uwaterloo.watform.alloyinterface;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** Domain wrapper around the relation-to-tuples data that represents an Alloy instance. */
public final class Instance {
    private final Map<String, Set<List<String>>> relations;
    private static final String PREFIX = AlloyStrings.THIS + AlloyStrings.SLASH;

    public Instance(Map<String, Set<List<String>>> relations) {
        this.relations = relations;
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

    private static String normalize(String relationName) {
        if (relationName.startsWith(PREFIX)) {
            return relationName;
        }
        return PREFIX + relationName;
    }
}
