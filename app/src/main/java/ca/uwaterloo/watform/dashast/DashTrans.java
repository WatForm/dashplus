package ca.uwaterloo.watform.dashast;

import java.util.List;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import ca.uwaterloo.watform.utils.*;

public class DashTrans extends ASTNode {
	String name;
	List<Object> items;

	public DashTrans(Pos pos, String n, List<Object> i) {
		super(pos);
		assert(n != null);
		assert(i != null);
		this.name = n;
		this.items = i;
	}
	@Override
	public void toString(StringBuilder sb, int indent) {
		String s = new String("");
		String ind = DashStrings.indent(indent); 
		if (items.isEmpty()) {
			s += ind + DashStrings.transName + " " + name + " { }\n";
		} else { 
		    s += ind + DashStrings.transName + " " + name + " {\n";
			StringJoiner j = new StringJoiner("");
 			items.forEach(k -> j.add(((ASTNode) k).toString(indent+1)));
			s += j.toString();
			s += ind + "}\n";
		}
		sb.append(s);
	}
}
