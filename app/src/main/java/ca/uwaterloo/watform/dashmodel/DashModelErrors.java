package ca.uwaterloo.watform.dashmodel;

import ca.uwaterloo.watform.utils.ErrorFatal;
import ca.uwaterloo.watform.utils.ErrorUser;
import ca.uwaterloo.watform.utils.Pos;

public class DashModelErrors {

    // these are common errors across multiple files

    // errors discovered in initializing DashModel phase
    public static void duplicateName(Pos pos, String type, String fqn) throws ErrorFatal {
        throw new ErrorUser(pos, fqn + "is a duplicate " + type + " name");
    }

    public static void nameShouldNotBePrimed(Pos pos, String n) throws ErrorFatal {
        throw new ErrorUser(pos, "Declared state/trans/event/var cannot have a primed name: " + n);
    }

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
