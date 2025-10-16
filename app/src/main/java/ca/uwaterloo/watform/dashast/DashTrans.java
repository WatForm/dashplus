package ca.uwaterloo.watform.dashast;

import java.util.List;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import ca.uwaterloo.watform.utils.*;

public class DashTrans extends Dash {
	String name;
	List<Object> items;

	public DashTrans(Pos p, String n, List<Object> i) {
		assert(n != null);
		assert(i != null);
		this.pos = p;
		this.name = n;
		this.items = i;
	}

	public String toString(Integer i) {
		String s = new String("");
		String ind = DashStrings.indent(i); 
		if (items.isEmpty()) {
			s += ind + DashStrings.transName + " " + name + " { }\n";
		} else { 
		    s += ind + DashStrings.transName + " " + name + " {\n";
			StringJoiner j = new StringJoiner("");
 			items.forEach(k -> j.add(((Dash) k).toString(i+1)));
			s += j.toString();
			s += ind + "}\n";
		}
		return s;
	}
}
