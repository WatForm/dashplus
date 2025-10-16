package ca.uwaterloo.watform.dashast;

import java.util.List;
import java.util.StringJoiner;
import java.util.Collections;

import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.dashast.DashStrings;

public class DashEventDecls extends Dash {

	private List<String> names;
	private DashStrings.IntEnvKind kind; 

	public DashEventDecls(Pos pos, List<String> n, DashStrings.IntEnvKind kind) {
		assert(n != null);
		this.pos = pos;
		this.names = n;
		this.kind = kind;
	}

	public String toString(Integer i) {
		String s = new String("");
		if (kind == DashStrings.IntEnvKind.ENV) {
			s += DashStrings.envName + " ";
		}
		StringJoiner sj = new StringJoiner(",\n");
        names.forEach(n -> sj.add(n));
		s += DashStrings.eventName + " " + sj.toString() +" {}\n";
		return DashStrings.indent(i) + s;
	}
	public List<String> getNames() {
		return names;
	}
	public DashStrings.IntEnvKind getKind() {
		return kind;
	}
}
