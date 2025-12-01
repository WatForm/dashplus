package ca.uwaterloo.watform.tlaplusast.tlaplusliterals;

import ca.uwaterloo.watform.tlaplusast.TlaSimpleExp;
import ca.uwaterloo.watform.tlaplusast.TlaStrings;

public class TlaLiteral extends TlaSimpleExp {

    public TlaLiteral(String s) {
        super(TlaStrings.STRING_START + s + TlaStrings.STRING_END);
    }
}
