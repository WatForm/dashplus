package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.utils.*;

public class DashFrom extends DashNamedExpr {

    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.fromName, sb, indent);
    }
}
