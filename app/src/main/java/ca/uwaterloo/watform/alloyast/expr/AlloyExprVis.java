package ca.uwaterloo.watform.alloyast.expr;

import ca.uwaterloo.watform.alloyast.expr.binary.AlloyBinaryExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyUnaryExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyVarExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.DashDo;
import ca.uwaterloo.watform.dashast.dashref.*;

public interface AlloyExprVis<T> {
    T visit(AlloyBinaryExpr binExpr);

    T visit(AlloyBlock block);

    T visit(AlloyBracketExpr bracketExpr);

    T visit(AlloyComprehensionExpr comprehensionExpr);

    T visit(AlloyIteExpr iteExpr);

    T visit(AlloyLetExpr letExpr);

    T visit(AlloyParenExpr parenExpr);

    T visit(AlloyQuantificationExpr quantificationExpr);

    T visit(AlloyUnaryExpr unaryExpr);

    T visit(AlloyVarExpr varExpr);

    T visit(AlloyDecl decl);

    default T visit(DashRef ref) {
        return null;
    }
    ;

    default T visit(DashDo dashDo) {
        return null;
    }

    default T visit(DashFrom dashFrom) {
        return null;
    }

    default T visit(DashGoto dashGoto) {
        return null;
    }

    default T visit(DashInit dashInit) {
        return null;
    }

    default T visit(DashInv dashInv) {
        return null;
    }

    default T visit(DashOn dashOn) {
        return null;
    }

    default T visit(DashParam dashParam) {
        return null;
    }

    default T visit(DashSend dashSend) {
        return null;
    }

    default T visit(DashWhen dashWhen) {
        return null;
    }
}
