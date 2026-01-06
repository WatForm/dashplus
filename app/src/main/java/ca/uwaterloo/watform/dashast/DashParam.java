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

import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
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
        this.stateName = stateName;
        this.paramSig = paramSig;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        // seems like a reasonable thing to return for the toString of this Expr
        sb.append(stateName.toString());
    }

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.append(stateName);
    }

    // it's convenient to include these translation methods here

    // p_stateName

    public AlloyQnameExpr paramVar() {
        return new AlloyQnameExpr(DashStrings.pName + DashStrings.alloySep + this.stateName);
    }

    // p_stateName:paramSig
    public AlloyDecl paramDecl() {
        return new AlloyDecl(
                // name
                paramVar(),
                // type
                new AlloyQnameExpr(this.paramSig));
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
        //throw ImplementationError.methodShouldNotBeCalled("DashParam/accept for AlloyExprVis "+this.getClass());
    }

    /*
    public <T> T accept(DashExprVis<T> visitor) {
        return visitor.visit(this);
    }
    */
}
