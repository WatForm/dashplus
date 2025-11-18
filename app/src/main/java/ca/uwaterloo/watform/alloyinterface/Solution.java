package ca.uwaterloo.watform.alloyinterface;

import ca.uwaterloo.watform.utils.CommonStrings;
import edu.mit.csail.sdg.translator.A4Solution;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kodkod.ast.Relation;
import kodkod.instance.Instance;
import kodkod.instance.Tuple;

public final class Solution {
    private A4Solution a4Solution;
    private final Map<String, Set<List<String>>> map;

    public Solution(A4Solution a4Solution) {
        this.a4Solution = a4Solution;
        this.map = new HashMap<>();
        populateMap();
    }

    private void populateMap() {
        this.map.clear();
        Instance instance = this.a4Solution.debugExtractKInstance();
        for (Relation r : instance.relations()) {
            Set<List<String>> set = new HashSet<>();
            for (Tuple tuple : instance.tuples(r)) {
                List<String> li = new ArrayList<>();
                for (int i = 0; i < tuple.arity(); i++) {
                    li.add((String) tuple.atom(i));
                }
                set.add(li);
            }
            this.map.put(r.name(), set);
        }
    }

    public boolean contains(String name) {
        return this.map.containsKey(name);
    }

    public Set<List<String>> get(String name) {
        return this.map.getOrDefault(name, Collections.emptySet());
    }

    public void next() {
        this.a4Solution = this.a4Solution.next();
        this.populateMap();
    }

    public void eval() {}

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CommonStrings.DIVIDER + CommonStrings.NEWLINE);
        for (Map.Entry<String, Set<List<String>>> entry : this.map.entrySet()) {
            stringBuilder.append(entry.getKey() + CommonStrings.NEWLINE);
            stringBuilder.append(entry.getValue() + CommonStrings.NEWLINE);
            stringBuilder.append(CommonStrings.DIVIDER + CommonStrings.NEWLINE);
        }
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((a4Solution == null) ? 0 : a4Solution.hashCode());
        result = prime * result + ((map == null) ? 0 : map.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Solution other = (Solution) obj;
        if (a4Solution == null) {
            if (other.a4Solution != null) return false;
        } else if (!a4Solution.equals(other.a4Solution)) return false;
        if (map == null) {
            if (other.map != null) return false;
        } else if (!map.equals(other.map)) return false;
        return true;
    }
}
