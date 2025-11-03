package ca.uwaterloo.watform.tlaplusast;

import ca.uwaterloo.watform.utils.ASTNode;
import java.util.ArrayList;
import java.util.List;

public class TLAPlusSetFilter extends TLAPlusExpression {

    private List<ASTNode> children;

    public TLAPlusSetFilter(TLAPlusVariable v, ASTNode set, ASTNode exp) {
        this.children = new ArrayList<>();
        this.children.add(v);
        this.children.add(set);
        this.children.add(exp);
    }

    /*
    @Override
    public List<String> toStringList() {
        List<String> result = new ArrayList<>();
        result.add(TLAPlusStrings.SET_START);
        result.addAll(this.children.get(0).toStringList());
        result.add(TLAPlusStrings.SET_IN);
        result.addAll(this.children.get(1).toStringList());
        result.add(TLAPlusStrings.COLON);
        result.addAll(this.children.get(2).toStringList());
        result.add(TLAPlusStrings.SET_END);
        return result;
    } */

    @Override
    public void toString(StringBuilder sb, int ident) {
        return;
        // TODO fix this
    }
}
