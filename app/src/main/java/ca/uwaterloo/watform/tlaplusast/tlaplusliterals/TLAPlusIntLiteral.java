package ca.uwaterloo.watform.tlaplusast.tlaplusliterals;

import ca.uwaterloo.watform.tlaplusast.TLAPlusSimpleExp;

public class TLAPlusIntLiteral extends TLAPlusSimpleExp {

    public TLAPlusIntLiteral(int n) {
        super(Integer.toString(n));
    }
}
