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

    public static AlloyModelError wrongNumberArgs(Pos p, String s, Integer i, Integer j) {
        return new AlloyModelError(
                p,
                "Wrong number of arguments to predicate/function (expected "
                        + Integer.toString(i)
                        + "): "
                        + s
                        + " (is "
                        + Integer.toString(j)
                        + ")");
    }

    public static AlloyModelError duplicateName(Pos pos1, String name) {
        return new AlloyModelError(pos1, "Duplicate name: " + name);
    }

    public static AlloyModelError duplicateFieldNameInSig(Pos pos1, String name) {
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

    public static AlloyModelError fieldBoundingExprCanContainOnlySigsAndCurrentFieldNames(
            Pos pos, String expr) {
        return new AlloyModelError(
                pos,
                "Field bounding expression can contain only sigs and fields of this sig: " + expr);
    }

    public static AlloyModelError recursiveBoundingExpr(Pos pos, String expr) {
        return new AlloyModelError(pos, "Recursive bounding expr: " + expr);
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

    public static AlloyModelError arityMismatchPredFunCall(
            Pos pos, String predFunCall, String expr, Integer expectedArity, Integer arity) {
        return new AlloyModelError(
                pos,
                "Arity mismatch in arg (expecting arity "
                        + Integer.toString(expectedArity)
                        + ") to pred/fun "
                        + predFunCall
                        + ": "
                        + expr
                        + " (has arity: "
                        + Integer.toString(arity)
                        + ")");
    }

    public static AlloyModelError arityMismatchReturnType(
            Pos pos, Integer returnTypeArity, Integer bodyArity) {
        return new AlloyModelError(
                pos,
                "Arity of return type ("
                        + Integer.toString(returnTypeArity)
                        + ") does not match arity of body ("
                        + Integer.toString(bodyArity)
                        + ")");
    }

    public static AlloyModelError missingArgsToPredFunCall(Pos pos, String expr) {
        return new AlloyModelError(pos, "Missing args to pred/fun call: " + expr);
    }

    public static AlloyModelError mustBeFormula(Pos pos, String expr) {
        return new AlloyModelError(pos, "Must be formula: " + expr);
    }

    public static AlloyModelError mustBeBinary(Pos pos, String expr) {
        return new AlloyModelError(pos, "Must be binary: " + expr);
    }

    public static AlloyModelError mustBeUnary(Pos pos, String expr) {
        return new AlloyModelError(pos, "Must be unary: " + expr);
    }

    public static AlloyModelError mustBeDotOrBoxJoin(Pos pos, String expr) {
        return new AlloyModelError(pos, "Must be dot or box join: " + expr);
    }

    public static AlloyModelError predFunNeedsArgs(Pos pos, String expr) {
        return new AlloyModelError(pos, "pred/fun needs args: " + expr);
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

    public static AlloyModelError cannotRefFieldInBoundingExprOutsideOfItsSig(
            String symbolName, String sigParent) {
        return new AlloyModelError(
                "Cannot reference: "
                        + symbolName
                        + " in a bounding expression outside of its signature: "
                        + sigParent);
    }

    public static AlloyModelError nonOneScopeForOneSig(Pos pos, String cmd) {
        return new AlloyModelError(pos, "Scopes for one sigs must be one: " + cmd);
    }

    public static AlloyModelError cantSetScopeOfInChild(Pos pos, String cmd) {
        return new AlloyModelError(pos, "Can't set scope of 'in' child: " + cmd);
    }

    public static AlloyModelError scopeOfTopLevelSigMustBeGiven(Pos pos, String cmd) {
        return new AlloyModelError(
                pos,
                "Can't set scope of 'extends' child when no scope for its top-level sig: " + cmd);
    }

    public static AlloyModelError subsetSigsCannotBeAbstrast(Pos pos, String sigPara) {
        return new AlloyModelError(pos, "Subset sigs cannot be abstract: " + sigPara);
    }

    public static AlloyModelError cantExtendSubsetSig(Pos pos, String sigName) {
        return new AlloyModelError(pos, "Subset sigs cannot be extended: " + sigName);
    }

    public static AlloyModelError importArgsNumDoesntMatch(Pos pos, String importPara) {
        return new AlloyModelError(
                pos, "Number of args to 'open' module does not match: " + importPara);
    }

    public static AlloyModelError orderedOnlyOnTopLevelSigs(String sigName) {
        return new AlloyModelError(
                "Ordering module can only be applied to top-level sigs: " + sigName);
    }

    public static AlloyModelError cantAtNonFieldOfThisSig(String fieldName) {
        return new AlloyModelError("Can't @ non-field of this sig: " + fieldName);
    }

    public static AlloyModelError assertNameMustBeUnique(Pos pos, String name) {
        return new AlloyModelError(pos, "Assert name must be unique in namespace: " + name);
    }

    public static AlloyModelError expectValueZeroOrOne(Pos pos) {
        return new AlloyModelError(pos, "Command expect value must be 0 or 1");
    }

    public static AlloyModelError argToImportMustBeSigs(Pos pos, String s) {
        return new AlloyModelError(pos, "arg to open must be a sig: " + s);
    }

    public static AlloyModelError argToImportMustBeUnique(Pos pos, String s) {
        return new AlloyModelError(pos, "arg to open must be a unique sig: " + s);
    }

    public static AlloyModelError multipleScopeValuesForSameSig(Pos pos, String s) {
        return new AlloyModelError(pos, "duplicated sig in command: " + s);
    }

    public static AlloyModelError nameCouldBeMultipleSigs(Pos pos, String s) {
        return new AlloyModelError(pos, "name could be muliple sigs: " + s);
    }

    public static AlloyModelError unknownName(Pos pos, String s) {
        return new AlloyModelError(pos, "unknown name: " + s);
    }

    public static AlloyModelError cannotResolveAssertName(Pos pos, String s) {
        return new AlloyModelError(pos, "cannot resolve assert name: " + s);
    }

    public static AlloyModelError cannotResolvePredFunName(Pos pos, String s) {
        return new AlloyModelError(pos, "cannot resolve pred/fun name: " + s);
    }

    public static AlloyModelError assertCanOnlyBeUsedWithCheck(Pos pos) {
        return new AlloyModelError(pos, "assert can only be used with check command");
    }

    public static AlloyModelError overloadingPredFunNotSupported(Pos pos) {
        return new AlloyModelError(pos, "overloaded pred/fun name in same namespace");
    }

    public static AlloyModelError recursivePredFunDependency(String name) {
        return new AlloyModelError("recursive pred/fun defn: " + name);
    }
}
