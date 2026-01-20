/*
    This is NOT part of D2A
    so that it can be called by places other than the translator
    just to create expressions.
*/

package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.dashtoalloy.AlloyHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyBinaryExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashParam;
// import ca.uwaterloo.watform.dashast.D2AStrings;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashmodel.DashFQN;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import java.util.List;
import java.util.stream.Collectors;

public class ExprTranslatorVis implements AlloyExprVis<AlloyExpr> {

    private boolean isPrimed = false;
    private boolean onlyGetName = false;
    private boolean isElectrum;
    private DashModel dm;
    protected DSL dsl;

    public ExprTranslatorVis(DashModel dm, boolean isElectrum) {
        this.dm = dm;
        this.isElectrum = isElectrum;
        this.dsl = new DSL(dm, isElectrum);
    }

    public ExprTranslatorVis(DashModel dm) {
        this.dm = dm;
        this.isElectrum = false;
    }

    public AlloyExpr translateExpr(AlloyExpr e, boolean onlyGetName) {
        this.onlyGetName = onlyGetName;
        return visit(e);
    }

    public AlloyExpr translateExpr(AlloyExpr e) {
        this.onlyGetName = false;
        return visit(e);
    }

    // visitor

    @Override
    public AlloyExpr visit(DashRef dashRef) {

        // DashRef var does not contain a primed variable
        // it could be within a PRIME unary op
        // in which case, object attribute isPrimed has been set

        // onlyGetName = true is ONLY used for the case when
        // translating type expressions of snapshot signatures
        // there we only require the full name with underscores (not the parameters)

        if (this.onlyGetName) {
            String vfqn = dashRef.name;
            return AlloyVar(DashFQN.translateFQN(vfqn));
        }

        // translate paramvalues
        // may be empty
        List<AlloyExpr> join_list =
                dashRef.paramValues.stream().map(i -> visit(i)).collect(Collectors.toList());

        String vfqn = dashRef.name;
        AlloyExpr v_expr = AlloyVar(DashFQN.translateFQN(vfqn));

        if (!this.isElectrum) {
            // tcmc, traces
            if (this.isPrimed)
                // p1.p2.(sn.v)
                join_list.add(this.dsl.nextJoinExpr((AlloyQnameExpr) v_expr));
            else
                // p1.p2.(s.v)
                join_list.add(this.dsl.curJoinExpr((AlloyQnameExpr) v_expr));
            return AlloyJoinFromExprList(join_list);
        } else {
            // Electrum
            if (this.isPrimed)
                // have to put the prime in the var name
                v_expr = new AlloyPrimeExpr(v_expr);
            if (this.dm.containsVar(vfqn)
                    && dm.varParams(vfqn).size() == 0
                    && !((this.dm.varTyp(vfqn)) instanceof AlloyVarExpr)) {
                // in Electrum - if we have a dynamic var (not buffer) with
                // no parameters, and
                // is a non-var, non-one var, non-lone var, non-set var, non-parametrized
                // type (i.e., an arrow type), we have to handle it specially
                // Variables.v or Variables.v'
                return AlloyJoin(AlloyVar(D2AStrings.variablesName), v_expr);
            } else {
                // p2.p1.v or p2.p1.v'
                join_list.add(v_expr);
                return AlloyJoinFromExprList(join_list);
            }
        }
    }

    @Override
    public AlloyExpr visit(AlloyPrimeExpr expr) {
        assert (expr.sub instanceof DashRef);
        this.isPrimed = true;
        // note that it does not put the PRIME on the outside
        // this is done within the DashRef
        AlloyExpr x = visit(expr.sub);
        this.isPrimed = false;
        return x;
    }

    // ones from Dash

    @Override
    public AlloyExpr visit(DashParam dashParam) {
        return dashParam.asAlloyVar();
    }

    @Override
    public AlloyExpr visit(AlloyVarExpr varExpr) {
        return varExpr;
    }

    // below this line are recursive ones

    @Override
    public AlloyExpr visit(AlloyBinaryExpr binExpr) {
        return binExpr.rebuild(this.visit(binExpr.left), this.visit(binExpr.right));
    }

    @Override
    public AlloyExpr visit(AlloyUnaryExpr unaryExpr) {
        return unaryExpr.rebuild(this.visit(unaryExpr.sub));
    }

    // misc exprs

    @Override
    public AlloyExpr visit(AlloyBlock block) {
        return new AlloyBlock(mapBy(block.exprs, e -> this.visit(e)));
    }

    @Override
    public AlloyExpr visit(AlloyBracketExpr bracketExpr) {
        return new AlloyBracketExpr(
                this.visit(bracketExpr.expr), mapBy(bracketExpr.exprs, e -> this.visit(e)));
    }

    @Override
    public AlloyExpr visit(AlloyCphExpr comprehensionExpr) {

        return new AlloyCphExpr(
                mapBy(comprehensionExpr.decls, i -> ((AlloyDecl) this.visit(i))),
                comprehensionExpr.body.map(b -> this.visit(b)).orElse(null));
    }

    @Override
    public AlloyExpr visit(AlloyDecl decl) {
        return decl.withExpr(this.visit(decl.expr));
    }

    @Override
    public AlloyExpr visit(AlloyIteExpr iteExpr) {
        return new AlloyIteExpr(
                this.visit(iteExpr.cond), this.visit(iteExpr.conseq), this.visit(iteExpr.alt));
    }

    @Override
    public AlloyExpr visit(AlloyLetExpr letExpr) {
        return letExpr.rebuild(this.visit(letExpr.body));
    }

    @Override
    public AlloyExpr visit(AlloyParenExpr parenExpr) {
        return new AlloyParenExpr(this.visit(parenExpr.sub));
    }

    @Override
    public AlloyExpr visit(AlloyQuantificationExpr quantificationExpr) {
        return quantificationExpr.rebuild(this.visit(quantificationExpr.body));
    }
}
