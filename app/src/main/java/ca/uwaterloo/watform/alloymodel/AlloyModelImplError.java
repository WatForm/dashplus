package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.utils.*;

public final class AlloyModelImplError extends ImplementationError {
    private AlloyModelImplError(Pos pos, String msg) {
        super(pos, msg);
    }

    private AlloyModelImplError(String msg) {
        this(Pos.UNKNOWN, msg);
    }

    public static AlloyModelImplError duplicateInstance(Pos pos) {
        return new AlloyModelImplError(
                pos,
                "AlloyModelTable: the "
                        + "same instance is added twice. Paragraph at "
                        + pos.toString());
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
}
