package ca.uwaterloo.watform.tlaast.tlaliterals;

import ca.uwaterloo.watform.tlaast.TlaSimpleExp;
import ca.uwaterloo.watform.tlaast.TlaStrings;

public class TlaStringSet extends TlaSimpleExp {

    public TlaStringSet() {
        super(TlaStrings.STRING_SET);
    }
}
