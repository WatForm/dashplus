package ca.uwaterloo.watform.tlaplusast.tlaplusquantifier;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusForAll extends TLAPlusQuantifier {
    public TLAPlusForAll(TLAPlusVariable v, TLAPlusExpression set, TLAPlusExpression exp) {
        super(v, set, exp, TLAPlusStrings.FOR_ALL);
    }
}
