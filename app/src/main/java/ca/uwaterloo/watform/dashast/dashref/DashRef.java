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

public class DashRef extends AlloyExpr {

    public final DashStrings.DashRefKind kind;
    public final String name;
    public final List<AlloyExpr> paramValues;
    public final boolean isNext;

    // for internal uses during translation
    public DashRef(DashStrings.DashRefKind k, String n, List<? extends AlloyExpr> prmValues) {
        this(Pos.UNKNOWN, k, n, prmValues);
    }

    public DashRef(
            DashStrings.DashRefKind k,
            String n,
            List<? extends AlloyExpr> prmValues,
            boolean isNext) {
        this(Pos.UNKNOWN, k, n, prmValues, isNext);
    }

    private DashRef(
            Pos p,
            DashStrings.DashRefKind k,
            String n,
            List<? extends AlloyExpr> prmValues,
            boolean isNext) {
        super(p);
        this.kind = k;
        this.name = n;
        this.paramValues = Collections.unmodifiableList(prmValues);
        this.isNext = isNext;
    }

    // used in parsing
    public DashRef(
            Pos p, DashStrings.DashRefKind k, String n, List<? extends AlloyExpr> prmValues) {
        this(p, k, n, prmValues, false);
    }

    // used in parsing??
    public DashRef(
            Pos p,
            DashStrings.DashRefKind k,
            List<AlloyNameExpr> names,
            List<? extends AlloyExpr> prmValues) {

        String n =
                names.stream()
                        .map(AlloyNameExpr::toString)
                        .collect(Collectors.joining(DashStrings.internalQualChar));
        this(p, k, n, prmValues, false);
    }

    public static List<AlloyExpr> emptyParamValuesList() {
        return new ArrayList<AlloyExpr>();
    }

    // only way to make a "next" DashRef (which happens in resolving)
    public DashRef makeNext() {
        assert (!this.isNext);
        return new DashRef(this.pos, this.kind, this.name, this.paramValues, true);
    }

    public AlloyExpr asAlloyArrow() {
        // p1 -> p2 -> fqn
        // used for initialization and
        // checking elements in conf/events
        assert (!this.isNext);
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
            if (kind == DashStrings.DashRefKind.STATE) {
                s += name;
            } else {
                s += DashFQN.chopPrefixFromFQN(name);
            }
            s += "[";
            s += GeneralUtil.strCommaList(paramValues);
            s += "]";
            if (kind != DashStrings.DashRefKind.STATE) {
                s += "/";
                s += DashFQN.chopNameFromFQN(name);
            }
        } else {
            s += name;
        }
        pCtx.append(s);
    }

    // referencing a for loop variable in a filter does not work
    // so do this as a loop
    /*
    public static List<DashRef> hasNumParams(List<DashRef> dr, int i) {
        // filter to ones that have this number of params
        List<DashRef> o = filterBy(dr, x -> x.paramValues.size() == i);
        return o;
    }
    */
    public boolean hasNumParams(int i) {
        return this.paramValues.size() == i;
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
