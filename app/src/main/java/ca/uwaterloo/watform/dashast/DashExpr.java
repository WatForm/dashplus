/* 
	These classes are used only during parsing because
	we do not know what order items within a state will be parsed in.
*/

package ca.uwaterloo.watform.dashast;

import java.util.Collections;

import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashStrings;
//import ca.uwaterloo.watform.alloyasthelper.ExprHelper;
// use this one, rather than regular Alloy Expr .toString
// so we can control the printing of the parameters in DashRefs
//import ca.uwaterloo.watform.alloyasthelper.ExprToString;


public abstract class DashExpr extends Dash {	

    public AlloyExpr exp;
    public Pos pos;

    public DashExpr(Pos p, AlloyExpr e) {
        assert(e != null);
        this.pos = p;
        this.exp = e; 
    }
    public String toString(String name, Integer i) {
        String s = new String();
        s += DashStrings.indent(i) + name + " {\n";

        // Alloy seems to put a NOOP on the front of the expression
        //Expr e = exp;
        //while (ExprHelper.isExprNoop(e)) {
        //    e = ExprHelper.getSub(e);
        //}

        // we don't want to translateExpr here b/c that includes s and s' in the output expression
        
        /*
        if (ExprHelper.isExprAndList(e)) {

            // Drop the "AND[p1,p2 ]" and print p1 and p2 on separate lines
            for (Expr a: ExprHelper.getExprListArgs(e)) {
                // have to call a new ExprToString each time b/c on exprToString call closes it 
                ep = new ExprToString(true); // true b/c withinDash printing expression
                s += DashStrings.indent(i+1) + ep.exprToString(a) + "\n";
            }
        } else {
            ep = new ExprToString(true); // true -> b/c withinDash printing expression
        */
        //NADTODO pass toString an Int parameter for indentation
        s += exp.toString() + "\n";
        //}
        s += DashStrings.indent(i) + "}\n";
        return s; 
    }
    public AlloyExpr getExp() {
        return exp;
    }
}
