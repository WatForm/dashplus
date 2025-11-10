package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.List;
import java.util.StringJoiner;

public final class DashVarDecls extends ASTNode implements DashStateItem {

    public final List<String> names;
    public final AlloyExpr typ;
    public final DashStrings.IntEnvKind kind;

    public DashVarDecls(Pos pos, List<String> n, AlloyExpr e, DashStrings.IntEnvKind k) {
        super(pos);
        assert (n != null && e != null);
        this.names = n;
        this.typ = e;
        this.kind = k;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        String s = DashStrings.indent(indent);
        if (kind == DashStrings.IntEnvKind.ENV) {
            s += DashStrings.envName + " ";
        }
        StringJoiner sj = new StringJoiner(",\n");
        names.forEach(n -> sj.add(n));
        sb.append(s + sj.toString() + ":" + typ.toString() + "\n");
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
