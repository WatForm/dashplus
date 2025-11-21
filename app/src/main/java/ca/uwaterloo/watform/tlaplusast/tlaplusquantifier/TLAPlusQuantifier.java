package ca.uwaterloo.watform.tlaplusast.tlaplusquantifier;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.ArrayList;
import java.util.List;

public abstract class TLAPlusQuantifier extends TLAPlusExpression {

    private final TLAPlusVariable v; // bound variable
    private final TLAPlusExpression set; // set that the iteration takes place over
    private final TLAPlusExpression exp; // expression used

    public TLAPlusQuantifier(TLAPlusVariable v, TLAPlusExpression set, TLAPlusExpression exp) {
        this.v = v;
        this.exp = exp;
        this.set = set;
    }

    public List<TLAPlusExpression> getChildren() {
        List<TLAPlusExpression> children = new ArrayList<>();
        children.add(this.v);
        children.add(this.set);
        children.add(this.exp);
        return children;
    }

    /*
    @Override
    public List<String> toStringList() {

        List<String> result = new ArrayList<>();
        result.add(this.symbol);
        result.addAll(this.children.get(0).toStringList());
        result.add(TLAPlusStrings.SET_IN);
        result.addAll(this.children.get(1).toStringList());
        result.add(TLAPlusStrings.COLON);
        result.addAll(this.children.get(2).toStringList());
        return result;
    }
     */
}
