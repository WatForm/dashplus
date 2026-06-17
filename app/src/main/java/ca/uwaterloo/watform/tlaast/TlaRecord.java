package ca.uwaterloo.watform.tlaast;

import ca.uwaterloo.watform.tlaast.tlaliterals.TlaStringLiteral;
import java.util.ArrayList;
import java.util.List;

public class TlaRecord extends TlaOperator {

    /*
    [x |-> e1, y |-> e2, ...]   \* record constructor: a record which field x equals to e1, field y equals to e2, ...
    */

    public static record KeyValue(TlaStringLiteral key, TlaExp value) {}

    public final List<KeyValue> keyValuePairs;

    public TlaRecord(List<KeyValue> keyValuePairs) {
        super(TlaOperator.Associativity.IRRELEVANT, TlaOperator.PrecedenceGroup.SAFE);
        this.keyValuePairs = keyValuePairs;
    }

    @Override
    public List<TlaExp> getChildren() {
        List<TlaExp> children = new ArrayList<>();
        for (var pair : this.keyValuePairs) {
            children.add((TlaExp) pair.key);
            children.add(pair.value);
        }
        return children;
    }

    @Override
    public String toTLAPlusSnippetCore() {
        StringBuilder sb = new StringBuilder();
        sb.append(TlaStrings.SQUARE_BRACKET_OPEN);
        for (int i = 0; i < keyValuePairs.size(); i++) {
            var pair = keyValuePairs.get(i);
            sb.append(
                    " "
                            + pair.key.toTLAPlusSnippetCore()
                            + " "
                            + TlaStrings.MAP
                            + " "
                            + pair.value.toTLAPlusSnippetCore());
            if (i < keyValuePairs.size() - 1) sb.append(",");
        }
        sb.append(TlaStrings.SQUARE_BRACKET_CLOSE);
        return sb.toString();
    }
}
