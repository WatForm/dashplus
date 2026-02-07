/*
	A DashParam is a new kind of DashExpr to refer to the parameter of a specific state.
	It includes the state name (fqn - so it is unique) and the sig of the parameter.

	A DashParam can be used in:
	- the parameter lists for states/trans/events/vars
	- in an expression (a parameter expression or otherwise)

	DashParam is an extension of DashExpr so it can go where Expr's go in an expression.

	This is completely an internally used DashExpr.

*/

package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
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
    public AlloyQnameExpr asAlloyVar() {
        return new AlloyQnameExpr(
                DashStrings.pName + DashStrings.alloySep + DashFQN.translateFQN(this.stateName));
    }

    // p_stateName:paramSig
    public AlloyDecl asAlloyDecl() {
        return new AlloyDecl(
                // name
                this.asAlloyVar(),
                // type
                new AlloyQnameExpr(this.paramSig));
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
