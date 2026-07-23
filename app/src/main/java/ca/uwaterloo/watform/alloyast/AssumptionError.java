/*
    All places where we don't support something that might be supported in AA
*/

package ca.uwaterloo.watform.alloyast;

import ca.uwaterloo.watform.utils.*;
import java.util.List;

public class AssumptionError extends UserOrImplError {

    private AssumptionError(String msg) {
        super(msg);
    }

    private AssumptionError(List<Pos> posList, String msg) {
        super(posList, msg);
    }

    private AssumptionError(Pos pos, String msg) {
        super(pos, msg);
    }

    public static AssumptionError cantHaveMultipleCmdDecls(Pos p, String s) {
        return new AssumptionError(p, "Can't have multiple cmds within cmd: " + s);
    }

    public static AssumptionError cantHaveScopeRange(Pos p, String s) {
        return new AssumptionError(p, "Can't have scope range in command: " + s);
    }

    public static AssumptionError macrosNotSupported(Pos p) {
        return new AssumptionError(p, "Macros are not supported");
    }

    public static AssumptionError missingAssertName(Pos p) {
        return new AssumptionError(p, "Assertion names must be unqualified strings");
    }

    public static AssumptionError atNotAllowed(Pos p, String s) {
        return new AssumptionError(p, "@ not allowed here: " + s);
    }

    public static AssumptionError thisNotAllowed(Pos p, String s) {
        return new AssumptionError(p, "'this' not allowed here: " + s);
    }

    public static AssumptionError cmdNameMustBeUnique(Pos p, String s) {
        return new AssumptionError(p, "command name must be unique: " + s);
    }
}
