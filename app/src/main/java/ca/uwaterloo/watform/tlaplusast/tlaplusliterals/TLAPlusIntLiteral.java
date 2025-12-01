package ca.uwaterloo.watform.tlaplusast.tlaplusliterals;

import ca.uwaterloo.watform.tlaplusast.TlaSimpleExp;

public class TLAPlusIntLiteral extends TlaSimpleExp {

    public TLAPlusIntLiteral(int n) {
        super(Integer.toString(n));
    }
}
