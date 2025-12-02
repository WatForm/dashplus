package ca.uwaterloo.watform.alloyast;

import ca.uwaterloo.watform.alloyast.expr.var.AlloyScopableExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.List;

public class AlloyCtorError extends DashPlusError {
    private AlloyCtorError(Pos pos, String msg) {
        super(pos, msg);
    }

    private AlloyCtorError(String msg) {
        super(msg);
    }

    private AlloyCtorError(List<Pos> posList, String msg) {
        super(posList, null, msg);
    }

    // ====================================================================================
    // Sig
    // ====================================================================================
    public static AlloyCtorError sigContradictQuals(Pos pos, String qual1, String qual2) {
        return new AlloyCtorError(pos, "A sig cannot contain both " + qual1 + " and " + qual2);
    }

    public static AlloyCtorError sigAbsSubset(Pos pos) {
        return new AlloyCtorError(pos, "A subset signature cannot be abstract");
    }

    public static AlloyCtorError sigCannotExtend(Pos pos, String sigRef) {
        return new AlloyCtorError(pos, "Signature cannot extend builtin " + sigRef + " signature");
    }

    // ====================================================================================
    // Module
    // ====================================================================================
    public static AlloyCtorError moduleIsUnique(Pos pos1, Pos pos2) {
        return new AlloyCtorError(
                List.of(pos1, pos2), "A file can only contain one Module declaration: ");
    }

    public static AlloyCtorError moduleIsAtTop(Pos pos) {
        return new AlloyCtorError(pos, "A Module declaration must occur at the top. ");
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

    public static AlloyCtorError endWithoutDotDot(Pos pos) {
        throw new AlloyCtorError(pos, "Cannot specify end scope without having two dots. ");
    }

    public static AlloyCtorError cmdNegScop(Pos pos) {
        throw new AlloyCtorError(pos, "Scope cannot be negative");
    }

    public static AlloyCtorError cmdDecreasingScope(Pos pos) {
        throw new AlloyCtorError(pos, "The end scope cannot be smaller than the start scope");
    }

    public static AlloyCtorError cmdInvalidIncrement(Pos pos) {
        throw new AlloyCtorError(pos, "The increment cannot be smaller than 1");
    }

    public static AlloyCtorError cmdBitwidthTooBig(Pos pos) {
        throw new AlloyCtorError(pos, "Cannot specify a bitwidth greater than 30");
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
