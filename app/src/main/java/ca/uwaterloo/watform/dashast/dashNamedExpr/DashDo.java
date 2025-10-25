package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.utils.*;

public class DashDo extends DashNamedExpr {

    @Override
    public void toString(StringBuilder sb, int indent) {
        super.toString(DashStrings.doName, sb, indent);
    }
}
