package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public final class DashEventDecls extends ASTNode implements DashStateItem {
    public final List<String> names;
    public final DashStrings.IntEnvKind kind;

    public DashEventDecls(Pos pos, List<String> n, DashStrings.IntEnvKind kind) {
        super(pos);
        assert (n != null);
        this.names = Collections.unmodifiableList(n);
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
        s += DashStrings.eventName + " " + sj.toString() + " {}\n";
        sb.append(DashStrings.indent(indent) + s);
    }

    public List<String> getNames() {
        return names;
    }

    public DashStrings.IntEnvKind getKind() {
        return kind;
    }
}
