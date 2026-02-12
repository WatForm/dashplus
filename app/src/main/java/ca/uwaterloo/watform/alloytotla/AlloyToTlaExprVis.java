package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.alloyast.expr.binary.AlloyBinaryExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBracketExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyCphExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyIteExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyLetExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyQuantificationExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyUnaryExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyVarExpr;
import ca.uwaterloo.watform.alloytotla.AlloyTlaExprLookup.VarArgsFunction;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.tlaast.*;

public class AlloyToTlaExprVis implements AlloyExprVis<TlaExp> {

    @Override
    public TlaExp visit(DashRef dashRef) {

        throw new UnsupportedOperationException("Unimplemented method 'visit' dashRef");
    }

    @Override
    public TlaExp visit(DashParam dashParam) {

        throw new UnsupportedOperationException("Unimplemented method 'visit' dashParam");
    }

    @Override
    public TlaExp visit(AlloyBinaryExpr binExpr) {

        VarArgsFunction<TlaExp, TlaExp> f = AlloyTlaExprLookup.lookup(binExpr);
        if (f != null) return f.apply(this.visit(binExpr.left), this.visit(binExpr.right));
        throw new UnsupportedOperationException(
                "non-translatable expression: " + binExpr.toString());
    }

    @Override
    public TlaExp visit(AlloyUnaryExpr unaryExpr) {
        VarArgsFunction<TlaExp, TlaExp> f = AlloyTlaExprLookup.lookup(unaryExpr);
        if (f != null) return f.apply(this.visit(unaryExpr.sub));

        throw new UnsupportedOperationException(
                "non-translatable expression: " + unaryExpr.toString());
    }

    @Override
    public TlaExp visit(AlloyVarExpr varExpr) {

        VarArgsFunction<TlaExp, TlaExp> f = AlloyTlaExprLookup.lookup(varExpr);
        if (f != null) return f.apply();
        throw new UnsupportedOperationException(
                "non-translatable expression: " + varExpr.toString());
    }

    @Override
    public TlaExp visit(AlloyBlock block) {
        VarArgsFunction<TlaExp, TlaExp> f = AlloyTlaExprLookup.lookup(block);
        if (f != null)
            return f.apply(mapBy(block.exprs, e -> this.visit(e)).toArray(new TlaExp[0]));

        throw new UnsupportedOperationException("non-translatable expression: " + block.toString());
    }

    @Override
    public TlaExp visit(AlloyBracketExpr bracketExpr) {

        throw new UnsupportedOperationException("Unimplemented method 'visit' for bracket");
    }

    @Override
    public TlaExp visit(AlloyCphExpr comprehensionExpr) {

        throw new UnsupportedOperationException("Unimplemented method 'visit' for cph");
    }

    @Override
    public TlaExp visit(AlloyIteExpr iteExpr) {

        throw new UnsupportedOperationException("Unimplemented method 'visit' for ite");
    }

    @Override
    public TlaExp visit(AlloyLetExpr letExpr) {

        throw new UnsupportedOperationException("Unimplemented method 'visit' for let");
    }

    @Override
    public TlaExp visit(AlloyQuantificationExpr quantificationExpr) {

        throw new UnsupportedOperationException("Unimplemented method 'visit' for quantification");
    }

    @Override
    public TlaExp visit(AlloyDecl decl) {

        throw new UnsupportedOperationException("Unimplemented method 'visit' for decl");
    }
}
