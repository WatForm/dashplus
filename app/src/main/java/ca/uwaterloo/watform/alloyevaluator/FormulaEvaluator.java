package ca.uwaterloo.watform.alloyevaluator;

import static ca.uwaterloo.watform.utils.CommonStrings.*;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.List;
import java.util.Set;

public class FormulaEvaluator implements AlloyExprVis<Boolean> {
    private final AlloyExprVis<Set<List<String>>> setEvaluator;

    public FormulaEvaluator(String xmlFileName) {
        setEvaluator = new SetEvaluator(xmlFileName);
    }

    public Boolean visit(DashRef dashRef) {
        dashOutput("Hit generic DashRef visit " + dashRef);
        return false;
    }

    public Boolean visit(AlloyBinaryExpr binExpr) {
        dashOutput("Hit generic AlloyBinaryExpr visit " + binExpr);
        return false;
    }

    public Boolean visit(AlloyUnaryExpr unaryExpr) {
        dashOutput("Hit generic AlloyUnaryExpr visit " + unaryExpr);
        return false;
    }

    public Boolean visit(AlloyVarExpr varExpr) {
        dashOutput("Hit generic AlloyVarExpr visit " + varExpr);
        return false;
    }

    public Boolean visit(AlloyBracketExpr bracketExpr) {
        dashOutput("Hit generic AlloyBracketExpr visit " + bracketExpr);
        return false;
    }

    public Boolean visit(AlloyCphExpr comprehensionExpr) {
        dashOutput("Hit generic AlloyCphExpr visit " + comprehensionExpr);
        return false;
    }

    public Boolean visit(AlloyIteExpr iteExpr) {
        dashOutput("Hit generic AlloyIteExpr visit " + iteExpr);
        return false;
    }

    public Boolean visit(AlloyLetExpr letExpr) {
        dashOutput("Hit generic AlloyLetExpr visit " + letExpr);
        return false;
    }

    public Boolean visit(AlloyQuantificationExpr quantificationExpr) {
        dashOutput("Hit generic AlloyQuantificationExpr visit " + quantificationExpr);
        return false;
    }

    public Boolean visit(AlloyDecl decl) {
        dashOutput("Hit generic AlloyDecl visit " + decl);
        return false;
    }

    public Boolean visit(AlloyBlock block) {
        dashOutput("Evaluating block: " + block);
        for (var expr : block.exprs) {
            dashOutput("Evaluating expr: " + expr);
            if (!expr.accept(this)) {
                dashOutput("The expression evaluated to false: " + expr);
                return false;
            }
        }
        return true;
    }

    public Boolean visit(AlloyQtExpr qtExpr) {
        switch (qtExpr.qt) {
            case AlloyQtEnum.NO -> {
                dashOutput("Multiplicity No case: " + qtExpr.sub);
                return qtExpr.sub.accept(setEvaluator).isEmpty();
            }
            case AlloyQtEnum.SOME -> {
                dashOutput("Multiplicity Some case: " + qtExpr.sub);
                return !qtExpr.sub.accept(setEvaluator).isEmpty();
            }
            case AlloyQtEnum.ONE -> {
                dashOutput("Multiplicity One case: " + qtExpr.sub);
                return qtExpr.sub.accept(setEvaluator).size() == 1;
            }
            case AlloyQtEnum.LONE -> {
                dashOutput("Multiplicity Lone case: " + qtExpr.sub);
                return qtExpr.sub.accept(setEvaluator).size() <= 1;
            }
            default -> {
                dashOutput("Multiplicity Hit unexpected case " + qtExpr.qt);
                return false;
            }
        }
    }
}
