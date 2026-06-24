package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashmodel.DashParam;
import java.util.*;

public class InitsD2A extends SnapshotSigD2A {

    /*
        TODO add Electrum
    */

    protected InitsD2A(DashModel dm, Options opt) {
        super(dm, opt);
    }

    public void addInit() {
        this.addInit(true);
    }

    public void addInitVarsOnly() {
        this.addInit(false);
    }

    private void addInit(Boolean addConfAndScopeAndStable) {

        List<DashParam> prs = this.dm.allParams();
        List<AlloyExpr> body = this.dsl.emptyExprList();

        if (addConfAndScopeAndStable) body.addAll(this.addConfAndScopeAndStable());

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

                e = AlloyAndList(body);

                // this is a bit awkward (same as in InvsD2A.java)::
                // Expr e is an AlloyVar of the param name, but we need the Decl of the param
                List<String> prmStateNames = mapBy(prs, p -> p.asIndexValue().getName());

                // System.out.println(e.toString());
                // System.out.println(prmStateNames);
                // get the param stateNames used in the expression
                Set<String> prsUsed =
                        this.dsl.testAndCollect(
                                x -> prmStateNames.contains(((AlloyQnameExpr) x).getName()), e);
                // System.out.println("params used: " + prsUsed.toString());

                // all param1. all param3. ... body
                // but all the parameters are not used in init
                // now get the Decls associated with those stateNames
                decls = this.dsl.emptyDeclList();
                for (int i = 0; i < prs.size(); i++) {
                    if (prsUsed.contains(prs.get(i).asIndexValue().getName())) {
                        // if (this.dsl.containsVar(e, prs.get(i).asAlloyVar())) {
                        decls.add(prs.get(i).asAlloyDecl());
                    }
                }
                // System.out.println(decls);
                // there is certainly a more efficient way to do the above
                // but as there are only ever 1-2 parameters,
                // it is not worth it to make it more efficient

                if (!decls.isEmpty()) {
                    e = AlloyAllVars(decls, e);
                    body = this.dsl.emptyExprList();
                    body.add(e);
                }
            }
        }

        // init is a reserved word in Electrum
        if (this.isElectrum) {
            this.am.addPred(D2AStrings.initPredName, this.dsl.emptyDeclList(), body);
        } else {
            // snapshot will always be needed as a parameter
            // because it is used in conf (every model has at least one state)
            this.am.addPred(D2AStrings.initPredName, this.dsl.curDecls(), body);
        }
    }

    private List<AlloyExpr> addConfAndScopeAndStable() {

        List<AlloyExpr> body = emptyList();
        if (!this.dm.hasOnlyOneState()) {
            // forall i. confi = default entries
            List<DashRef> entered = this.dm.rootLeafStatesEntered();
            if (entered.isEmpty())
                // everything must have a default in initialization of state table
                // so this should be impossible
                shouldNotReach();
            for (int i = 0; i <= this.dm.maxDepthParams(); i++) {
                // Java requires local var used in lambda to be final
                if (this.dm.hasStatesAti(i)) {
                    final int numParams = i;
                    // entered comes back in terms of DashRefs and DashParams
                    List<AlloyExpr> ent =
                            mapBy(
                                    filterBy(entered, x -> x.hasNumParams(numParams)),
                                    y -> this.translateDashRefToArrowExpr(y));
                    if (!ent.isEmpty()) body.add(AlloyEqual(this.dsl.curConf(i), AlloyUnion(ent)));
                    else body.add(AlloyEqual(this.dsl.curConf(i), this.dsl.noneArrow(i)));
                }
            }
        }
        for (int i = 0; i <= this.dm.maxDepthParams(); i++) {

            // scopesUsedi = none
            if (this.dm.hasConcurrency())
                body.add(AlloyEqual(this.dsl.curScopesUsed(i), this.dsl.noneArrow(i)));

            if (this.dm.hasTransAti(i))
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
        if (this.dm.hasConcurrency()) body.add(this.dsl.curStableTrue());
        return body;
    }
}
