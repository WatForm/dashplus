package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.dashtoalloy.AlloyHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashmodel.DashFQN;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InitsD2A extends SnapshotSigD2A {

    /*
        TODO add Electrum
    */      

    protected InitsD2A(DashModel dm, boolean isElectrum) {
        super(dm, isElectrum);
    }
 
    public void addInit() {

        List<DashParam> prs = this.dm.allParamsInOrder();
        List<AlloyExpr> body = new ArrayList<AlloyExpr>();
        
        if (!this.dm.hasOnlyOneState()) {
            // forall i. confi = default entries
            List<DashRef> entered = this.dm.rootLeafStatesEntered();
            if (entered.isEmpty()) 
            	// everything must have a default in initialization of state table
            	// so this should be impossible
                shouldNotReach();
            for (int i=0;i <= this.dm.maxDepthParams(); i++) {
                // Java required local var used in lambda to be final
                final int numParams = i;
                List<AlloyExpr> ent = 
                	mapBy(
                		filterBy(entered, x -> x.hasNumParams(numParams)),
                		y -> this.dsl.DashRefToArrow(y));
                if (!ent.isEmpty()) 
                	body.add(AlloyEqual(this.dsl.curConf(i),AlloyUnionList(ent)));
                else 
                	body.add(AlloyEqual(this.dsl.curConf(i), AlloyNone()));
            }
        }
        for (int i = 0; i <= this.dm.maxDepthParams(); i++) {
            
            // scopesUsedi = none
            if (this.dm.hasConcurrency())
                body.add(AlloyEqual(this.dsl.curScopesUsed(i),this.dsl.noneArrow(i)));

            body.add(AlloyEqual(this.dsl.curTransTaken(i),this.dsl.noneArrow(i)));
            
            // no limits on initial set of events except that they must be environmental
            //s.events1 :> internalEvents = none -> none
            if (this.dm.hasIntEventsAti(i))
                body.add(AlloyEqual(
                    AlloyRangeRes(this.dsl.curEvents(i), this.dsl.allIntEventsVar()),
                    this.dsl.noneArrow(i)));
        }
        
        // even if these are empty we need this predicate to exist
        for (AlloyExpr i: this.dm.initsR())
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
                decls = new ArrayList<AlloyDecl>();
                e = AlloyAndList(body);
                for (int i=0; i < prs.size();i++) {
                    if (this.dsl.containsVar(e, prs.get(i).asAlloyVar())) {
                        decls.add(prs.get(i).asAlloyDecl());
                    }
                }
                if (!decls.isEmpty()) 
                    e = AlloyAll(decls,e);
                body = new ArrayList<AlloyExpr>();
                body.add(e);
            }
        }
        if (this.dm.hasConcurrency()) 
        	body.add(this.dsl.curStableTrue());

        // init is a reserved word in Electrum
        if (this.isElectrum) {
            List<AlloyDecl> emptyDecls = new ArrayList<AlloyDecl>();
            this.addPred(
            	D2AStrings.initFactName, 
            	emptyDecls, 
            	body);
        } else {
            // snapshot will always be needed as a parameter
            // because it is used in conf (every model has at least one state)
            this.addPred(
                D2AStrings.initFactName, 
                this.dsl.curDecls(),
                body);
        }
    }
 }	