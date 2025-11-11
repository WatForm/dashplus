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

    // ====================================================================================
    // Sig
    // ====================================================================================

    // ====================================================================================
    // Module
    // ====================================================================================
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

    // ====================================================================================
    // Cmd
    // ====================================================================================
    public static AlloyCtorError growingScope(Pos pos, AlloyScopableExpr scopableExpr) {
        return new AlloyCtorError(
                pos, "Cannot specify a growing scope for " + scopableExpr.toString());
    }

    public static AlloyCtorError redundantExactly(Pos pos) {
        throw new AlloyCtorError(pos, "The exactly keyword is redundant here");
    }

    // ====================================================================================
    // Decl
    // ====================================================================================
    public static AlloyCtorError declExactlyCannotHaveDisj(Pos pos) {
        throw new AlloyCtorError(
                pos,
                "Decl with quant EXACTLY cannot be disjoint on either "
                        + "side and cannot be var. ");
    }

    // ====================================================================================
    // Qname
    // ====================================================================================
    public static AlloyCtorError qnameFirstMustBeNameThisOrSeq(Pos pos) {
        throw new AlloyCtorError(
                pos,
                "First var of AlloyQnameExpr must be either AlloyNameExpr, "
                        + "AlloySeqExpr or AlloyThisExpr. ");
    }

    public static AlloyCtorError qnameTailIsAllName(Pos pos) {
        throw new AlloyCtorError(
                pos, "Everything after the head of " + "AlloyQnameExpr must be AlloyNameExpr. ");
    }
}
