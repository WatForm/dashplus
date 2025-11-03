package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TLAPlusFormulaApplication extends TLAPlusExpression {

    private String name;
    private List<? extends TLAPlusExpression> params;

    public TLAPlusFormulaApplication(String name) {
        this.name = name;
        this.params = new ArrayList<>();
    }

    public TLAPlusFormulaApplication(String name, List<? extends TLAPlusExpression> parameters) {
        this.name = name;
        this.params = parameters;
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        return;
        // TODO fix this
    }

    /*
    @Override
    public List<String> toStringList() {

        List<String> result = new ArrayList<>();
        int n = this.children.size();

        if (n == 0) // no parameters
        {
            result.add(this.name);
            return result;
        }

        result.add(this.name + TLAPlusStrings.BRACKET_OPEN);
        for (int i = 0; i < n; i++) {
            result.addAll(this.children.get(i).toStringList());
            if (i != n - 1) result.add(TLAPlusStrings.COMMA);
        }
        result.add(TLAPlusStrings.BRACKET_CLOSE);
        return result;
    }
     */
}
