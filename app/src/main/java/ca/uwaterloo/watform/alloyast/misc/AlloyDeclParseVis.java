package ca.uwaterloo.watform.alloyast.misc;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParseVis;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.List;
import org.antlr.v4.runtime.tree.TerminalNode;

public final class AlloyDeclParseVis extends AlloyBaseVisitor<AlloyDecl> {
    @Override
    public AlloyDecl visitDecl(AlloyParser.DeclContext ctx) {
        return this.visit(ctx.getChild(0));
    }

    @Override
    public AlloyDecl visitDeclMul(AlloyParser.DeclMulContext ctx) {
        AlloyExprParseVis exprParseVis = new AlloyExprParseVis();

        final boolean isVar = null != ctx.VAR() ? true : false;

        final boolean isPrivate = null != ctx.PRIVATE() ? true : false;

        List<AlloyNameExpr> names =
                ParserUtil.visitAll(ctx.names().name(), exprParseVis, AlloyNameExpr.class);

        boolean isDisj1 = false;
        boolean isDisj2 = false;
        int colonPosition = ctx.COLON().getSymbol().getStartIndex();
        for (TerminalNode disj : ctx.DISJ()) {
            if (disj.getSymbol().getStartIndex() < colonPosition) {
                isDisj1 = true;
            } else {
                isDisj2 = true;
            }
        }

        AlloyDecl.Quant quant = null;
        if (null != ctx.LONE()) {
            quant = AlloyDecl.Quant.LONE;
        } else if (null != ctx.ONE()) {
            quant = AlloyDecl.Quant.ONE;
        } else if (null != ctx.SOME()) {
            quant = AlloyDecl.Quant.SOME;
        } else if (null != ctx.SET()) {
            quant = AlloyDecl.Quant.SET;
        }
        return new AlloyDecl(
                new Pos(ctx),
                isVar,
                isPrivate,
                isDisj1,
                names,
                isDisj2,
                quant,
                exprParseVis.visit(ctx.expr1()));
    }

    @Override
    public AlloyDecl visitDeclExact(AlloyParser.DeclExactContext ctx) {
        AlloyExprParseVis exprParseVis = new AlloyExprParseVis();

        final boolean isPrivate = null != ctx.PRIVATE() ? true : false;

        List<AlloyNameExpr> names =
                ParserUtil.visitAll(ctx.names().name(), exprParseVis, AlloyNameExpr.class);

        return new AlloyDecl(
                new Pos(ctx),
                false,
                isPrivate,
                false,
                names,
                false,
                AlloyDecl.Quant.EXACTLY,
                exprParseVis.visit(ctx.expr1()));
    }
}
