package ca.uwaterloo.watform.dashtotla;

import ca.uwaterloo.watform.dashast.DashStrings;

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

    // common formulae without arguments
    public static final String INIT = SPECIAL + "Init";
    public static final String NEXT = SPECIAL + "Next";
    public static final String TYPE_OK = SPECIAL + "TypeOK";
    public static final String STUTTER = SPECIAL + "stutter";
    public static final String SMALL_STEP = SPECIAL + "small" + SPECIAL + "step";
    public static final String SOME_TRANSITION = SPECIAL + "some" + SPECIAL + "transition";
    public static final String SOME_PRE_TRANSITION =
            SPECIAL + "some" + PRE + SPECIAL + "transition";
    public static final String NONE_TRANSITION = SPECIAL + "none" + SPECIAL + "transition";
    public static final String INTERNAL_EVENTS = SPECIAL + "internal" + SPECIAL + "events";
    public static final String ENVIRONMENTAL_EVENTS =
            SPECIAL + "environmental" + SPECIAL + "events";

    // common formulae with arguments
    public static final String NEXT_IS_STABLE =
            SPECIAL + "next" + SPECIAL + "is" + SPECIAL + "stable";

    public static final String ARG = SPECIAL + "arg"; // used as a prefix for parameter variables
    public static final String ALL = SPECIAL + "all"; // used as a prefix for type formulae

    public static final String NONE_TRANSITION_LITERAL = "[none]";
}
