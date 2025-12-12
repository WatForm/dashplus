// these are common errors across multiple files

package ca.uwaterloo.watform.dashmodel;

import ca.uwaterloo.watform.utils.*;

public class DashModelErrors {

    // errors discovered in initializing DashModel phase
    public static void duplicateName(Pos pos, String type, String fqn) throws Reporter.ErrorUser {
        throw new Reporter.ErrorUser(pos, fqn + "is a duplicate " + type + " name");
    }

    public static void nameShouldNotBePrimed(Pos pos, String n) throws Reporter.ErrorUser {
        throw new Reporter.ErrorUser(
                pos, "Declared state/trans/event/var cannot have a primed name: " + n);
    }

    // parts of the code that should be unreachable

    public static String ancesNotPrefixMsg = " must be a prefix of ";

    public static void ancesNotPrefix(String a, String d) throws ImplementationError {
        throw new ImplementationError(a + ancesNotPrefixMsg + d);
    }

    public static String chopPrefixFromFQNwithNoPrefixMsg = "chopPrefixFromFQNwithNoPrefix: ";

    public static void chopPrefixFromFQNwithNoPrefix(String s) throws ImplementationError {
        throw new ImplementationError(chopPrefixFromFQNwithNoPrefixMsg + s);
    }
}
