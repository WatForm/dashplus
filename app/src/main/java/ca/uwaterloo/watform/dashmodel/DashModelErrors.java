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

    public static void unsupportedExpr(Pos pos, String cls, String n) throws Reporter.ErrorUser {
        throw new Reporter.ErrorUser(
                pos, "Expression not supported in Dash: " + n + " which is " + cls);
    }
}
