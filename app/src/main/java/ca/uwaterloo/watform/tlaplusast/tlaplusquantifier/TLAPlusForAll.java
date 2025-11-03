package ca.uwaterloo.watform.tlaplusast.tlaplusquantifier;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusForAll extends TLAPlusQuantifier {
    public TLAPlusForAll(TLAPlusVariable v, TLAPlusExpression set, TLAPlusExpression exp) {
        super(v, set, exp);
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        return;
        // TODO fix this
    }
}
