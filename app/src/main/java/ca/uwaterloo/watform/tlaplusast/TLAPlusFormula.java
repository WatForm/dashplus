package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TLAPlusFormula extends TLAPlusExpression {

    private String name;

    public TLAPlusFormula(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public TLAPlusFormula(String name, List<TLAPlusASTNode> parameters) {
        this.name = name;
        this.children = parameters;
    }

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
}
