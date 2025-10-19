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

import ca.uwaterloo.watform.utils.Pos;

public class DashParam extends DashExpr {

	public final String stateName;
	public final String paramSig;

	public DashParam(String stateName, String paramSig) {
		// for compatibility with Expr
        //NADTODO is DashParam only ever created and never parsed?
		super();

		this.stateName = stateName;
		this.paramSig = paramSig;
	}
	@Override
    public void toString(StringBuilder sb, int indent) {
    	// seems like a reasonable thing to return for the toString of this Expr
    	sb.append(stateName.toString());
    }



}