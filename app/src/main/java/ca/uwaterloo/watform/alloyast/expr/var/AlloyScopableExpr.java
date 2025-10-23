package ca.uwaterloo.watform.alloyast.expr.var;

public sealed interface AlloyScopableExpr
        permits AlloyQnameExpr,
                AlloySigIntExpr,
                AlloyIntExpr,
                AlloySeqExpr,
                AlloyStringExpr,
                AlloyStepsExpr {}
