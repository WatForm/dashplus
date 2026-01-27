package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.List;

public class InvsD2A extends InitsD2A {

    /*
        TODO add Electrum
    */

    protected InvsD2A(DashModel dm, TranslateOutput opt) {
        super(dm, opt);
    }

    public void addInvs() {

        List<DashParam> prs = this.dm.allParams();
        List<AlloyExpr> body = this.dsl.emptyExprList();

        // since this is a fact, we don't need it if there are no invariants
        if (!this.dm.invsR().isEmpty()) {
            for (AlloyExpr i : this.dm.invsR())
                // these may have the use of parameters in them
                body.add(this.translateExpr(i));

            // it was really tricky to get these types/lists right
            // so don't try to combine these steps

            AlloyExpr e;
            List<AlloyDecl> decls = this.dsl.emptyDeclList();

            if (!prs.isEmpty()) {
                // all parameters are not used in inv
                e = AlloyAndList(body);
                for (int i = 0; i < prs.size(); i++) {
                    if (this.dsl.containsVar(e, prs.get(i).asAlloyVar())) {
                        decls.add(prs.get(i).asAlloyDecl());
                    }
                }
                if (!decls.isEmpty()) {
                    // all universally quantified
                    e = AlloyAllVars(decls, e);
                    body = this.dsl.emptyExprList();
                    body.add(e);
                }
            }
            // body is either a list of expr or
            // a list containing single universally quantified expr over the parameters

            // remember invs is a fact, not a pred
            if (!this.isElectrum && this.dsl.containsVar(body, this.dsl.curVar())) {
                decls = this.dsl.emptyDeclList();
                decls.add(this.dsl.curDecl());
                e = AlloyAllVars(decls, AlloyAndList(body));
                body = this.dsl.emptyExprList();
                body.add(e);
            }
            this.addFact(D2AStrings.invFactName, body);
        }
    }
}
