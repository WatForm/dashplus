/*
    A DashParam is a (stateName, paramSig) that represents the declaration of a set of indices for a state in Dash.

    There are three ways a DashParam is used:
    - asAlloyDecl: AlloyDecl(Var(p_stateName), AlloyExpr(one AlloyVar(sigName)))
    - asIndexValue: AlloyVar(p_stateName) - "this" parameter value
    - asWholeSet: AlloyVar(sigName) - set of all parameter values

	A DashParam is used only in DashModel in:
	- the parameter lists for states/trans/events/vars
    - the list of allParams

    In DashModel initialize:
    - "thisStatename" is replaced with dashParam.asIndexValue()
    - in resolution of all states, events, etc to DashRefs, dashParam.asIndexValue() is used to represent the value in this state
    - Note: a user could write 'p_Statename' as a value in any expression

    In DashToAlloy:
    - DashRefs for entered/exited use dashParam.asIndexValue() to refer to particular value of dashParam.asWholeSet() when we need the whole set
    - list of all DashParams is used to create signatures in SpaceSignatures
    - various place DashParams are used to create decls (for trans predicates, for invs, inits, smallstep, etc.)

*/

package ca.uwaterloo.watform.dashmodel;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public class DashParam extends AlloyExpr {

    public final String stateName;
    public final String paramSig;

    public DashParam(String stateName, String paramSig) {
        // for compatibility with Expr
        // NADTODO is DashParam only ever created and never parsed?
        // super();
        // does not have a pos of origin
        super(Pos.UNKNOWN);
        this.stateName = stateName; // fqn
        this.paramSig = paramSig;
    }

    @Override
    public void pp(PrintContext pCtx) {
        // pCtx.append(stateName);
        pCtx.append(paramSig);
    }

    // it's convenient to include these translation methods here

    // this is used in Resolve where a parameter is used
    // as a paramValue of a DashRef
    // p_stateName
    public AlloyQnameExpr asIndexValue() {
        return new AlloyQnameExpr(
                DashStrings.pName + DashStrings.alloySep + DashFQN.translateFQN(this.stateName));
    }

    public AlloyQnameExpr asWholeSet() {
        // just the name of the parameter; set of all its values
        return new AlloyQnameExpr(DashFQN.translateFQN(this.paramSig));
    }

    // p_stateName:paramSig
    public AlloyDecl asAlloyDecl() {
        return new AlloyDecl(
                // name
                this.asIndexValue(), AlloyQtEnum.ONE, this.asWholeSet());
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
        // throw ImplementationError.methodShouldNotBeCalled("DashParam/accept for AlloyExprVis
        // "+this.getClass());
    }

    @Override
    public int getPrec() {
        return AlloyExpr.NO_PAREN;
    }
}
