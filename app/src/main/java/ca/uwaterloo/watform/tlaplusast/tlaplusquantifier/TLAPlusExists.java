package ca.uwaterloo.watform.tlaplusast.tlaplusquantifier;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusExists extends TLAPlusQuantifier {
    public TLAPlusExists(TLAPlusVariable v, TLAPlusExpression set, TLAPlusExpression exp) {
        super(v, set, exp);
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        return;
        // TODO fix this
    }
}
