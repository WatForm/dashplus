package ca.uwaterloo.watform.dashmodel;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashast.DashFile;
import java.util.ArrayList;
import java.util.List;

public class InitsInvsDM extends BaseDM {

    protected List<AlloyExpr> initsR = new ArrayList<AlloyExpr>();
    protected List<AlloyExpr> invsR = new ArrayList<AlloyExpr>();

    protected InitsInvsDM() {
        super();
    }

    protected InitsInvsDM(DashFile d) {
        super(d);
    }

    public List<AlloyExpr> initsR() {
        return initsR;
    }

    public List<AlloyExpr> invsR() {
        return invsR;
    }
}
