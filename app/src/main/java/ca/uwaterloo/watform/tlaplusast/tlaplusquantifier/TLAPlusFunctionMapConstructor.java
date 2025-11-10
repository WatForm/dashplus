package ca.uwaterloo.watform.tlaplusast.tlaplusquantifier;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVariable;

public class TLAPlusFunctionMapConstructor extends TLAPlusQuantifier {
    public TLAPlusFunctionMapConstructor(
            TLAPlusVariable v, TLAPlusExpression set, TLAPlusExpression exp) {
        super(v, set, exp);
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        return;
        // TODO fix this
    }
}
