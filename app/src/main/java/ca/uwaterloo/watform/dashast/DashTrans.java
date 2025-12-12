package ca.uwaterloo.watform.dashast;

import static ca.uwaterloo.watform.utils.GeneralUtil.passIfNull;

import ca.uwaterloo.watform.dashast.dashNamedExpr.*;
import ca.uwaterloo.watform.utils.*;

public final class DashTrans extends ASTNode implements DashStateItem {
    public final String name;
    public final DashFrom fromP;
    public final DashGoto gotoP;
    public final DashOn onP;
    public final DashSend sendP;
    public final DashWhen whenP;
    public final DashDo doP;

    public DashTrans(
            Pos pos,
            String n,
            DashFrom fromP,
            DashGoto gotoP,
            DashOn onP,
            DashSend sendP,
            DashWhen whenP,
            DashDo doP) {
        super(pos);
        assert (n != null);
        assert (pos != null);
        this.name = n;
        this.fromP = fromP;
        this.gotoP = gotoP;
        this.onP = onP;
        this.sendP = sendP;
        this.whenP = whenP;
        this.doP = doP;
    }

    private boolean emptyTrans() {
        return fromP == null
                && gotoP == null
                && onP == null
                && sendP == null
                && whenP == null
                && doP == null;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        String s = new String("");
        String ind = DashStrings.indent(indent);
        if (emptyTrans()) {
            sb.append(ind + DashStrings.transName + " " + name + " { }\n");
        } else {
            sb.append(ind + DashStrings.transName + " " + name + " {\n");
            passIfNull(sb, indent + 1, this.fromP);
            passIfNull(sb, indent + 1, this.gotoP);
            passIfNull(sb, indent + 1, this.onP);
            passIfNull(sb, indent + 1, this.sendP);
            passIfNull(sb, indent + 1, this.whenP);
            passIfNull(sb, indent + 1, this.doP);
            sb.append(ind + "}\n");
        }
        sb.append(s);
    }
}
