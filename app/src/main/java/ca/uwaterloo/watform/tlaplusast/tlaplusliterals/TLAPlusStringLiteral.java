package ca.uwaterloo.watform.tlaplusast.tlaplusliterals;

import ca.uwaterloo.watform.tlaplusast.TLAPlusSimpleExp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;

public class TLAPlusStringLiteral extends TLAPlusSimpleExp {

    public TLAPlusStringLiteral(String s) {
        super(TLAPlusStrings.STRING_START + s + TLAPlusStrings.STRING_END);
    }
}
