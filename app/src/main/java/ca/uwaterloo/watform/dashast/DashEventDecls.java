package ca.uwaterloo.watform.dashast;

import java.util.List;
import java.util.StringJoiner;
import java.util.Collections;

import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.dashast.DashStrings;

public class DashEventDecls extends ASTNode {

	private List<String> names;
	private DashStrings.IntEnvKind kind; 

	public DashEventDecls(Pos pos, List<String> n, DashStrings.IntEnvKind kind) {
		super(pos);
		assert(n != null);
		this.names = n;
		this.kind = kind;
	}
	@Override
	public void toString(StringBuilder sb, int indent) {
		String s = new String("");
		if (kind == DashStrings.IntEnvKind.ENV) {
			s += DashStrings.envName + " ";
		}
		StringJoiner sj = new StringJoiner(",\n");
        names.forEach(n -> sj.add(n));
		s += DashStrings.eventName + " " + sj.toString() +" {}\n";
		sb.append(DashStrings.indent(indent) + s);
	}
	public List<String> getNames() {
		return names;
	}
	public DashStrings.IntEnvKind getKind() {
		return kind;
	}
}
