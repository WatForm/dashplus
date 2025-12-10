package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaUnionSet;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaFalse;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaTrue;
import ca.uwaterloo.watform.tlaast.tlanaryops.TlaAndList;
import ca.uwaterloo.watform.tlaast.tlanaryops.TlaOrList;
import ca.uwaterloo.watform.tlaast.tlanaryops.TlaSet;
import java.util.ArrayList;
import java.util.List;

class DashToTlaStrings {
    // this class stores information about things that are common to every part of the translation

    public static final String QUALIFIER = DashStrings.SLASH;
    // this is used as a separator for DashFQNs

    public static final String SPECIAL = "_";
    // special character used for naming translation artefacts
    // this scheme has flaws, TODO: come up with a better scheme

    public static final String TRANSITIONS = SPECIAL + "transitions";
    public static final String TAKEN = SPECIAL + "taken";
    public static final String PRE = SPECIAL + "pre";
    public static final String POST = SPECIAL + "post";
    public static final String ENABLED = SPECIAL + "enabled";

    // common variables
    public static final String CONF = SPECIAL + "conf";
    public static final String EVENTS = SPECIAL + "events";
    public static final String TRANS_TAKEN = SPECIAL + "trans" + SPECIAL + "taken";
    public static final String SCOPES_USED = SPECIAL + "scopes" + SPECIAL + "used";
    public static final String STABLE = SPECIAL + "stable";

    // common formulae
    public static final String INIT = SPECIAL + "Init";
    public static final String NEXT = SPECIAL + "Next";
    public static final String TYPE_OK = SPECIAL + "TypeOK";
    public static final String STUTTER = SPECIAL + "stutter";
    public static final String SMALL_STEP = SPECIAL + "small" + SPECIAL + "step";
    public static final String SOME_TRANSITION = SPECIAL + "some" + SPECIAL + "transition";
    public static final String SOME_PRE_TRANSITION =
            SPECIAL + "some" + PRE + SPECIAL + "transition";
    public static final String NEXT_IS_STABLE =
            SPECIAL + "next" + SPECIAL + "is" + SPECIAL + "stable";

    public static final String ARG = SPECIAL + "arg"; // used as a prefix for parameter variables
    public static final String ALL = SPECIAL + "all"; // used as a prefix for type formulae

    public static final String parameterVariable(String varName) {
        return ARG + varName;
    }

    public static final String typeFormula(String varName) {
        return ALL + varName;
    }

    public static final TlaSet NULL_SET = new TlaSet(new ArrayList<>());

    public static String tlaFQN(String dashFQN) {
        return SPECIAL + dashFQN.replace(QUALIFIER, SPECIAL);
    }

    public static String takenTransTlaFQN(String transitionFQN) {
        return TAKEN + tlaFQN(transitionFQN);
    }

    public static String preTransTlaFQN(String transitionFQN) {
        return PRE + tlaFQN(transitionFQN);
    }

    public static String postTransTlaFQN(String transitionFQN) {
        return POST + tlaFQN(transitionFQN);
    }

    public static String enabledTransTlaFQN(String transitionFQN) {
        return ENABLED + tlaFQN(transitionFQN);
    }

    public static TlaExp repeatedUnion(List<? extends TlaExp> operands) {
        int n = operands.size();
        if (n == 0) return NULL_SET;
        return foldLeft(operands.subList(1, n), TlaUnionSet::new, operands.get(0));
    }

    public static TlaExp repeatedAnd(List<? extends TlaExp> operands) {
        if (operands.size() == 0) return new TlaTrue();
        return new TlaAndList(operands);
    }

    public static TlaExp repeatedOr(List<? extends TlaExp> operands) {
        if (operands.size() == 0) return new TlaFalse();
        return new TlaOrList(operands);
    }
}
