/*
	This considers all instances of t1s
	(thus, the existential quantification)

    Need to rewrite the explanation below

    pred t1_enabledAfterStep[
            s:Snapshot,s':Snapshot,
            pParam0: Param0,
            ...
            scopesUsed0: StateLabel,
            scopesUsed1: Identifiers -> StateLabel,
            ...
            genEvents0:EventLabel,
            genEvents1: Identifiers -> EventLabel,
            ... ]
    {
        // many of these may depend on param values
        some (src_state_t1 & s'.confi) // where i is depth of src_state,
        guard_cond_t1[s']
        (s.stable = True) =>
            // only trans taken in big step so far is the t of scopesUsed and genEvents
            (o1 in code below) forall i:Ids. not(t1_nonOrthScopes(i) in scopesUsedi)
            (ev1) t1_on  in (s.eventsi :> EnvEvents) + genEventsi
        else {
            (o2) forall i:Ids. not(t1_nonOrthScopes(i) in scopesi + s'.scopesUsedi)
            (ev2) t1_on  in s.eventsi  + genEventsi
        }
    }
*/

package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.List;

public class TransIsEnabledAfterStepD2A extends TransPreD2A {

    protected TransIsEnabledAfterStepD2A(DashModel dm, TranslateOutput opt) {
        super(dm, opt);
    }

    public void addTransIsEnabledAfterStep(String tfqn) {

        String tout = DashFQN.translateFQN(tfqn);
        List<DashParam> prs = this.dm.transParams(tfqn);
        List<AlloyDecl> decls = this.dsl.emptyDeclList();
        List<AlloyExpr> body = this.dsl.emptyExprList();

        if (this.isElectrum) {
            decls.addAll(this.dsl.paramDecls(prs));
        } else {
            decls.addAll(this.dsl.curNextParamsDecls(prs));
        }
        for (int i = 0; i <= this.dm.maxDepthParams(); i++) {
            decls.add(this.dsl.scopeDecl(i));
            if (this.dm.hasEventsAti(i)) {
                decls.add(this.dsl.genEventDecl(i));
            }
        }

        if (!this.dm.hasOnlyOneState())
            // some (p3 -> p2 -> p1 -> src & s'.confi)
            // src does not have to be a basic state
            body.add(
                    AlloySome(
                            AlloyInter(
                                    this.dm.fromR(tfqn).asAlloyArrow(),
                                    this.dsl.nextConf(prs.size()))));

        // primed guard condition is true
        // TODO

        // orthogonality  ------------------

        // if first step of the big step
        // tfqn's non-orthogonal scope are not in any scopes used in the parameters
        List<AlloyExpr> orth1 = this.dsl.emptyExprList();
        List<DashRef> nonO = this.dm.nonOrthogonalScopesOf(tfqn);

        for (int i = 0; i <= this.dm.maxDepthParams(); i++) {
            final int j = i;
            List<AlloyExpr> u =
                    mapBy(
                            filterBy(nonO, x -> x.hasNumParams(j)),
                            y -> this.dsl.asScope(y).asAlloyArrow());
            // o1: forall i. not(t1_nonOrthScopei in scopesi)
            for (AlloyExpr x : u) orth1.add(AlloyNot(AlloyIn(x, this.dsl.scopeVar(i))));
        }
        AlloyExpr o1 = AlloyAndList(orth1);

        // if not the first of the big step
        // tfqn's non-orthogonal scope are not in any scopes used in the parameters + the cur scopes
        // used
        List<AlloyExpr> orth2 = this.dsl.emptyExprList();
        for (int i = 0; i <= this.dm.maxDepthParams(); i++) {
            final int j = i;
            List<AlloyExpr> u =
                    mapBy(
                            filterBy(nonO, x -> x.hasNumParams(j)),
                            y -> this.dsl.asScope(y).asAlloyArrow());
            // o2: forall i. not(t1_nonOrthScopei in scopesi + s'.scopesUsedi)
            for (AlloyExpr x : u)
                orth2.add(
                        AlloyNot(
                                AlloyIn(
                                        x,
                                        AlloyUnion(
                                                this.dsl.curScopesUsed(i), this.dsl.scopeVar(i)))));
        }
        AlloyExpr o2 = AlloyAndList(orth2);

        // events ----------------------------

        DashRef ev = this.dm.onR(tfqn);
        AlloyExpr ev1, ev2;
        if (ev != null) {
            Integer level = ev.paramValues.size();
            if (this.dm.hasEnvEvents()) {
                // ev1: t1_on  in (s.events0 inter EnvEvents) + genEvents0
                // or
                // ev1: t1_on  in (s.eventsi :> EnvEvents) + genEventsi
                ev1 =
                        AlloyIn(
                                ev.asAlloyArrow(),
                                AlloyUnion(
                                        this.dsl.RangeResLevel(
                                                this.dsl.curEvents(level),
                                                this.dsl.allEnvEventsVar(),
                                                level),
                                        this.dsl.genEventVar(level)));
            } else {
                // no env events so just
                // ev1: t1_on  in genEventsi
                ev1 = AlloyIn(ev.asAlloyArrow(), this.dsl.genEventVar(ev.paramValues.size()));
            }
            // ev2: t1_on  in s.eventsi  + genEventsi
            ev2 =
                    AlloyIn(
                            ev.asAlloyArrow(),
                            AlloyUnion(
                                    this.dsl.curEvents(ev.paramValues.size()),
                                    this.dsl.genEventVar(ev.paramValues.size())));
        } else {
            ev1 = AlloyTrueCond();
            ev2 = AlloyTrueCond();
        }

        if (this.dm.hasConcurrency())
            body.add(AlloyIte(this.dsl.curStableTrue(), AlloyAnd(o1, ev1), AlloyAnd(o2, ev2)));
        else body.add(AlloyAnd(o1, ev1));

        this.am.addPred(tout + D2AStrings.enabledAfterStepName, decls, body);
    }
}
