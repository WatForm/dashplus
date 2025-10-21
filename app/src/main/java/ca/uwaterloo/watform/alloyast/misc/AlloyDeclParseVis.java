package ca.uwaterloo.watform.alloyast.misc;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParseVis;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.List;

public final class AlloyDeclParseVis extends AlloyBaseVisitor<AlloyDecl> {
    @Override
    public AlloyDecl visitDecl(AlloyParser.DeclContext ctx) {
        AlloyExprParseVis exprParseVis = new AlloyExprParseVis();
        final Boolean disj1 = null != ctx.DISJ(0) ? true : false;
        List<AlloyNameExpr> names =
                ParserUtil.visitAll(ctx.names().name(), exprParseVis, AlloyNameExpr.class);
        final Boolean disj2 = null != ctx.DISJ(1) ? true : false;
        AlloyDecl.Quant quant;
        if (null != ctx.LONE()) {
            quant = AlloyDecl.Quant.LONE;
        } else if (null != ctx.ONE()) {
            quant = AlloyDecl.Quant.ONE;
        } else if (null != ctx.SOME()) {
            quant = AlloyDecl.Quant.SOME;
        } else if (null != ctx.SET()) {
            quant = AlloyDecl.Quant.SET;
        } else {
            quant = null;
        }
        return new AlloyDecl(
                new Pos(ctx), disj1, names, disj2, quant, exprParseVis.visit(ctx.expr1()));
    }
}
