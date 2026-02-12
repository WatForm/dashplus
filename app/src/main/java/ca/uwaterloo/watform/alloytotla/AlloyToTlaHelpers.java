package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaVar;

public class AlloyToTlaHelpers {

    public static String sigSet(String sigName) {
        return sigName + SIG_SET_SUFFIX;
    }

    public static String sigConstraint(String sigName) {
        return sigName + SIG_CONSTRAINT_SUFFIX;
    }

    public static TlaVar fieldVar(String fieldName, String sigName) {
        return new TlaVar(sigName + SPECIAL + fieldName);
    }

    public static String unnamedFact(int n) {
        return UNNAMED_FACT_PREFIX + n;
    }

    public static TlaAppl SIG_SETS_PRIMED() {
        return TlaAppl(SIG_SETS_PRIMED);
    }

    public static TlaAppl SIG_SETS_UNPRIMED() {
        return TlaAppl(SIG_SETS_UNPRIMED);
    }
}
