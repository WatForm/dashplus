package ca.uwaterloo.watform.predabstraction;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;

import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashtoalloy.DSL;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import java.util.*;

/*
    Input to this visitor class: AlloyExpr formula, CAF Map,
    Output: new formula with all the CAF's replaced by their respective ABV.boolean/isTrue
*/
public class CAF2ABVReplacer implements AlloyExprVis<AlloyExpr> {

    private HashMap<AlloyExpr, AlloyExpr> cafReplacementMap;

    public CAF2ABVReplacer(
            HashMap<String, AlloyExpr> cafMap,
            HashMap<AlloyExpr, AlloyExpr> dmap,
            String rootName) {
        cafReplacementMap = new HashMap<AlloyExpr, AlloyExpr>();
        for (String vname : cafMap.keySet()) {
            AlloyExpr caf = cafMap.get(vname);
            DSL dsl = new DSL(false);
            String vfqn = DashFQN.translateFQN(DashFQN.fqn(rootName, vname));
            AlloyExpr varIsTrue = dsl.AlloyIsTrue(dsl.curJoinExpr(AlloyVar(vfqn)));
            this.cafReplacementMap.put(caf, varIsTrue);
        }
        for (AlloyExpr d : dmap.keySet()) {
            AlloyExpr e = dmap.get(d);
            if (cafReplacementMap.containsKey(e)) {
                cafReplacementMap.put(d, cafReplacementMap.get(e));
            } else {
                if (e instanceof AlloyNegExpr) {
                    if (cafReplacementMap.containsKey(((AlloyUnaryExpr) e).sub)) {
                        cafReplacementMap.put(
                                d, AlloyNot(cafReplacementMap.get(((AlloyUnaryExpr) e).sub)));
                    }
                }
            }
        }
    }

    public AlloyExpr replaceWithABVs(AlloyExpr e) {
        if (e != null) {
            return this.visit(e);
        } else {
            return null;
        }
    }

    @Override
    public AlloyExpr visit(DashRef dashRef) {
        if (cafReplacementMap.containsKey((AlloyExpr) dashRef)) {
            return cafReplacementMap.get(dashRef);
        } else {
            return (AlloyExpr) dashRef;
        }
    }

    @Override
    public AlloyExpr visit(AlloyPrimeExpr expr) {
        if (cafReplacementMap.containsKey((AlloyExpr) expr)) {
            return cafReplacementMap.get(expr);
        } else {
            return (AlloyExpr) expr;
        }
    }

    // ones from Dash

    /*
    @Override
    public AlloyExpr visit(DashParam dashParam) {
        subexprs.add(dashParam.asAlloyVar());
        return null;
    }
    ;
    */

    @Override
    public AlloyExpr visit(AlloyVarExpr expr) {
        if (cafReplacementMap.containsKey((AlloyExpr) expr)) {
            return cafReplacementMap.get(expr);
        } else {
            return (AlloyExpr) expr;
        }
    }
    ;

    // below this line are recursive ones

    @Override
    public AlloyExpr visit(AlloyBinaryExpr binExpr) {
        // List<String> binOps = List.of(AND, AND_AMP, OR, OR_BAR, RFATARROW, IMPLIES, IFF,
        // IFF_ARR);
        // if (binOps.contains(binExpr.op)) {
        //     subexprs.add(this.visit(binExpr.left));
        //     subexprs.add(this.visit(binExpr.right));
        //     return binExpr.rebuild(binExpr.left, binExpr.right);
        // } else {
        //     return binExpr;
        // }
        if (binExpr instanceof AlloyAndExpr
                || binExpr instanceof AlloyOrExpr
                || binExpr instanceof AlloyImpliesExpr
                || binExpr instanceof AlloyIffExpr) {
            return binExpr.rebuild(this.visit(binExpr.left), this.visit(binExpr.right));
        } else {
            if (cafReplacementMap.containsKey((AlloyExpr) binExpr)) {
                return cafReplacementMap.get(binExpr);
            } else {
                return (AlloyExpr) binExpr;
            }
        }
    }
    ;

    @Override
    public AlloyExpr visit(AlloyUnaryExpr unaryExpr) {
        if (unaryExpr instanceof AlloyNegExpr) {
            return unaryExpr.rebuild(this.visit(unaryExpr.sub));
        } else {
            if (cafReplacementMap.containsKey((AlloyExpr) unaryExpr)) {
                return cafReplacementMap.get(unaryExpr);
            } else {
                return (AlloyExpr) unaryExpr;
            }
        }
    }
    ;

    // misc exprs

    @Override
    public AlloyExpr visit(AlloyBlock block) {
        List<AlloyExpr> exprs = new ArrayList<>();
        for (AlloyExpr e : block.exprs) {
            exprs.add(this.visit(e));
        }
        return new AlloyBlock(exprs);
    }
    ;

    @Override
    public AlloyExpr visit(AlloyBracketExpr bracketExpr) {
        if (cafReplacementMap.containsKey((AlloyExpr) bracketExpr)) {
            return cafReplacementMap.get(bracketExpr);
        } else {
            return (AlloyExpr) bracketExpr;
        }
    }
    ;

    @Override
    public AlloyExpr visit(AlloyCphExpr comprehensionExpr) {
        if (cafReplacementMap.containsKey((AlloyExpr) comprehensionExpr)) {
            return cafReplacementMap.get(comprehensionExpr);
        } else {
            return (AlloyExpr) comprehensionExpr;
        }
    }
    ;

    @Override
    public AlloyExpr visit(AlloyDecl decl) {
        return decl;
    }
    ;

    @Override
    public AlloyExpr visit(AlloyIteExpr iteExpr) {
        return iteExpr.rebuild(
                this.visit(iteExpr.cond), this.visit(iteExpr.conseq), this.visit(iteExpr.alt));
    }
    ;

    @Override
    public AlloyExpr visit(AlloyLetExpr letExpr) {
        return letExpr.rebuild(this.visit(letExpr.body));
    }
    ;

    @Override
    public AlloyExpr visit(AlloyParenExpr parenExpr) {
        return parenExpr.rebuild(this.visit(parenExpr.sub));
    }
    ;

    @Override
    public AlloyExpr visit(AlloyQuantificationExpr quantificationExpr) {
        return quantificationExpr.rebuild(this.visit(quantificationExpr.body));
    }
    ;
}
