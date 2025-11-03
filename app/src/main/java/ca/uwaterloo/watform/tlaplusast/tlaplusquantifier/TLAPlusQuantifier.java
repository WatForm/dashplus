package ca.uwaterloo.watform.tlaplusast.tlaplusquantifier;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;

public abstract class TLAPlusQuantifier extends TLAPlusExpression {

    private String symbol;

    public TLAPlusQuantifier(TLAPlusVariable v, ASTNode set, ASTNode exp, String symbol) {
        this.children = new ArrayList<>();
        this.children.add(v);
        this.children.add(set);
        this.children.add(exp);
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
