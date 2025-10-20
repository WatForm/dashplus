package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.utils.*;
import java.util.List;
import java.util.StringJoiner;

public class DashBufferDecls extends ASTNode {

    private List<String> names;
    private String element;
    private DashStrings.IntEnvKind kind;
    private Integer startIndex;
    private Integer endIndex;

    public DashBufferDecls(
            Pos pos,
            List<String> n,
            String element,
            DashStrings.IntEnvKind k,
            int startIndex,
            int endIndex) {
        super(pos);
        assert (n != null && element != null);
        this.names = n;
        this.element = element;
        this.kind = k;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
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

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }
}
