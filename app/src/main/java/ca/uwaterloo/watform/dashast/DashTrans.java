package ca.uwaterloo.watform.dashast;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.passIfNull;

import ca.uwaterloo.watform.dashast.dashNamedExpr.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

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
    public void pp(PrintContext pCtx) {
        pCtx.append(transName + SPACE + name + SPACE + LBRACE);
        if (!emptyTrans()) {
            pCtx.brkNoSpace();
            List<DashNamedExpr> li = new ArrayList<>();
            if (null != fromP) li.add(fromP);
            if (null != gotoP) li.add(gotoP);
            if (null != onP) li.add(onP);
            if (null != sendP) li.add(sendP);
            if (null != whenP) li.add(whenP);
            if (null != doP) li.add(doP);
            for (DashNamedExpr dne : li) {
                dne.ppNewBlock(pCtx);
                if (!(dne == li.getLast())) {
                    pCtx.nl();
                }
            }
            pCtx.brkNoSpaceNoIndent();
        }
        pCtx.append(RBRACE);
    }
}
