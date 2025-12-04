package ca.uwaterloo.watform.tlaast;

import java.util.ArrayList;
import java.util.List;

public class TlaAppl extends TlaOperator {

    private final String name;
    public final List<TlaExp> parameters;

    public TlaAppl(String name, List<TlaExp> parameters) {
        super(TlaOperator.Associativity.IRRELEVANT, TlaOperator.PrecedenceGroup.SAFE);
        this.name = name;
        this.parameters = parameters;
    }

    public TlaAppl(String name) {
        this(name, new ArrayList<>());
    }

    public List<TlaExp> getChildren() {
        return this.parameters;
    }

    @Override
    public String toTLAPlusSnippetCore() {
        int n = this.parameters.size();
        if (n == 0) return this.name;

        StringBuilder sb = new StringBuilder();
        sb.append(this.name + TlaStrings.BRACKET_OPEN);
        for (int i = 0; i < n; i++) {
            sb.append(this.getTLASnippetOfChild(this.parameters.get(i)));
            if (i != n - 1) sb.append(TlaStrings.COMMA);
        }
        sb.append(TlaStrings.BRACKET_CLOSE);
        return sb.toString();
    }
}
