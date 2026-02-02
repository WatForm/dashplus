package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashmodel.DashFQN;
import ca.uwaterloo.watform.dashmodel.DashModel;
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

    protected TransPreD2A(DashModel dm, TranslateOutput opt) {
        super(dm, opt);
    }

    public void addTransPre(String tfqn) {
        List<DashParam> params = this.dm.transParams(tfqn);
        List<AlloyExpr> body = this.dsl.emptyExprList();
        String tout = DashFQN.translateFQN(tfqn);

        if (!this.dm.hasOnlyOneState())
            // p3 -> p2 -> p1 -> src & s.confVar(i) != none
            // src does not have to be a basic state
            body.add(
                    AlloySome(
                            AlloyInter(
                                    this.dm.fromR(tfqn).asAlloyArrow(),
                                    this.dsl.curConf(params.size()))));

        if (this.dm.whenR(tfqn) != null) body.add(this.translateExpr(this.dm.whenR(tfqn)));

        if (this.dm.hasConcurrency()) {
            // has a scope that is orthogonal to any scopes used
            List<DashRef> nonO = this.dm.nonOrthogonalScopesOf(tfqn);
            for (int i = 0; i <= this.dm.maxDepthParams(); i++) {
                final Integer j = i;
                List<AlloyExpr> u =
                        mapBy(
                                filterBy(nonO, x -> x.hasNumParams(j)),
                                x -> this.dsl.asScope(x).asAlloyArrow());
                for (AlloyExpr x : u) body.add(AlloyNot(AlloyIn(x, this.dsl.curScopesUsed(i))));
            }
        }

        // event trigger
        // only one triggering event
        if (this.dm.hasConcurrency() && this.dm.onR(tfqn) != null && this.dm.hasEnvEvents()) {
            // trig_events_t1
            DashRef ev = this.dm.onR(tfqn);
            int sz = ev.paramValues.size();
            AlloyExpr ifBranch;
            if (this.dm.isIntEvent(ev.name)) {
                ifBranch = AlloyFalseCond();
            } else {
                ifBranch =
                        AlloyIn(
                                ev.asAlloyArrow(),
                                AlloyRangeRes(this.dsl.curEvents(sz), this.dsl.allEnvEventsVar()));
            }
            AlloyExpr elseBranch = AlloyIn(ev.asAlloyArrow(), this.dsl.curEvents(sz));
            body.add(AlloyIte(this.dsl.curStableTrue(), ifBranch, elseBranch));
        } else if (this.dm.onR(tfqn) != null) {
            DashRef ev = this.dm.onR(tfqn);
            int sz = ev.paramValues.size();
            body.add(AlloyIn(ev.asAlloyArrow(), this.dsl.curEvents(sz)));
        }

        // not a higher priority transition enabled
        List<String> priTrans = this.dm.higherPriTrans(tfqn);
        List<AlloyExpr> args = this.dsl.emptyExprList();
        for (String t : priTrans) {
            // src must directly above this trans in the hierarchy
            // so its parameters must be a subset of the current parameters
            // and we don't need to quantify over them
            args = this.dsl.curParamVars(this.dm.transParams(t));
            body.add(AlloyNot(AlloyPredCall(DashFQN.translateFQN(t) + D2AStrings.preName, args)));
        }

        this.am.addPred(tout + D2AStrings.preName, this.dsl.curParamsDecls(params), body);
    }
}
