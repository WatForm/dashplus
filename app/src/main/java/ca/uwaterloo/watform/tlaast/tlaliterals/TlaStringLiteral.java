package ca.uwaterloo.watform.tlaast.tlaliterals;

import ca.uwaterloo.watform.tlaast.TlaSimpleExp;
import ca.uwaterloo.watform.tlaast.TlaStrings;

public class TlaStringLiteral extends TlaSimpleExp {

    public TlaStringLiteral(String s) {
        super(TlaStrings.STRING_START + s + TlaStrings.STRING_END);
    }
}
