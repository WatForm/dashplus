package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashmodel.DashParam;
import java.util.*;

public class InvsD2A extends InitsD2A {

    /*
        TODO add Electrum
    */

    protected InvsD2A(DashModel dm, Options opt) {
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

                // this is a bit awkward (same as in InitsD2A.java):
                // Expr e uses the stateName of the param, but we need the Decl of the param
                List<String> prmStateNames = mapBy(prs, p -> p.asIndexValue().getName());

                // get the param stateNames used in the expression
                Set<String> prsUsed =
                        this.dsl.testAndCollect(
                                x -> prmStateNames.contains(((AlloyQnameExpr) x).getName()), e);

                // now get the Decls associated with those stateNames

                // there is certainly a more efficient way to do the above
                // but as there are only ever 1-2 parameters,
                // it is not worth it to make it more efficient

                for (int i = 0; i < prs.size(); i++) {
                    if (prsUsed.contains(prs.get(i).asIndexValue().getName())) {
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
            this.am.addFact(D2AStrings.invFactName, body);
        }
    }
}
