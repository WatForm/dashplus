package ca.uwaterloo.watform.dashast;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.utils.*;
import java.util.List;
import java.util.StringJoiner;

public class DashTrans extends ASTNode {
    public String name;
    public List<Object> items;

    public DashTrans(Pos pos, String n, List<Object> i) {
        super(pos);
        assert (n != null);
        assert (i != null);
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
            items.forEach(k -> j.add(((ASTNode) k).toString(indent + 1)));
            s += j.toString();
            s += ind + "}\n";
        }
        sb.append(s);
    }

    public List<DashFrom> froms() {
        return extractItemsOfClass(items, DashFrom.class);
    }

    public List<DashOn> ons() {
        return extractItemsOfClass(items, DashOn.class);
    }

    public List<DashWhen> whens() {
        return extractItemsOfClass(items, DashWhen.class);
    }

    public List<DashGoto> gotos() {
        return extractItemsOfClass(items, DashGoto.class);
    }

    public List<DashSend> sends() {
        return extractItemsOfClass(items, DashSend.class);
    }

    public List<DashDo> dos() {
        return extractItemsOfClass(items, DashDo.class);
    }
}
