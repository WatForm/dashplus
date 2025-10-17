package ca.uwaterloo.watform.dashast;

import java.util.*;

import java.util.List;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.Collections;
import java.util.stream.Collectors;

import ca.uwaterloo.watform.utils.Pos;
import ca.uwaterloo.watform.utils.ASTNode;
import ca.uwaterloo.watform.utils.CodingError;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;

public class DashState extends DashParagraph {

	// stuff from parsing
	public String name;
	//private String sfqn; // set during resolveAllState
	private String param; 
	private DashStrings.StateKind kind; // basic state = OR with no subStates
	private DashStrings.DefKind def; 
	private List<Object> items;
	private int tabsize = 2;

	public DashState(
			Pos pos, 
			String n, 
			String prm, 
			DashStrings.StateKind k, 
			DashStrings.DefKind d, 
			List<Object> i) {
		super(pos);
		assert(n != null);
		assert(i != null);
		this.name = n;
		this.param = prm;
		this.kind = k;
		this.def = d;
		this.items = i;	
	}

	// to sort the items in a state for display
	// this order is very arbitrary
	
	public Integer itemToInt(Object i) {
		 
		if (DashVarDecls.class.isInstance(i)) return 1;
		else if (DashBufferDecls.class.isInstance(i)) return 2;
		else if (DashEventDecls.class.isInstance(i)) return 3;
		else if (DashInit.class.isInstance(i)) return 4;
		else if (DashInv.class.isInstance(i)) return 5;
		else if (DashTrans.class.isInstance(i)) return 6;
		else if (DashState.class.isInstance(i)) return 7;	
		else {
			CodingError.missingCase("itemToInt");
			return 0;
		}
	}

	@Override
	public void toString(StringBuilder sb, int indent) {
		String ind = DashStrings.indent(indent); 
		String s = new String(ind);
		if (def == DashStrings.DefKind.DEFAULT) {
			s += DashStrings.defaultName + " ";
		}
		if (kind == DashStrings.StateKind.AND) {
			s += DashStrings.concName +" ";
		}
		if (items.isEmpty()) {
			s += DashStrings.stateName + " " + name + " {}\n";
		} else { 
		    s += DashStrings.stateName + " " + name;
			if (param == null) s += " {\n";
			else s += " [" + param + "] {\n";
			StringJoiner j = new StringJoiner("");
			// sorting items for display order
			// map type of item to an integer (in function above)
			Collections.sort(items, 
				(i1, i2) -> Integer.compare(itemToInt(i1),itemToInt(i2)
)				);
 			items.forEach(k -> j.add(((ASTNode) k).toString(indent+1)));
			s += j.toString() + ind + "}\n";

		}
		sb.append(s);
	}

	public static String noParam() {
		return null;
	}
	public static List<Object> noSubstates() {
		return new ArrayList<Object>();
	}
	
}
