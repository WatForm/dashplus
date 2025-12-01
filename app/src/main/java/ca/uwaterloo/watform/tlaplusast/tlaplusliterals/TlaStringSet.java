package ca.uwaterloo.watform.tlaplusast.tlaplusliterals;

import ca.uwaterloo.watform.tlaplusast.TlaSimpleExp;
import ca.uwaterloo.watform.tlaplusast.TlaStrings;

public class TlaStringSet extends TlaSimpleExp {

    public TlaStringSet() {
        super(TlaStrings.STRING_SET);
    }
}
