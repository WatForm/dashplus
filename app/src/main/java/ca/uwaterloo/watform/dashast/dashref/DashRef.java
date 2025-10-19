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

//import java.util.Collection;
//import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.utils.Pos;
import ca.uwaterloo.watform.utils.GeneralUtil;
import static ca.uwaterloo.watform.dashmodel.DashFQN.*;

public class DashRef extends AlloyExpr  {

	private DashRefKind kind;
	private String name;
	private List<AlloyExpr> paramValues;

	// generally in the code we know the kind by context but
	// for printing we need the kind here
	// and this simplified some code for the DashRef to know its kind
	static enum DashRefKind {
		STATE,
		EVENT,
		VAR,
		TRANS
		// BUFFER ????
	}

	// for internal uses during translation
	public DashRef(DashRefKind k, String n, List<AlloyExpr> prmValues) {
		this.kind = k;
		this.name = n;
		this.paramValues = prmValues;	
	}

	// for uses when parsed
	public DashRef(Pos p, DashRefKind k, String n, List<AlloyExpr> prmValues) {
		super(p);
		this.kind = k;
		this.name = n;
		this.paramValues = prmValues;	
	}

	public static List<AlloyExpr> emptyParamValuesList() {
		return new ArrayList<AlloyExpr>();
	}

	@Override
	public void toString(StringBuilder sb, int indent) {
		// STATE: Root/A/B[a1,b1]
		// other: Root/A/B[a1,b1]/var1
		String s = "";
		if (kind == DashRefKind.STATE) {
			s += getName();
		} else {
			s += chopPrefixFromFQN(getName());
		}
        
        if (!paramValues.isEmpty()) {
        	s += "[";
	        List<String> paramValues = 
	            getParamValues().stream()
	            .map(i -> i.toString())
	            .collect(Collectors.toList());
	        //Collections.reverse(paramValues);
	        s += GeneralUtil.strCommaList(paramValues);
	        s += "]";
	    }
        if (kind != DashRefKind.STATE) {
        	s += "/";
        	s += chopNameFromFQN(getName());
        }
        //System.out.println(s + "\n");
        sb.append(s);
	}

	// getters
	public String getName() {
		return name;
	}
	public List<AlloyExpr> getParamValues() {
		return paramValues;
	}

    // referencing a for loop variable in a filter does not work
    // so do this as a loop
    public static List<DashRef> hasNumParams(List<DashRef> dr, int i) {
        // filter to ones that have this number of params
        List<DashRef> o = dr.stream()
            .filter(x -> x.getParamValues().size() == i)
            .collect(Collectors.toList()); 
        return o;
    }
    
}