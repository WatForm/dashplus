package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TLAPlusFormulaApplication extends TLAPlusExpression {

    private String name;
    private List<TLAPlusExpression> params;

    public TLAPlusFormulaApplication(String name) {
        this.name = name;
        this.params = new ArrayList<>();
    }

    public List<TLAPlusExpression> getChildren() {
        return this.params;
    }

    public TLAPlusFormulaApplication(String name, List<TLAPlusExpression> parameters) {
        this.name = name;
        this.params = parameters;
    }

    @Override
    public void toString(StringBuilder sb, int ident) {

        int n = this.params.size();

        if (n == 0) {
            sb.append(this.name);
            return;
        }

        sb.append(this.name + TLAPlusStrings.BRACKET_OPEN);
        for (int i = 0; i < n; i++) {
            this.params.get(i).toString(sb, ident);
            if (i != n - 1) sb.append(TLAPlusStrings.COMMA);
        }
        sb.append(TLAPlusStrings.BRACKET_CLOSE);

        return;
    }
}
