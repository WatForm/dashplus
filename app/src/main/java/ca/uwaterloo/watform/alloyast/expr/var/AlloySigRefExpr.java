package ca.uwaterloo.watform.alloyast.expr.var;

public sealed interface AlloySigRefExpr
        permits AlloyQnameExpr,
                AlloyUnivExpr,
                AlloyStringExpr,
                AlloyStepsExpr,
                AlloySigIntExpr,
                AlloySeqIntExpr,
                AlloyNoneExpr {}
