package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.ArrayList;
import java.util.List;

public abstract class TLAPlusQuantificationOperator extends TLAPlusOperator {

    private final TLAPlusVariable v; // bound variable
    private final TLAPlusExpression set; // set that the iteration takes place over
    private final TLAPlusExpression exp; // expression used

    public TLAPlusQuantificationOperator(
            TLAPlusVariable v,
            TLAPlusExpression set,
            TLAPlusExpression exp,
            TLAPlusOperator.PrecedenceGroup precedenceGroup) {
        super(TLAPlusOperator.Associativity.IRRELEVANT, precedenceGroup);
        this.v = v;
        this.exp = exp;
        this.set = set;
    }

    public TLAPlusExpression getSet() {
        return this.set;
    }

    public TLAPlusVariable getV() {
        return this.v;
    }

    public TLAPlusExpression getExp() {
        return this.exp;
    }

    public List<TLAPlusExpression> getChildren() {
        List<TLAPlusExpression> children = new ArrayList<>();
        children.add(this.v);
        children.add(this.set);
        children.add(this.exp);
        return children;
    }
}
