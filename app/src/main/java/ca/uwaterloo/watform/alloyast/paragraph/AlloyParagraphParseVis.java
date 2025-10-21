package ca.uwaterloo.watform.alloyast.paragraph;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParseVis;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara.AlloyModuleArg;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;

public final class AlloyParagraphParseVis extends AlloyBaseVisitor<AlloyParagraph> {
    private final AlloyExprParseVis exprParseVis = new AlloyExprParseVis();
    private final AlloySigRefsParseVis sigRefsParseVis = new AlloySigRefsParseVis();

    @Override
    public AlloyParagraph visitParagraph(AlloyParser.ParagraphContext ctx) {
        return this.visit(ctx.getChild(0));
    }

    // ====================================================================================
    // Module
    // ====================================================================================
    @Override
    public AlloyModulePara visitModulePara(AlloyParser.ModuleParaContext ctx) {
        return new AlloyModulePara(
                new Pos(ctx),
                (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
                ParserUtil.visitAll(
                        ctx.moduleArg(), new AlloyModuleArgParseVis(), AlloyModuleArg.class));
    }

    @Override
    public AlloyParagraph visitImportPara(AlloyParser.ImportParaContext ctx) {
        return new AlloyImportPara(
                new Pos(ctx),
                null != ctx.PRIVATE(),
                (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
                null != ctx.LBRACK(),
                ((null != ctx.sigRefs())
                        ? this.sigRefsParseVis.visit(ctx.sigRefs())
                        : Collections.emptyList()),
                ((null != ctx.name()) ? (AlloyNameExpr) exprParseVis.visit(ctx.name()) : null));
    }

    @Override
    public AlloyParagraph visitSigPara(AlloyParser.SigParaContext ctx) {
        return visitChildren(ctx);
    }

    // ====================================================================================
    // Enum
    // ====================================================================================
    @Override
    public AlloyParagraph visitEnumPara(AlloyParser.EnumParaContext ctx) {
        return new AlloyEnumPara(
                null != ctx.PRIVATE(),
                (AlloyNameExpr) exprParseVis.visit(ctx.name()),
                null != ctx.names()
                        ? ParserUtil.visitAll(ctx.names().name(), exprParseVis, AlloyNameExpr.class)
                        : Collections.emptyList());
    }

    // ====================================================================================
    // Fact
    // ====================================================================================
    @Override
    public AlloyFactPara visitFactPara(AlloyParser.FactParaContext ctx) {
        if (null != ctx.name()) {
            return new AlloyFactPara(
                    new Pos(ctx),
                    (AlloyNameExpr) exprParseVis.visit(ctx.name()),
                    (AlloyBlock) this.exprParseVis.visit(ctx.block()));
        } else if (null != ctx.STRING_LITERAL()) {
            return new AlloyFactPara(
                    new Pos(ctx),
                    new AlloyStrLiteralExpr(new Pos(ctx), ctx.STRING_LITERAL().getText()),
                    (AlloyBlock) this.exprParseVis.visit(ctx.block()));
        } else {
            return new AlloyFactPara(
                    new Pos(ctx), (AlloyBlock) this.exprParseVis.visit(ctx.block()));
        }
    }

    @Override
    public AlloyParagraph visitPredPara(AlloyParser.PredParaContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public AlloyParagraph visitFunPara(AlloyParser.FunParaContext ctx) {
        return visitChildren(ctx);
    }

    // ====================================================================================
    // Assert
    // ====================================================================================
    @Override
    public AlloyParagraph visitAssertPara(AlloyParser.AssertParaContext ctx) {
        if (null != ctx.name()) {
            return new AlloyAssertPara(
                    new Pos(ctx),
                    (AlloyNameExpr) exprParseVis.visit(ctx.name()),
                    (AlloyBlock) this.exprParseVis.visit(ctx.block()));
        } else if (null != ctx.STRING_LITERAL()) {
            return new AlloyAssertPara(
                    new Pos(ctx),
                    new AlloyStrLiteralExpr(new Pos(ctx), ctx.STRING_LITERAL().getText()),
                    (AlloyBlock) this.exprParseVis.visit(ctx.block()));
        } else {
            return new AlloyAssertPara(
                    new Pos(ctx), (AlloyBlock) this.exprParseVis.visit(ctx.block()));
        }
    }

    @Override
    public AlloyParagraph visitMacroPara(AlloyParser.MacroParaContext ctx) {
        return visitChildren(ctx);
    }
}
