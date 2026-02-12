package ca.uwaterloo.watform.tlaast.tlaliterals;

import ca.uwaterloo.watform.tlaast.TlaSimpleExp;
import ca.uwaterloo.watform.tlaast.TlaStrings;

public class TlaStringSet extends TlaSimpleExp {

    /*

    STRING

    this is a TLA+ constant that refers to the set of all strings
    */

    public TlaStringSet() {
        super(TlaStrings.STRING_SET);
    }
}
