package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TLAPlusFormulaApplication extends TLAPlusOperator {

    private final String name;
    public final List<TLAPlusExpression> parameters;

    public TLAPlusFormulaApplication(String name, List<TLAPlusExpression> parameters) {
        super(TLAPlusOperator.Associativity.IRRELEVANT, TLAPlusOperator.PrecedenceGroup.SAFE);
        this.name = name;
        this.parameters = parameters;
    }

    public TLAPlusFormulaApplication(String name) {
        this(name, new ArrayList<>());
    }

    public List<TLAPlusExpression> getChildren() {
        return this.parameters;
    }

    @Override
    public String toTLAPlusSnippetCore() {
        int n = this.parameters.size();
        if (n == 0) return this.name;

        StringBuilder sb = new StringBuilder();
        sb.append(this.name + TLAPlusStrings.BRACKET_OPEN);
        for (int i = 0; i < n; i++) {
            sb.append(this.getTLASnippetOfChild(this.parameters.get(i)));
            if (i != n - 1) sb.append(TLAPlusStrings.COMMA);
        }
        sb.append(TLAPlusStrings.BRACKET_CLOSE);
        return sb.toString();
    }
}
