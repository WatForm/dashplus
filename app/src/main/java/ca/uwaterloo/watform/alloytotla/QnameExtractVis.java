package ca.uwaterloo.watform.alloytotla;

import ca.uwaterloo.watform.alloyast.expr.binary.AlloyBinaryExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBracketExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyCphExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyIteExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyLetExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyQuantificationExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyUnaryExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyIdenExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNoneExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyUnivExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyVarExpr;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QnameExtractVis implements AlloyExprVis<List<String>> {

    @Override
    public List<String> visit(DashRef dashRef) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public List<String> visit(DashParam dashParam) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public List<String> visit(AlloyBinaryExpr binExpr) {
        // TODO Auto-generated method stub
        var ans = visit(binExpr.left);
        ans.addAll(visit(binExpr.right));
        return ans;
    }

    @Override
    public List<String> visit(AlloyUnaryExpr unaryExpr) {
        // TODO Auto-generated method stub

        return visit(unaryExpr.sub);
    }

    @Override
    public List<String> visit(AlloyVarExpr varExpr) {
        // TODO Auto-generated method stub

        List<String> nothing = new ArrayList<>();
        return switch (varExpr) {
            case AlloyUnivExpr _ -> nothing;
            case AlloyIdenExpr _ -> nothing;
            case AlloyNoneExpr _ -> nothing;
            case AlloyQnameExpr e -> Arrays.asList(e.label);
            default -> null;
        };
    }

    @Override
    public List<String> visit(AlloyBlock block) {
        // TODO Auto-generated method stub

        List<String> ans = new ArrayList<>();
        block.exprs.forEach(exp -> ans.addAll(visit(exp)));
        return ans;
    }

    @Override
    public List<String> visit(AlloyBracketExpr bracketExpr) {
        // TODO Auto-generated method stub
        return visit(bracketExpr.expr);
    }

    @Override
    public List<String> visit(AlloyCphExpr comprehensionExpr) {
        // TODO Auto-generated method stub
        // bound variables need to be removed
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public List<String> visit(AlloyIteExpr iteExpr) {
        // TODO Auto-generated method stub
        var ans = visit(iteExpr.cond);
        ans.addAll(visit(iteExpr.alt));
        ans.addAll(visit(iteExpr.alt));
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public List<String> visit(AlloyLetExpr letExpr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public List<String> visit(AlloyQuantificationExpr quantificationExpr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public List<String> visit(AlloyDecl decl) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }
}
