package ca.uwaterloo.watform.alloyast;

import ca.uwaterloo.watform.utils.*;

public class AlloyCtorErrors extends Reporter.ErrorUser {
    private AlloyCtorErrors(Pos pos, String msg) {
        super(pos, msg);
    }

    private AlloyCtorErrors(String msg) {
        this(Pos.UNKNOWN, msg);
    }

    public static AlloyCtorErrors sigMustHaveName(Pos pos) {
        return new AlloyCtorErrors(pos, "Signature must have a non-blank name: " + pos.toString());
    }

    public static AlloyCtorErrors sigMustHaveArgs(Pos pos) {
        return new AlloyCtorErrors(
                pos,
                "Signature must have args(can be empty list, but not null): " + pos.toString());
    }

    public static AlloyCtorErrors sigMustHaveBlock(Pos pos) {
        return new AlloyCtorErrors(pos, "Signature must have a block: " + pos.toString());
    }

    public static AlloyCtorErrors moduleIsUnique(Pos pos1, Pos pos2) {
        return new AlloyCtorErrors(
                pos1,
                "A file can only contain one Module declaration: "
                        + pos1.toString()
                        + ", \n"
                        + pos2.toString());
    }

    public static AlloyCtorErrors moduleIsAtTop(Pos pos) {
        return new AlloyCtorErrors(
                pos, "A Module declaration must occur at the top: " + pos.toString());
    }
}
