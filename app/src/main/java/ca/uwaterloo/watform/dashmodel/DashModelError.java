// these are common errors across multiple files

package ca.uwaterloo.watform.dashmodel;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class DashModelError extends UserOrImplError {

    private DashModelError(String msg) {
        super(msg);
    }

    private DashModelError(Pos pos, String msg) {
        super(pos, msg);
    }

    private DashModelError(List<Pos> posList, String msg) {
        super(posList, msg);
    }

    // errors discovered in initializing DashModel phase
    public static DashModelError duplicateName(Pos pos, String type, String fqn) {
        return new DashModelError(pos, fqn + "is a duplicate " + type + " name");
    }

    public static DashModelError nameShouldNotBePrimed(Pos pos, String n) {
        return new DashModelError(
                pos, "Declared state/trans/event/var cannot have a primed name: " + n);
    }

    // parts of the code that should be unreachable

    public static DashModelError unsupportedExpr(Pos pos, String cls, String n) {
        return new DashModelError(
                pos, "Expression not supported in Dash: " + n + " which is " + cls);
    }

    // InitializeDM

    private static DashModelError notDashModel() {
        return new DashModelError("No Dash state in this model.");
    }

    public static DashModelError allAndDefaults(Pos pos, String sfqn) {
        return new DashModelError(
                pos, "All conc children of state must be defaults if one is a default: " + sfqn);
    }

    public static DashModelError missingDefault(Pos pos, String fqn) {
        return new DashModelError(pos, "State does not have default state: " + fqn);
    }

    public static DashModelError tooManyDefaults(Pos pos, String fqn) {
        return new DashModelError(pos, "Too many default states in state: " + fqn);
    }

    public static DashModelError duplicateStateName(Pos pos, String fqn) {
        return new DashModelError(pos, fqn + "is a duplicate state name");
    }

    public static DashModelError onlyOneState(Pos pos) {
        return new DashModelError(pos, "Dash model can only have one 'state' section");
    }

    public static DashModelError nameCantBeFQN(Pos pos, String name) {
        return new DashModelError(pos, "When declared, name cannot have slash: " + name);
    }

    public static DashModelError dupNames(Pos pos, String dups) {
        return new DashModelError(pos, "Duplicate names: " + dups);
    }

    // ResolveDM Errorw
    /*
    public static DashModelError unknownError(DashStrings.DashRefKind kind, AlloyExpr expr) {
        if (kind == DashStrings.DashRefKind.STATE)
            return new DashModelError(expr.pos, "state does not exist: " + expr.toString());
        else if (kind == DashStrings.DashRefKind.EVENT)
            return new DashModelError(expr.pos, "event does not exist: " + expr.toString());
        else return new DashModelError(expr.pos, "variable does not exist: " + expr.toString());
    }
    */
    public static DashModelError wrongNumberParamsError(AlloyExpr expr) {
        return new DashModelError(expr.pos, "Incorrect number of parameters: " + expr.toString());
    }

    public static DashModelError ambiguousRefError(AlloyExpr expr) {
        return new DashModelError(expr.pos, " Name not unique: " + expr.toString());
    }

    public static DashModelError unknownElementWithParamsError(AlloyExpr expr) {
        return new DashModelError(
                expr.pos, " " + "Unknown Dash element with params: " + expr.toString());
    }

    public static DashModelError cantNextNonVarError(AlloyExpr expr) {
        return new DashModelError(
                expr.pos, " " + " Non-var/buffer cannot be primed: " + expr.toString());
    }

    public static DashModelError unknownSrcDestError(String x, String t, String tfqn) {
        return new DashModelError(
                "Src/Dest of trans is unknown: " + "trans " + tfqn + " " + t + " " + x);
    }

    public static DashModelError cantNextEnvVarBufError(AlloyExpr expr) {
        return new DashModelError(expr.pos, " Env var/buffer cannot be primed: " + expr.toString());
    }

    public static DashModelError noNextVarsError(AlloyExpr expr) {
        return new DashModelError(
                expr.pos, "Primed variables are not allowed in: " + expr.toString());
    }

    public static DashModelError cantNextNonDynamicVarError(AlloyExpr expr) {
        return new DashModelError(
                expr.pos,
                "Cannot prime something that is not a dynamic variable: " + expr.toString());
    }

    public static DashModelError ambiguousUseOfThisError(AlloyExpr expr) {
        return new DashModelError(expr.pos, "Ambiguous use of 'this' " + expr.toString());
    }

    public static DashModelError nonParamUseOfThisError(AlloyExpr expr) {
        return new DashModelError(
                expr.pos, " 'this' must refer to a parametrized state: " + expr.toString());
    }

    public static DashModelError lookupOfNonExistentEvent(String ev) {
        return new DashModelError("lookup of event that does not exist: " + ev);
    }
}
