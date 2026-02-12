package ca.uwaterloo.watform.tlaast.tlaliterals;

import ca.uwaterloo.watform.tlaast.TlaSimpleExp;
import ca.uwaterloo.watform.tlaast.TlaStrings;

public class TlaIntSet extends TlaSimpleExp {

    /*

    Int

    this is a TLA+ constant that refers to the set of all integers
    */

    public TlaIntSet() {
        super(TlaStrings.INT_SET);
    }
}
