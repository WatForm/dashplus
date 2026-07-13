package ca.uwaterloo.watform.alloyinterface;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyevaluator.AtomFactory;
import ca.uwaterloo.watform.alloyevaluator.AtomTuple;
import ca.uwaterloo.watform.alloyevaluator.OverflowAtom.OverflowDirection;
import ca.uwaterloo.watform.alloyevaluator.TupleSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** Domain wrapper around the relation-to-tuples data that represents an Alloy instance. */
public final class Instance {
    private final Map<String, TupleSet> relations;
    private static final String PREFIX = AlloyStrings.THIS + AlloyStrings.SLASH;
    private final TupleSet univ;
    private final TupleSet iden;
    private final TupleSet intSet;
    private final AtomFactory factory;

    public Instance(Map<String, Set<List<String>>> relations) {
        this.relations = new HashMap<>();
        int min, max;
        try {
            min = Integer.parseInt(setToList(relations.get("Int/min")).get(0).get(0));
            max = Integer.parseInt(setToList(relations.get("Int/max")).get(0).get(0));
        } catch (Exception e) {
            throw AlloyInterfaceImplError.failedCast("Failed to extract int min and max");
        }
        factory = new AtomFactory(min, max);

        List<AtomTuple> univList = new ArrayList<>();
        List<AtomTuple> intList =
                mapBy(
                        range(min, max + 1),
                        i -> new AtomTuple(List.of(factory.createAtom(i.intValue()))));
        univList.addAll(intList);
        intSet = new TupleSet(intList);

        for (var entry : relations.entrySet()) {
            var newKey = removeParentSigInfo(entry.getKey());
            this.relations.put(
                    newKey,
                    new TupleSet(
                            mapBy(
                                    setToList(entry.getValue()),
                                    t -> new AtomTuple(mapBy(t, a -> factory.createAtom(a))))));
            var it = entry.getValue().iterator();
            if (it.hasNext() && it.next().size() == 1) {
                univList.addAll(
                        mapBy(
                                setToList(entry.getValue()),
                                t -> new AtomTuple(mapBy(t, a -> factory.createAtom(a)))));
            }
        }

        univ = new TupleSet(univList);
        iden = new TupleSet(mapBy(univList, t -> AtomTuple.concat(t, t)));
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

    public Optional<TupleSet> getRelation(String relationName) {
        return Optional.ofNullable(relations.get(normalize(relationName)));
    }

    public Optional<TupleSet> get(String key) {
        return Optional.ofNullable(relations.get(key));
    }

    public Set<String> getRelationMapKeys() {
        Set<String> keys = new HashSet<>(relations.keySet());
        return keys;
    }

    public TupleSet getUniv() {
        return univ;
    }

    public TupleSet getIden() {
        return iden;
    }

    public TupleSet getIntSet() {
        return intSet;
    }

    // will have to be edited, does not support imports
    private static String normalize(String relationName) {
        if (relationName.startsWith(PREFIX)) {
            return relationName;
        }
        return PREFIX + relationName;
    }

    public TupleSet getIntScalar(int val) {
        return new TupleSet(List.of(new AtomTuple(List.of(factory.createAtom(val)))));
    }

    private TupleSet getOverflowScalar(OverflowDirection direction) {
        return new TupleSet(List.of(new AtomTuple(List.of(factory.createAtom(direction)))));
    }

    public TupleSet getCardinality(TupleSet set) {
        if (set.containsOverflow()) {
            return getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN);
        } else {
            return getIntScalar(set.size());
        }
    }
}
