package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.repeatedAnd;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import java.util.Arrays;
import java.util.function.*;

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public TlaExp visit(DashParam dashParam) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public TlaExp visit(AlloyBinaryExpr binExpr) {

        VarArgsFunction<TlaExp, TlaExp> f = AlloyTlaExprLookup.lookup(binExpr);
        if(f!=null)
            return f.apply(this.visit(binExpr.left),this.visit(binExpr.right));
        throw new UnsupportedOperationException("non-translatable expression: "+binExpr.toString());
    }

    @Override
    public TlaExp visit(AlloyUnaryExpr unaryExpr) {
        VarArgsFunction<TlaExp, TlaExp> f = AlloyTlaExprLookup.lookup(unaryExpr);
        if(f!=null)
            return f.apply(this.visit(unaryExpr.sub));
        
        throw new UnsupportedOperationException("non-translatable expression: "+unaryExpr.toString());
    }

    @Override
    public TlaExp visit(AlloyVarExpr varExpr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public TlaExp visit(AlloyBlock block) {
        return repeatedAnd(mapBy(block.exprs, e -> visit(e)));
    }

    @Override
    public TlaExp visit(AlloyBracketExpr bracketExpr) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public TlaExp visit(AlloyCphExpr comprehensionExpr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public TlaExp visit(AlloyIteExpr iteExpr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public TlaExp visit(AlloyLetExpr letExpr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public TlaExp visit(AlloyQuantificationExpr quantificationExpr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public TlaExp visit(AlloyDecl decl) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }
}
