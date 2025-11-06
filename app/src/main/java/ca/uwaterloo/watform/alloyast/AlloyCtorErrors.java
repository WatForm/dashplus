package ca.uwaterloo.watform.alloyast;

import ca.uwaterloo.watform.utils.*;

public class AlloyCtorErrors extends RuntimeException {
    public Pos pos;

    private AlloyCtorErrors(Pos pos, String msg) {
        super(msg, null);
        this.pos = pos;
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
}
