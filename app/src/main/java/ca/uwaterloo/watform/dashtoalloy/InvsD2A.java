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

public class InvsD2A extends InitsD2A {

    /*
        TODO add Electrum
    */      

    protected InvsD2A(DashModel dm, boolean isElectrum) {
        super(dm, isElectrum);
    }

    public void addInvs() {

        List<DashParam> prs = this.dm.allParamsInOrder();
        List<AlloyExpr> body = new ArrayList<AlloyExpr>();
       
        // since this is a fact, we don't need it if there are no invariants 
        if (!this.dm.invsR().isEmpty()) {
            for (AlloyExpr i: this.dm.invsR())
                // these may have the use of parameters in them
                body.add(this.translateExpr(i));

            // it was really tricky to get these types/lists right
            // so don't try to combine these steps

            AlloyExpr e;
            List<AlloyDecl> decls = new ArrayList<AlloyDecl>();

            if (!prs.isEmpty()) {
                // all parameters are not used in inv
                e = AlloyAndList(body);
                for (int i=0; i < prs.size();i++) {
                    if (this.dsl.containsVar(e, prs.get(i).asAlloyVar())) {
                        decls.add(prs.get(i).asAlloyDecl());
                    }
                }
                if (!decls.isEmpty()) {
                    // all universally quantified
                    e = AlloyAll(decls,e);
                    body = new ArrayList<AlloyExpr>();
                    body.add(e);
                }      
            } 
            // body is either a list of expr or 
            // a list containing single universally quantified expr over the parameters 

            // remember invs is a fact, not a pred
            if (!this.isElectrum && this.dsl.containsVar(body, this.dsl.curVar())) {
                decls = new ArrayList<AlloyDecl>();
                decls.add(this.dsl.curDecl());
                e = AlloyAll(decls, AlloyAndList(body));
                body = new ArrayList<AlloyExpr>();
                body.add(e);
            } 
            this.addFact(D2AStrings.invFactName, body);
        }
    }
}