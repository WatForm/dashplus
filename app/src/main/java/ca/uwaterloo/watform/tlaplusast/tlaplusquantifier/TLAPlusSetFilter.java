package ca.uwaterloo.watform.tlaplusast.tlaplusquantifier;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVariable;
import java.util.List;

public class TLAPlusSetFilter extends TLAPlusExpression {

    private List<TLAPlusExpression> children;
    private TLAPlusVariable v;
    private TLAPlusExpression set;
    private TLAPlusExpression exp;

    public TLAPlusSetFilter(TLAPlusVariable v, TLAPlusExpression set, TLAPlusExpression exp) {
        this.v = v;
        this.set = set;
        this.exp = exp;
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
