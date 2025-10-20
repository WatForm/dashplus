/* string names used in Dash and conversion to Alloy */

// NADTODO make these upper case
// NADTODO where should module names go?

package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.utils.CommonStrings;
import java.util.Collections;

public final class DashStrings {

    private DashStrings() {}

    // used for translation to Alloy
    // NAD TODO this should match the qualChar used in parsing for vars and state names
    public static String inputQualChar = "/";
    public static String internalQualChar = inputQualChar;
    public static String outputQualChar = "_";

    // separator used in Alloy names
    public static final String alloySep = "_";

    // standard modules
    public static final String utilBooleanName = "util/boolean";
    public static final String boolName = "boolean/Bool";
    public static final String trueName = "boolean/True";
    public static final String falseName = "boolean/False";
    public static final String isTrue = "boolean/isTrue";
    public static final String isFalse = "boolean/isFalse";

    // public static final String utilOrderingName = "util/ordering";
    public static final String utilTracesName = "util/traces";
    public static final String tracesFirstName = "first";
    public static final String tracesNextName = "next";
    public static final String tracesLastName = "last";
    public static final String tracesBackName = "back";

    public static final String utilBufferName = "util/buffer";

    public static final String utilTcmcPathName = "util/tcmc";
    public static final String tcmcInitialStateName = "tcmc/ks_s0";
    public static final String tcmcSigmaName = "tcmc/ks_sigma";

    // Dash input keywords
    // used for printing: parts of Dash syntax
    // must be in sync with Dash-cup-symbols.txt
    public static final String stateName = "state";
    public static final String concName = "conc";
    public static final String defaultName = "default";
    public static final String eventName = "event";
    public static final String envName = "env";
    public static final String bufName = "buf";
    public static final String transName = "trans";
    public static final String fromName = "from";
    public static final String onName = "on";
    public static final String whenName = "when";
    public static final String doName = "do";
    public static final String gotoName = "goto";
    public static final String sendName = "send";
    public static final String invName = "inv";
    public static final String enterName = "enter";
    public static final String exitName = "exit";
    public static final String initName = "init";

    // user must be aware of this name
    public static final String bufferIndexName = "bufIdx";

    public static final String dsh_prefix = "dsh_";
    // init is a reserved word in Dash
    public static final String initFactName = dsh_prefix + "initial";
    public static final String electrumInitName = dsh_prefix + "init";
    // predicate names

    public static final String smallStepName = dsh_prefix + "small_step";
    public static final String stableName = dsh_prefix + "stable";
    public static final String stutterName = dsh_prefix + "stutter";
    public static final String strongNoStutterName = dsh_prefix + "strong_no_stutter";
    // public static final String equalsName = "equals";
    public static final String isEnabled = dsh_prefix + "isEnabled";
    public static final String tracesFactName = dsh_prefix + "traces_fact";
    public static final String electrumFactName = dsh_prefix + "electrum_fact";
    public static final String tcmcFactName = dsh_prefix + "tcmc_fact";
    public static final String singleEventName = dsh_prefix + "single_event";
    public static final String reachabilityName = dsh_prefix + "reachability";
    public static final String enoughOperationsName = dsh_prefix + "enough_operations";
    public static final String completeBigStepsName = dsh_prefix + "complete_big_steps";
    /* names used in Dash translation */
    // sig names
    public static final String DshPrefix = "Dsh";
    public static final String snapshotName = DshPrefix + "Snapshot";
    public static final String allEventsName = DshPrefix + "Events";
    public static final String allEnvironmentalEventsName = DshPrefix + "EnvEvents";
    public static final String allInternalEventsName = DshPrefix + "IntEvents";
    public static final String variablesName = DshPrefix + "Vars";
    public static final String stateLabelName = DshPrefix + "States";
    public static final String scopeLabelName = DshPrefix + "Scopes";
    // public static final String systemStateName = "SystemState";
    public static final String transitionLabelName = "Transitions";
    // 2024-02-21 NAD we can just set the set of transitions to empty
    // public static final String noTransName = "NO_TRANS";
    public static final String identifierName = DshPrefix + "Ids";
    public static final String bufferName = DshPrefix + "Buffer";
    public static final String scopeSuffix = "Scope";

    // field names
    public static final String confName = dsh_prefix + "conf";
    public static final String scopesUsedName = dsh_prefix + "sc_used";
    public static final String eventsName = dsh_prefix + "events";
    public static final String transTakenName = dsh_prefix + "taken";
    // predicate names
    // public static final String tName = "dsh_t";
    public static final String preName = "_pre";
    public static final String postName = "_post";
    // public static final String semanticsName = "_semantics";
    public static final String testIfNextStableName = "_nextIsStable";
    public static final String enabledAfterStepName = "_enabledAfterStep";
    public static final String allSnapshotsDifferentName = "allSnapshotsDifferent";
    // variable/parameter names
    // how to name parameter variables
    public static final String curName = "s";
    public static final String nextName = "sn";
    public static final String pName = "p";
    public static final String genEventName = "genEvs";
    public static final String scopeName = "sc";
    public static final String randomParamExt = "_aa";

    // strings used internally
    public static final String processRef = "$$PROCESSREF$$";
    public static final String SLASH = "/";
    public static final String PRIME = "'";

    public static final String prime(final String a) {
        return a + "'";
    }
    ;

    public static enum IntEnvKind {
        INT,
        ENV
    }

    public static enum StateKind {
        AND,
        OR
        // basic is determined if no children
    }

    // this distinct is only used at parsing
    // within StateTable the default of a state is final String name
    // or null
    public static enum DefKind {
        DEFAULT,
        NOTDEFAULT
    }

    public static boolean hasPrime(final String s) {
        return (s.substring(s.length() - 1, s.length()).equals(PRIME));
    }

    public static final String removePrime(final String s) {
        if (hasPrime(s)) return s.substring(0, s.length() - 1);
        else return s;
    }

    public static final String indent(Integer i) {
        return String.join("", Collections.nCopies(i, CommonStrings.TAB));
    }
}
