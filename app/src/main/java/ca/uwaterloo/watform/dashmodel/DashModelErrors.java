package ca.uwaterloo.watform.dashmodel;

import ca.uwaterloo.watform.utils.ErrorFatal;
import ca.uwaterloo.watform.utils.ErrorUser;
import ca.uwaterloo.watform.utils.Pos;

public class DashModelErrors {

    // errors discovered in initializing DashModel phase
    public static void notDashModel() throws ErrorFatal {
        throw new ErrorUser("No Dash state in this model.");
    }

    public static String tooManyMsg = "Multiple ";

    public static void tooMany(Pos p, String xType, String fqn) throws ErrorFatal {
        throw new ErrorUser(p, tooManyMsg + xType + " in " + fqn);
    }

    public static void duplicateName(Pos p, String type, String fqn) throws ErrorFatal {
        throw new ErrorUser(p, fqn + "is a duplicate " + type + " name");
    }

    public static String allConcDefaultStatesMsg =
            "All conc children of state must be defaults if one is for state: ";

    public static void allAndDefaults(Pos p, String sfqn) throws ErrorFatal {
        throw new ErrorUser(p, allConcDefaultStatesMsg + sfqn);
    }

    public static String noDefaultStateMsg = "State does not have default state: ";

    public static void noDefaultState(Pos p, String fqn) throws ErrorFatal {
        throw new ErrorUser(p, noDefaultStateMsg + fqn);
    }

    public static String tooManyDefaultStatesMsg = "Too many default states in state: ";

    public static void tooManyDefaults(Pos p, String fqn) throws ErrorFatal {
        throw new ErrorUser(p, tooManyDefaultStatesMsg + fqn);
    }

    public static void duplicateStateName(Pos p, String fqn) throws ErrorFatal {
        throw new ErrorUser(p, fqn + "is a duplicate state name");
    }

    public static String nameShouldNotBePrimedMsg =
            "Declared state/trans/event/var cannot have a primed name: ";

    public static void nameShouldNotBePrimed(Pos p, String n) throws ErrorFatal {
        throw new ErrorUser(p, nameShouldNotBePrimedMsg + n);
    }

    public static String onlyOneStateMsg = "Dash model can only have one 'state' section";

    public static void onlyOneState(Pos o) throws ErrorFatal {
        throw new ErrorUser(o, onlyOneStateMsg);
    }

    public static String nameCantBeFQNMsg = "When declared, name cannot have slash: ";

    public static void nameCantBeFQN(Pos o, String name) throws ErrorFatal {
        throw new ErrorUser(o, nameCantBeFQNMsg + name);
    }

    public static String dupNameMsg = "Duplicate names: ";

    public static void dupNames(Pos o, String dups) throws ErrorFatal {
        throw new ErrorUser(o, dupNameMsg + dups);
    }

    // errors discovered in resolveExpr DashModel phase

    // parts of the code that should be unreachable

    public static String ancesNotPrefixMsg = " must be a prefix of ";

    public static void ancesNotPrefix(String a, String d) throws ErrorFatal {
        throw new ErrorFatal(a + ancesNotPrefixMsg + d);
    }

    public static String chopPrefixFromFQNwithNoPrefixMsg = "chopPrefixFromFQNwithNoPrefix: ";

    public static void chopPrefixFromFQNwithNoPrefix(String s) throws ErrorFatal {
        throw new ErrorFatal(chopPrefixFromFQNwithNoPrefixMsg + s);
    }
}
