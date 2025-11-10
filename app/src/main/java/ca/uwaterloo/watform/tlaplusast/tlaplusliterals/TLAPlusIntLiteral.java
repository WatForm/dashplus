package ca.uwaterloo.watform.tlaplusast.tlaplusliterals;

import ca.uwaterloo.watform.tlaplusast.TLAPlusSimpleExpression;

public class TLAPlusIntLiteral extends TLAPlusSimpleExpression {

    public TLAPlusIntLiteral(int n) {
        super(Integer.toString(n));
    }
}
