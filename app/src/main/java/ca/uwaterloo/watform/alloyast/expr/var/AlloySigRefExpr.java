package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.utils.Pos;

public sealed interface AlloySigRefExpr
        permits AlloyQnameExpr,
                AlloyUnivExpr,
                AlloyStringExpr,
                AlloyStepsExpr,
                AlloySigIntExpr,
                AlloySeqIntExpr,
                AlloyNoneExpr {

    String getName();

    Pos getPos();
}
