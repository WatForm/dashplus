/*
	These static methods let us treat an DashExpr as a reference to a Dash object
	that has a name and a list of parameter values.

	From Root/A/B[exp1,exp2]/v1 in parsing
	a DashRef is recorded in the AST as $$PROCESSREF$$ . exp2 . exp1 . Root/A/B/v1

	After resolving, a DashRef with no params is $$PROCESSREF$$. var1

	These references can be within DashExpr so we can't do a class extension.

	Even though we could do something different for states/events
	(where they aren't referenced within DashExpr)
	its best to use the same functions for all

	Note: we cannot allow any of
	b1 -> a1 -> var1
	b1.a1.var1
	var1[a1,b1]
	as a way to reference vars or events with parameter values b/c we cannot
	tell the difference between the above and something like Chairs.occupied'
	where "Chairs" is not a parameter value but a something to be joined with
	occupied after it has all of its parameter values.
*/

package ca.uwaterloo.watform.dashast.dashref;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.parser.Parser.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class DashRef extends AlloyExpr {

    public final String name;
    public final List<AlloyExpr> paramValues;

    protected DashRef(Pos p, String n, List<? extends AlloyExpr> prmValues) {
        super(p); // for pos
        this.name = n;
        this.paramValues = Collections.unmodifiableList(prmValues);
    }

    protected DashRef(String n, List<? extends AlloyExpr> prmValues) {
        this(Pos.UNKNOWN, n, prmValues);
    }

    protected DashRef(Pos p, List<AlloyNameExpr> names, List<? extends AlloyExpr> prmValues) {

        String n =
                names.stream()
                        .map(AlloyNameExpr::toString)
                        .collect(Collectors.joining(DashStrings.internalQualChar));
        this(p, n, prmValues);
    }

    public static List<AlloyExpr> emptyParamValuesList() {
        return new ArrayList<AlloyExpr>();
    }

    public AlloyExpr asAlloyArrow() {
        // p1 -> p2 -> fqn
        // used for initialization and
        // checking elements in conf/events
        assert (this instanceof VarDashRef && !((VarDashRef) this).isNext);
        List<AlloyExpr> ll = reverse(this.paramValues);
        ll.add(AlloyVar(DashFQN.translateFQN(this.name)));
        return AlloyArrowExprList(ll);
    }

    @Override
    public void pp(PrintContext pCtx) {
        // pp within Dash state (not in AlloyModel, where it should not exist)
        // STATE: Root/A/B[a1,b1]
        // other: Root/A/B[a1,b1]/var1
        String s = "";
        if (!paramValues.isEmpty()) {
            // then it has to be at least partially resolved already
            if (this instanceof StateDashRef) {
                s += name;
            } else {
                s += DashFQN.chopPrefixFromFQN(name);
            }
            s += "[";
            s += GeneralUtil.strCommaList(paramValues);
            s += "]";
            if (this instanceof StateDashRef) {
                s += "/";
                s += DashFQN.chopNameFromFQN(name);
            }
        } else {
            s += name;
        }
        pCtx.append(s);
    }

    public boolean hasNumParams(int i) {
        return this.paramValues.size() == i;
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    public abstract DashStrings.DashRefKind kind();

    public boolean isNext() {
        return (this instanceof VarDashRef && ((VarDashRef) this).isNext);
    }

    @Override
    public int getPrec() {
        return AlloyExpr.NO_PAREN;
    }
}
