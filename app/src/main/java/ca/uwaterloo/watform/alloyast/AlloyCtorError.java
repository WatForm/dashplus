package ca.uwaterloo.watform.alloyast;

import ca.uwaterloo.watform.alloyast.expr.var.AlloyScopableExpr;
import ca.uwaterloo.watform.utils.*;

public class AlloyCtorError extends RuntimeException {
    public final Pos pos;

    private AlloyCtorError(Pos pos, String msg) {
        super(msg);
        this.pos = pos;
    }

    private AlloyCtorError(String msg) {
        this(Pos.UNKNOWN, msg);
    }

    // Sig
    public static AlloyCtorError sigMustHaveName(Pos pos) {
        return new AlloyCtorError(pos, "Signature must have a non-blank name: " + pos.toString());
    }

    public static AlloyCtorError sigMustHaveArgs(Pos pos) {
        return new AlloyCtorError(
                pos,
                "Signature must have args(can be empty list, but not null): " + pos.toString());
    }

    public static AlloyCtorError sigMustHaveBlock(Pos pos) {
        return new AlloyCtorError(pos, "Signature must have a block: " + pos.toString());
    }

    // Module
    public static AlloyCtorError moduleIsUnique(Pos pos1, Pos pos2) {
        return new AlloyCtorError(
                pos1,
                "A file can only contain one Module declaration: "
                        + pos1.toString()
                        + ", \n"
                        + pos2.toString());
    }

    public static AlloyCtorError moduleIsAtTop(Pos pos) {
        return new AlloyCtorError(
                pos, "A Module declaration must occur at the top: " + pos.toString());
    }

    // Cmd
    public static AlloyCtorError growingScope(Pos pos, AlloyScopableExpr scopableExpr) {
        return new AlloyCtorError(
                pos, "Cannot specify a growing scope for " + scopableExpr.toString());
    }

    public static AlloyCtorError redundantExactly(Pos pos) {
        throw new Reporter.ErrorUser(pos, "The exactly keyword is redundant here");
    }
}
