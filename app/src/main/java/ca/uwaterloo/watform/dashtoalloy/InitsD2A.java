package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.List;

public class InitsD2A extends SnapshotSigD2A {

    /*
        TODO add Electrum
    */

    protected InitsD2A(DashModel dm, TranslateOutput opt) {
        super(dm, opt);
    }

    public void addInit() {

        List<DashParam> prs = this.dm.allParams();
        List<AlloyExpr> body = this.dsl.emptyExprList();

        if (!this.dm.hasOnlyOneState()) {
            // forall i. confi = default entries
            List<DashRef> entered = this.dm.rootLeafStatesEntered();
            if (entered.isEmpty())
                // everything must have a default in initialization of state table
                // so this should be impossible
                shouldNotReach();
            for (int i = 0; i <= this.dm.maxDepthParams(); i++) {
                // Java required local var used in lambda to be final
                final int numParams = i;
                List<AlloyExpr> ent =
                        mapBy(
                                filterBy(entered, x -> x.hasNumParams(numParams)),
                                y -> y.asAlloyArrow());
                if (!ent.isEmpty()) body.add(AlloyEqual(this.dsl.curConf(i), AlloyUnionList(ent)));
                else body.add(AlloyEqual(this.dsl.curConf(i), this.dsl.noneArrow(i)));
            }
        }
        for (int i = 0; i <= this.dm.maxDepthParams(); i++) {

            // scopesUsedi = none
            if (this.dm.hasConcurrency())
                body.add(AlloyEqual(this.dsl.curScopesUsed(i), this.dsl.noneArrow(i)));

            body.add(AlloyEqual(this.dsl.curTransTaken(i), this.dsl.noneArrow(i)));

            // no limits on initial set of events except that they must be environmental
            // s.events1 :> internalEvents = none -> none
            if (this.dm.hasIntEventsAti(i))
                // s.events0 inter IntEvents = none
                // or
                // s.event1 :> IntEvents = none -> none
                body.add(
                        AlloyEqual(
                                this.dsl.RangeResLevel(
                                        this.dsl.curEvents(i), this.dsl.allIntEventsVar(), i),
                                this.dsl.noneArrow(i)));
        }

        // even if these are empty we need this predicate to exist
        for (AlloyExpr i : this.dm.initsR())
            // these may have the use of parameters in them
            body.add(this.translateExpr(i));

        // it was really tricky to get these types/lists right
        // so don't try to combine these steps

        AlloyExpr e;
        List<AlloyDecl> decls;
        if (!body.isEmpty()) {
            if (!prs.isEmpty()) {
                // all param1. all param3. ... body
                // but all the parameters are not used in init
                decls = this.dsl.emptyDeclList();
                e = AlloyAndList(body);
                // FIXING HERE
                for (int i = 0; i < prs.size(); i++) {
                    if (this.dsl.containsVar(e, prs.get(i).asAlloyVar())) {
                        decls.add(prs.get(i).asAlloyDecl());
                    }
                }
                if (!decls.isEmpty()) {
                    e = AlloyAllVars(decls, e);
                    body = this.dsl.emptyExprList();
                    body.add(e);
                }
            }
        }
        if (this.dm.hasConcurrency()) body.add(this.dsl.curStableTrue());

        // init is a reserved word in Electrum
        if (this.isElectrum) {
            this.am.addPred(D2AStrings.initPredName, this.dsl.emptyDeclList(), body);
        } else {
            // snapshot will always be needed as a parameter
            // because it is used in conf (every model has at least one state)
            this.am.addPred(D2AStrings.initPredName, this.dsl.curDecls(), body);
        }
    }
}
