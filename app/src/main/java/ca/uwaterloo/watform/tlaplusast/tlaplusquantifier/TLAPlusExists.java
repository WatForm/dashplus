package ca.uwaterloo.watform.tlaplusast.tlaplusquantifier;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusExists extends TLAPlusQuantifier {
    public TLAPlusExists(TLAPlusVariable v, TLAPlusExpression set, TLAPlusExpression exp) {
        super(v, set, exp, TLAPlusStrings.EXISTS);
    }
}
