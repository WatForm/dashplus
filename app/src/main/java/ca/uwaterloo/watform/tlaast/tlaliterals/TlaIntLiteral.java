package ca.uwaterloo.watform.tlaast.tlaliterals;

import ca.uwaterloo.watform.tlaast.TlaSimpleExp;

public class TlaIntLiteral extends TlaSimpleExp {

    public TlaIntLiteral(int n) {
        super(Integer.toString(n));
    }
}
