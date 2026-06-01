package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashmodel.DashParam;
import java.util.List;

public class TransPreD2A extends InvsD2A {

    /*
        pred pre_t1[s:Snapshot,pparam0:Param0, ...] {

            some (src_state_t1 & <prs for src_state>.s.conf)
            orthogonal to any scopes uses

            guard_cond_t1 [s]

            s.stable = True =>
                { // beginning of a big step
                  // transition can be triggered only by environmental events
                  <prs for trig_event>.trig_events_t1 in (s.events & ExternalEvent )
                } else {
                  // intermediate snapshot
                  // transition can be triggered by any type of event
                  <prs for trig_event>.trig_events_t1 in s.events
                }
            pre of a higher priority transition is not enabled
        }
        Assumption: prs for src state and trig events are a subset of prs for trans
    */

    protected TransPreD2A(DashModel dm, Options opt) {
        super(dm, opt);
    }

    public void addTransPreVarsOnly(String tfqn) {
        this.addTransPre(tfqn, true);
    }

    public void addTransPre(String tfqn) {
        this.addTransPre(tfqn, false);
    }

    private void addTransPre(String tfqn, Boolean addGuardOnly) {
        List<DashParam> params = this.dm.transParams(tfqn);
        List<AlloyExpr> body = this.dsl.emptyExprList();
        String tout = DashFQN.translateFQN(tfqn);

        // guard
        if (this.dm.whenR(tfqn) != null) body.add(this.translateExpr(this.dm.whenR(tfqn)));

        if (!addGuardOnly) {
            if (!this.dm.hasOnlyOneState())
                // p3 -> p2 -> p1 -> src & s.confVar(i) != none
                // src does not have to be a basic state
                body.add(
                        AlloySome(
                                AlloyInter(
                                        this.translateDashRefToArrowExpr(this.dm.fromR(tfqn)),
                                        this.dsl.curConf(params.size()))));
            if (this.dm.hasConcurrency()) {
                // has a scope that is orthogonal to any scopes used
                List<DashRef> nonO = this.dm.nonOrthogonalScopesOf(tfqn);
                for (int i = 0; i <= this.dm.maxDepthParams(); i++) {
                    final Integer j = i;
                    List<AlloyExpr> u =
                            mapBy(
                                    filterBy(nonO, x -> x.hasNumParams(j)),
                                    x -> this.translateDashRefToArrowExpr(this.dsl.asScope(x)));
                    for (AlloyExpr x : u) body.add(AlloyNot(AlloyIn(x, this.dsl.curScopesUsed(i))));
                }
            }

            // event trigger
            // only one triggering event
            if (this.dm.hasConcurrency() && this.dm.onR(tfqn) != null && this.dm.hasEnvEvents()) {
                // trig_events_t1
                DashRef ev = this.dm.onR(tfqn);
                int sz = ev.paramValues.size();
                if (this.dm.isIntEvent(ev.name)) {
                    // triggered by internal event
                    // this transition cannot be triggered unless
                    // not stable
                    body.add(AlloyNot(this.dsl.curStableTrue()));
                } else {
                    AlloyExpr ifBranch =
                            AlloyIn(
                                    this.translateDashRefToArrowExpr(ev),
                                    this.dsl.RangeResLevel(
                                            this.dsl.curEvents(sz),
                                            this.dsl.allEnvEventsVar(),
                                            sz));

                    AlloyExpr elseBranch =
                            AlloyIn(this.translateDashRefToArrowExpr(ev), this.dsl.curEvents(sz));
                    body.add(AlloyIte(this.dsl.curStableTrue(), ifBranch, elseBranch));
                }
            } else if (this.dm.onR(tfqn) != null) {
                DashRef ev = this.dm.onR(tfqn);
                int sz = ev.paramValues.size();
                body.add(AlloyIn(this.translateDashRefToArrowExpr(ev), this.dsl.curEvents(sz)));
            }

            // not a higher priority transition enabled
            List<String> priTrans = this.dm.higherPriTrans(tfqn);
            List<AlloyExpr> args = this.dsl.emptyExprList();
            for (String t : priTrans) {
                // src must directly above this trans in the hierarchy
                // so its parameters must be a subset of the current parameters
                // and we don't need to quantify over them
                args = this.dsl.curParamVars(this.dm.transParams(t));
                body.add(
                        AlloyNot(AlloyPredCall(D2AStrings.preName(DashFQN.translateFQN(t)), args)));
            }
        }
        this.am.addPred(D2AStrings.preName(tout), this.dsl.curParamsDecls(params), body);
    }
}
