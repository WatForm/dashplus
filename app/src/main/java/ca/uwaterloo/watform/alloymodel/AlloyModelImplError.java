package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.utils.*;

public final class AlloyModelImplError extends ImplementationError {

    private AlloyModelImplError(String msg) {
        super(msg);
    }

    private AlloyModelImplError(Pos pos, String msg) {
        super(pos, msg);
    }

    public static AlloyModelImplError builtinNotFound(String s) {
        return new AlloyModelImplError(
                "Trying to look up the arity of a symbol that is not builtin: " + s);
    }

    public static AlloyModelImplError predFunNotFound(String s) {
        return new AlloyModelImplError(
                "Trying to look up the arity of a symbol that is not a pred/fun: " + s);
    }

    public static AlloyModelImplError fcnNotFound(String s) {
        return new AlloyModelImplError(
                "Trying to look up the result of a symbol that is not a fcn: " + s);
    }

    public static AlloyModelImplError lookUpWithNoName() {
        return new AlloyModelImplError(
                "Trying to get a nameless paragraph from "
                        + "AlloyModelTable; use AlloyModelTable.getUnnamedParagraphs() "
                        + "instead. ");
    }

    public static AlloyModelImplError invalidTypRelArg() {
        return new AlloyModelImplError(
                "AlloyTypRel must contain at least one type. "
                        + "AlloyTypRel.unionRel cannot contain any nulls or blanks. ");
    }

    public static AlloyModelImplError diffArity() {
        return new AlloyModelImplError(
                "AlloyTypRel.unionRel must contain "
                        + "lists of the same length; relations of "
                        + "same arity");
    }

    public static AlloyModelImplError tryingToAccessNonExistentSig(String s) {
        return new AlloyModelImplError("Trying to access a sig that does not exist: " + s);
    }

    public static AlloyModelImplError tryingToAccessNonExistentField(String s) {
        return new AlloyModelImplError("Trying to access a field that does not exist: " + s);
    }

    public static AlloyModelImplError tryingToAccessNonExistentPredFun(String s) {
        return new AlloyModelImplError(
                "Trying to access a pred/fun name that does not exist: " + s);
    }

    public static AlloyModelImplError noScopeForNonSig(String s) {
        return new AlloyModelImplError("Asking for scope of a non-sig: " + s);
    }

    public static AlloyModelImplError blankStringNotAllowed() {
        return new AlloyModelImplError("Blank string not allowed");
    }

    public static AlloyModelImplError qnameNameCannotHaveSlash(String s) {
        return new AlloyModelImplError("Qname name " + s + " cannot have slash in it");
    }
}
