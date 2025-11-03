package ca.uwaterloo.watform.tlaplusast.tlaplusquantifier;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusExists extends TLAPlusQuantifier {
    public TLAPlusExists(TLAPlusVariable v, ASTNode set, ASTNode exp) {
        super(v, set, exp, TLAPlusStrings.EXISTS);
    }
}
