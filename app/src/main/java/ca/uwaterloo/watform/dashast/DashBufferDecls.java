package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public final class DashBufferDecls extends ASTNode implements DashStateItem {

    public final List<String> names;
    public final String element;
    public final DashStrings.IntEnvKind kind;

    public DashBufferDecls(Pos pos, List<String> n, String element, DashStrings.IntEnvKind k) {
        super(pos);
        assert (n != null && element != null);
        this.names = Collections.unmodifiableList(n);
        this.element = element;
        this.kind = k;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        // indices are hidden
        String s = new String("");
        if (kind == DashStrings.IntEnvKind.ENV) {
            s += DashStrings.envName + " ";
        }
        StringJoiner sj = new StringJoiner(",\n");
        names.forEach(n -> sj.add(n));
        s += sj.toString() + ":" + DashStrings.bufName + "[" + element + "]\n";
        sb.append(DashStrings.indent(indent) + s);
    }

    public List<String> getNames() {
        return names;
    }

    public String getElement() {
        return element;
    }

    public DashStrings.IntEnvKind getKind() {
        return kind;
    }
}
