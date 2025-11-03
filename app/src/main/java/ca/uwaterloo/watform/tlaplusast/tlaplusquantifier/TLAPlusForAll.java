package ca.uwaterloo.watform.tlaplusast.tlaplusquantifier;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusForAll extends TLAPlusQuantifier {
    public TLAPlusForAll(TLAPlusVariable v, ASTNode set, ASTNode exp) {
        super(v, set, exp, TLAPlusStrings.FOR_ALL);
    }
}
