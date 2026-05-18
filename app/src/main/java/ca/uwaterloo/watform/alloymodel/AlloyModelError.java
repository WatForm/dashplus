package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.utils.*;
import java.util.List;

public final class AlloyModelError extends UserOrImplError {

    private AlloyModelError(String msg) {
        super(msg);
    }

    private AlloyModelError(Pos pos, String msg) {
        super(pos, msg);
    }

    private AlloyModelError(List<Pos> posList, String msg) {
        super(posList, msg);
    }

    // errors

    public static AlloyModelError unknownArity(Pos p, String s) {
        return new AlloyModelError(p, "Unknown arity: " + s);
    }

    public static AlloyModelError wrongNumberArgs(Pos p, String s) {
        return new AlloyModelError(p, "Wrong number of arguments to predicate: " + s);
    }

    public static AlloyModelError duplicateName(Pos pos1, String name) {
        return new AlloyModelError(pos1, "Duplicate name: " + name);
    }

    public static AlloyModelError duplicateFieldName(Pos pos1, String name) {
        return new AlloyModelError(pos1, "Duplicate field name: " + name);
    }

    public static AlloyModelError duplicateSigName(Pos pos1, String name) {
        return new AlloyModelError(pos1, "Duplicate sig name: " + name);
    }

    public static AlloyModelError duplicatePredName(Pos pos, String name) {
        return new AlloyModelError(pos, "Duplicate pred name: " + name);
    }

    public static AlloyModelError sigNameIsFieldName(Pos pos1, String name) {
        return new AlloyModelError(pos1, "Field name the same as sig name: " + name);
    }

    public static AlloyModelError paraDNE(String name) {
        return new AlloyModelError("The paragraph with " + name + " does not exist. ");
    }

    public static AlloyModelError moduleMustBeUnique(Pos pos1, Pos pos2) {
        return new AlloyModelError(
                List.of(pos1, pos2), "AlloyModel can only contain one Module declaration: ");
    }

    public static AlloyModelError arityMismatch(Pos pos, String leftExpr, String rightExpr) {
        return new AlloyModelError(
                pos, "Arity mismatch between: " + leftExpr + " and " + rightExpr);
    }

    public static AlloyModelError mustBeFormula(Pos pos, String expr) {
        return new AlloyModelError(pos, "Must be formula: " + expr);
    }

    public static AlloyModelError mustBeBinary(Pos pos, String expr) {
        return new AlloyModelError(pos, "Must be binary: " + expr);
    }

    public static AlloyModelError mustBeUnary(Pos pos, String expr) {
        return new AlloyModelError(pos, "Must be binary: " + expr);
    }

    public static AlloyModelError sigsInCycle(String sigs) {
        return new AlloyModelError("Sigs in a cycle " + sigs);
    }

    public static AlloyModelError mulOfDeclMustBeOne(Pos pos, String decl) {
        return new AlloyModelError(pos, "Must be multiplicity of `one`: " + decl);
    }

    public static AlloyModelError noMulGivenAndCannotBeCalculated(Pos pos, String expr) {
        return new AlloyModelError(
                pos, "No mul given and cannot be calculated: " + expr.toString());
    }
}
