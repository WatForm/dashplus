package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.utils.*;

public final class AlloyModelImplErrors extends ImplementationError {
    private AlloyModelImplErrors(Pos pos, String msg) {
        super(pos, msg);
    }

    private AlloyModelImplErrors(String msg) {
        this(Pos.UNKNOWN, msg);
    }

    public static AlloyModelImplErrors duplicateInstance(Pos pos) {
        return new AlloyModelImplErrors(
                pos,
                "AlloyModelTable: the "
                        + "same instance is added twice. Paragraph at "
                        + pos.toString());
    }

    public static AlloyModelImplErrors lookUpWithNoName() {
        return new AlloyModelImplErrors(
                "Trying to get a nameless paragraph from "
                        + "AlloyModelTable; use AlloyModelTable.getUnnamedParagraphs() "
                        + "instead. ");
    }
}
