/*

* An AlloyCtorError (Alloy constructor error) is well-formedness error that is thrown at construction time of Alloy ASTs.

* An AlloyCtorError is a subclass of DashPlusException that could be caused either:
    - During parsing, these should be caught and collected as user feedback by adding them into the Reporter.
    - During creation of AST elements by Dash To Alloy translation or other internal functions, which is an implementation error and should be allowed to propagate to the main as an exception.


- This is an example of a DashPlusException that is treated as UserError (if generated during parsing) or as ImplementationError (if generated during translation)

*/

package ca.uwaterloo.watform.alloyast;

import ca.uwaterloo.watform.alloyast.expr.var.AlloyScopableExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.List;

public class AlloyCtorError extends UserOrImplError {

    private AlloyCtorError(String msg) {
        super(msg);
    }

    private AlloyCtorError(List<Pos> posList, String msg) {
        super(posList, msg);
    }

    private AlloyCtorError(Pos pos, String msg) {
        super(pos, msg);
    }

    public static AlloyCtorError utilFileNotFound(Pos pos, String msg) {
        return new AlloyCtorError(pos, "Util file not found in jar: " + msg);
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

    public static AlloyCtorError emptyAlloyDeclNames(Pos pos) {
        throw new AlloyCtorError(pos, "Decl must have names");
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

    // =======
    // Wff errors
    // =======
    /*
    public static AlloyCtorError arityMismatch(Pos pos, String left, String right) {
        throw new AlloyCtorError(
                pos, "Arities are not equal in left: " + left + " and right: " + right);
    }
    */

    public static AlloyCtorError seqWithMul(Pos p, String s) {
        throw new AlloyCtorError(p, "Decl with seq cannot have mul: " + s);
    }

    // ====================================================================================
    // AlloyQtEnum
    // This error is not possible to come from parsing,
    // dash.g4 enforces the correct mul; an Antlr
    // error is thrown if it's not correct.
    // ====================================================================================
    public static AlloyCtorError invalidAlloyQtEnum(Pos pos, String msg) {
        return new AlloyCtorError(pos, msg);
    }

    public static AlloyCtorError invalidAlloyQtEnum(String msg) {
        return new AlloyCtorError(msg);
    }

    /**
     * A WFF error, but it cannot occur through the ANTLR parser. So it cannot occur during parsing;
     * it must be an ImplementationError
     *
     * @param pos
     * @param field1
     * @param field2
     * @param className
     * @return AlloyASTImplError
     */
    public static AlloyCtorError xorFields(
            Pos pos, String field1, String field2, String className) {
        return new AlloyCtorError(
                pos,
                field1
                        + " and "
                        + field2
                        + " are mutually-exclusive fields in "
                        + className
                        + ". It must contain exactly one of them.");
    }
}
