package ca.uwaterloo.watform.tlaplusast.tlaplusliterals;

import ca.uwaterloo.watform.tlaplusast.TlaSimpleExp;

public class TlaIntLiteral extends TlaSimpleExp {

    public TlaIntLiteral(int n) {
        super(Integer.toString(n));
    }
}
