/* string names used in Dash and conversion to Alloy */

// NADTODO make these upper case
// NADTODO where should module names go?

package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.utils.CommonStrings;
import java.util.Collections;

public final class DashStrings extends CommonStrings {

    private DashStrings() {}

    // NAD TODO this should match the qualChar used in parsing for vars and state names
    public static String inputQualChar = "/";
    public static String internalQualChar = inputQualChar;
    public static String outputQualChar = "_";

    // separator used in Alloy names
    public static final String alloySep = "_";

    public static final String pName = "p";
    public static final String SLASH = "/";
    public static final String PRIME = "'";

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

    // generally in the code we know the kind by context but
    // for printing we need the kind here
    // and this simplified some code for the DashRef to know its kind
    public static enum DashRefKind {
        STATE,
        EVENT,
        VAR,
        TRANS
        // BUFFER ????
    }

    public static boolean hasPrime(final String s) {
        return (s.substring(s.length() - 1, s.length()).equals(PRIME));
    }

    public static final String indent(Integer i) {
        return String.join("", Collections.nCopies(i, CommonStrings.TAB));
    }
}
