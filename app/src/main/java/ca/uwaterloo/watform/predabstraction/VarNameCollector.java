package ca.uwaterloo.watform.predabstraction;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashtoalloy.D2AStrings;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import java.util.*;

public class VarNameCollector implements AlloyExprVis<Void> {

    private Set<String> varNames;
    private Set<String> ignored = new HashSet<>();

    public VarNameCollector() {
        ignored.add(AlloyStrings.LONE);
        ignored.add(AlloyStrings.ONE);
        ignored.add(AlloyStrings.SOME);
        ignored.add(AlloyStrings.SET);
        ignored.add(AlloyStrings.NO);
        ignored.add(AlloyStrings.NONE);
        ignored.add(AlloyStrings.ALL);
        ignored.add(AlloyStrings.THIS);
        ignored.add(AlloyStrings.util_plus);
        ignored.add(AlloyStrings.util_minus);
        ignored.add(AlloyStrings.util_lt);
        ignored.add(D2AStrings.curName);
        ignored.add(D2AStrings.nextName);
    }

    public Set<String> getVarNames(AlloyExpr e) {

        this.varNames = new HashSet<String>();
        if (e != null) {
            this.visit(e);
        }
        try {
            for (String v : varNames) {
                if (isInteger(v) || ignored.contains(v)) {
                    this.varNames.remove(v);
                }
            }
            // this.varNames.removeAll(ignored);
            return this.varNames;
        } catch (Exception ex) {
            return this.varNames;
        }
    }

    private boolean isInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Void visit(DashRef dashRef) {
        if (!ignored.contains(dashRef.name)) {
            varNames.add(dashRef.name);
        }
        return null;
    }

    @Override
    public Void visit(AlloyPrimeExpr expr) {
        this.visit(expr.sub);
        return null;
    }

    // ones from Dash

    /*
    @Override
    public Void visit(DashParam dashParam) {
        subexprs.add(dashParam.asAlloyVar());
        return null;
    }
    ;
    */

    @Override
    public Void visit(AlloyVarExpr varExpr) {
        if (!ignored.contains(varExpr.label)) {
            varNames.add(varExpr.label);
        }
        return null;
    }
    ;

    // below this line are recursive ones

    @Override
    public Void visit(AlloyBinaryExpr binExpr) {
        // List<String> binOps = List.of(AND, AND_AMP, OR, OR_BAR, RFATARROW, IMPLIES, IFF,
        // IFF_ARR);
        // if (binOps.contains(binExpr.op)) {
        //     subexprs.add(this.visit(binExpr.left));
        //     subexprs.add(this.visit(binExpr.right));
        //     return binExpr.rebuild(binExpr.left, binExpr.right);
        // } else {
        //     return binExpr;
        // }
        this.visit(binExpr.left);
        this.visit(binExpr.right); // just do this.visit
        return null;
    }
    ;

    @Override
    public Void visit(AlloyUnaryExpr unaryExpr) {
        this.visit(unaryExpr.sub);
        return null;
    }
    ;

    // misc exprs

    @Override
    public Void visit(AlloyBlock block) {
        for (AlloyExpr e : block.exprs) {
            this.visit(e);
        }
        return null;
    }
    ;

    @Override
    public Void visit(AlloyBracketExpr bracketExpr) {
        this.visit(bracketExpr.expr);
        for (AlloyExpr e : bracketExpr.exprs) {
            this.visit(e);
        }
        return null;
    }
    ;

    @Override
    public Void visit(AlloyCphExpr comprehensionExpr) {
        AlloyExpr e = comprehensionExpr.body.orElse(null);
        if (e != null) {
            this.visit(e);
        }
        return null;
    }
    ;

    @Override
    public Void visit(AlloyDecl decl) {
        return null;
    }
    ;

    @Override
    public Void visit(AlloyIteExpr iteExpr) {
        this.visit(iteExpr.cond);
        this.visit(iteExpr.conseq);
        this.visit(iteExpr.alt);
        return null;
    }
    ;

    @Override
    public Void visit(AlloyLetExpr letExpr) {
        this.visit(letExpr.body);
        return null;
    }
    ;

    @Override
    public Void visit(AlloyParenExpr parenExpr) {
        this.visit(parenExpr.sub);
        return null;
    }
    ;

    @Override
    public Void visit(AlloyQuantificationExpr quantificationExpr) {
        this.visit(quantificationExpr.body);
        return null;
    }
    ;
}
