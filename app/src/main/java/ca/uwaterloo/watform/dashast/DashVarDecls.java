package ca.uwaterloo.watform.dashast;

import java.util.List;
import java.util.StringJoiner;

import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.util.Pos;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;

public class DashVarDecls extends Dash {
 
	private List<String> names;
	private AlloyExpr typ;
	private DashStrings.IntEnvKind kind;

	public DashVarDecls (Pos pos, List<String> n, AlloyExpr e, DashStrings.IntEnvKind k) {
		assert(n != null && e != null);
		this.pos = pos;
		this.names = n;
		this.typ = e;
		this.kind = k;
	}

	public String toString(Integer i) {
		String s = DashStrings.indent(i);
		if (kind == DashStrings.IntEnvKind.ENV) {
			s += DashStrings.envName + " ";
		}
		StringJoiner sj = new StringJoiner(",\n");
        names.forEach(n -> sj.add(n));
		return s + sj.toString() + ":" + typ.toString() + "\n";
	}
	public List<String> getNames() {
		return names;
	}
	public AlloyExpr getTyp() {
		return typ;
	}
	public DashStrings.IntEnvKind getKind() {
		return kind;
	}
}