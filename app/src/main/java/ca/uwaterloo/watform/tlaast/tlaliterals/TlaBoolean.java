package ca.uwaterloo.watform.tlaast.tlaliterals;

import ca.uwaterloo.watform.tlaast.TlaSimpleExp;
import ca.uwaterloo.watform.tlaast.TlaStrings;

public class TlaBoolean extends TlaSimpleExp {

    /*
    BOOLEAN

    (this is a constant that is equal to {TRUE,FALSE})
    */

    public TlaBoolean() {
        super(TlaStrings.BOOLEAN);
    }
}
