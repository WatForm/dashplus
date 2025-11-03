package ca.uwaterloo.watform.tlaplusast.tlaplusquantifier;

import ca.uwaterloo.watform.tlaplusast.*;

public abstract class TLAPlusQuantifier extends TLAPlusExpression {

    private String symbol;
    private TLAPlusVariable v;
    private TLAPlusExpression set;
    private TLAPlusExpression exp;

    public TLAPlusQuantifier(
            TLAPlusVariable v, TLAPlusExpression set, TLAPlusExpression exp, String symbol) {
        this.v = v;
        this.exp = exp;
        this.set = set;
        this.symbol = symbol;
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
