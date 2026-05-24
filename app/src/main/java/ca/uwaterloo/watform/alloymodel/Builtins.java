package ca.uwaterloo.watform.alloymodel;

// for AlloyQnameVars with built in meaning (found in util files)

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import java.util.*;

public class Builtins {

    protected static final Optional<Integer> UNKNOWN_ARITY = Optional.empty();
    // private static final Optional<Integer> BOOLEAN_ARITY = Optional.of(1);
    protected static final Optional<Integer> ONE_ARITY = Optional.of(1);
    protected static final Optional<Integer> TWO_ARITY = Optional.of(2);

    private static final Map<String, Integer> builtinsArity =
            Map.ofEntries(
                    Map.entry(AlloyStrings.boolName, 1),
                    Map.entry(AlloyStrings.shortBoolName, 1),
                    Map.entry(AlloyStrings.trueName, 1),
                    Map.entry(AlloyStrings.shortTrueName, 1),
                    Map.entry(AlloyStrings.falseName, 1),
                    Map.entry(AlloyStrings.shortFalseName, 1),
                    Map.entry(AlloyStrings.isTrue, 2),
                    Map.entry(AlloyStrings.shortIsTrue, 2),
                    Map.entry(AlloyStrings.isFalse, 2),
                    Map.entry(AlloyStrings.shortIsFalse, 2),
                    Map.entry(AlloyStrings.util_plus, 3),
                    Map.entry(AlloyStrings.util_minus, 3),
                    Map.entry(AlloyStrings.util_lt, 3),
                    Map.entry(AlloyStrings.shortMaxName, 2),
                    Map.entry(AlloyStrings.shortMinName, 2),
                    // util/sequniv.als takes two args and returns a seq, which has arity 2
                    Map.entry(AlloyStrings.shortSeqUnivDelete, 4),
                    Map.entry("idx", 2),
                    Map.entry("gte", 3),
                    Map.entry("lte", 3),
                    Map.entry("gt", 3),
                    Map.entry("nextRing", 2),
                    Map.entry("firstElem", 2),
                    Map.entry("first", 2),
                    Map.entry("next", 2),
                    Map.entry("__Snapshot/next", 2),
                    Map.entry("isEmpty", 2));

    protected static Boolean isBuiltin(String name) {
        return builtinsArity.containsKey(name);
    }

    protected static Integer builtinArity(String name) {
        if (!isBuiltin(name)) {
            throw AlloyModelImplError.builtinNotFound(name);
        } else {
            return builtinsArity.get(name);
        }
    }
}
