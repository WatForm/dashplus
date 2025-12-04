package ca.uwaterloo.watform.tlaast.tlaliterals;

import ca.uwaterloo.watform.tlaast.TlaSimpleExp;
import ca.uwaterloo.watform.tlaast.TlaStrings;

public class TlaLiteral extends TlaSimpleExp {

    public TlaLiteral(String s) {
        super(TlaStrings.STRING_START + s + TlaStrings.STRING_END);
    }
}
