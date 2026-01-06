package ca.uwaterloo.watform.alloyast.expr;

import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;

public interface AlloyExprVis<T> {
    public default T visit(AlloyExpr expr) {
        return expr.accept(this);
    }

    // ones from Dash
    T visit(DashRef dashRef);

    T visit(DashParam dashParam);

    // Abstract ones that need implementation in extensions
    T visit(AlloyBinaryExpr binExpr);

    T visit(AlloyUnaryExpr unaryExpr);

    T visit(AlloyVarExpr varExpr);

    // Misc exprs (besides AlloyParenExpr); also need implementation in
    // extensions
    T visit(AlloyBlock block);

    T visit(AlloyBracketExpr bracketExpr);

    T visit(AlloyCphExpr comprehensionExpr);

    T visit(AlloyIteExpr iteExpr);

    T visit(AlloyLetExpr letExpr);

    T visit(AlloyQuantificationExpr quantificationExpr);

    T visit(AlloyDecl decl);

    default T visit(AlloyParenExpr parenExpr) {
        return parenExpr.sub.accept(this);
    }

    // Default implementations
    // These will delegate up to the super class

    // Binary
    default T visit(AlloyAndExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyArrowExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyCmpExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyDiffExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyDomRestrExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyDotExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyFunAddExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyFunDivExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyFunMulExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyFunRemExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyFunSubExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyIffExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyImpliesExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyIntersExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyOrExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyRelOvrdExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyReleasesExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyRngRestrExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyShAExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyShLExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyShRExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloySinceExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyStateSeqExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyTriggeredExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyUnionExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    default T visit(AlloyUntilExpr expr) {
        return visit((AlloyBinaryExpr) expr);
    }

    // Unary
    default T visit(AlloyAfterExpr expr) {
        return visit((AlloyUnaryExpr) expr);
    }

    default T visit(AlloyAlwaysExpr expr) {
        return visit((AlloyUnaryExpr) expr);
    }

    default T visit(AlloyBeforeExpr expr) {
        return visit((AlloyUnaryExpr) expr);
    }

    default T visit(AlloyEventuallyExpr expr) {
        return visit((AlloyUnaryExpr) expr);
    }

    default T visit(AlloyHistoricallyExpr expr) {
        return visit((AlloyUnaryExpr) expr);
    }

    default T visit(AlloyNegExpr expr) {
        return visit((AlloyUnaryExpr) expr);
    }

    default T visit(AlloyCardExpr expr) {
        return visit((AlloyUnaryExpr) expr);
    }

    default T visit(AlloyNumIntExpr expr) {
        return visit((AlloyUnaryExpr) expr);
    }

    default T visit(AlloyNumSumExpr expr) {
        return visit((AlloyUnaryExpr) expr);
    }

    default T visit(AlloyOnceExpr expr) {
        return visit((AlloyUnaryExpr) expr);
    }

    default T visit(AlloyPrimeExpr expr) {
        return visit((AlloyUnaryExpr) expr);
    }

    default T visit(AlloyQtExpr expr) {
        return visit((AlloyUnaryExpr) expr);
    }

    default T visit(AlloyReflTransClosExpr expr) {
        return visit((AlloyUnaryExpr) expr);
    }

    default T visit(AlloyTransClosExpr expr) {
        return visit((AlloyUnaryExpr) expr);
    }

    default T visit(AlloyTransExpr expr) {
        return visit((AlloyUnaryExpr) expr);
    }

    // var
    default T visit(AlloyAtNameExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyDisjExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyFunMaxExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyFunMinExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyFunNextExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyIdenExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyIntExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyNameExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyNoneExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyNumExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyPredTotOrdExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyQnameExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyScopableExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloySeqExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloySeqIntExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloySigIntExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloySigRefExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyStepsExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyStrLiteralExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyStringExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloySumExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyThisExpr expr) {
        return visit((AlloyVarExpr) expr);
    }

    default T visit(AlloyUnivExpr expr) {
        return visit((AlloyVarExpr) expr);
    }
}
