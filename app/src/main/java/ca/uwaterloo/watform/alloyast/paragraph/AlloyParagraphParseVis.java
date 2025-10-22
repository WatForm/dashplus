package ca.uwaterloo.watform.alloyast.paragraph;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParseVis;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.misc.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara.AlloyModuleArg;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;

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
    public AlloyImportPara visitImportPara(AlloyParser.ImportParaContext ctx) {
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
    public AlloyEnumPara visitEnumPara(AlloyParser.EnumParaContext ctx) {
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

    // ====================================================================================
    // Pred
    // ====================================================================================
    @Override
    public AlloyParagraph visitPredPara(AlloyParser.PredParaContext ctx) {
        boolean hasBrack = false;
        boolean hasParen = false;
        List<AlloyDecl> decls = Collections.emptyList();
        if (null != ctx.arguments()) {
            hasBrack = null != ctx.arguments().LBRACK();
            hasParen = null != ctx.arguments().LPAREN();
            if (null != ctx.arguments().decls()) {
                decls =
                        ParserUtil.visitAll(
                                ctx.arguments().decls().decl(),
                                new AlloyDeclParseVis(),
                                AlloyDecl.class);
            }
        }

        return new AlloyPredPara(
                new Pos(ctx),
                null != ctx.PRIVATE(),
                (null != ctx.sigRef()) ? (AlloySigRefExpr) exprParseVis.visit(ctx.sigRef()) : null,
                (AlloyNameExpr) exprParseVis.visit(ctx.name()),
                hasBrack,
                hasParen,
                decls,
                (AlloyBlock) exprParseVis.visit(ctx.block()));
    }

    // ====================================================================================
    // Fun
    // ====================================================================================
    @Override
    public AlloyFunPara visitFunPara(AlloyParser.FunParaContext ctx) {
        boolean hasBrack = false;
        boolean hasParen = false;
        List<AlloyDecl> decls = Collections.emptyList();
        if (null != ctx.arguments()) {
            hasBrack = null != ctx.arguments().LBRACK();
            hasParen = null != ctx.arguments().LPAREN();
            if (null != ctx.arguments().decls()) {
                decls =
                        ParserUtil.visitAll(
                                ctx.arguments().decls().decl(),
                                new AlloyDeclParseVis(),
                                AlloyDecl.class);
            }
        }

        AlloyFunPara.Mul mul = AlloyFunPara.Mul.DEFAULTSET;
        if (null != ctx.multiplicity()) {
            if (null != ctx.multiplicity().LONE()) {
                mul = AlloyFunPara.Mul.LONE;
            } else if (null != ctx.multiplicity().SOME()) {
                mul = AlloyFunPara.Mul.SOME;
            } else if (null != ctx.multiplicity().ONE()) {
                mul = AlloyFunPara.Mul.ONE;
            } else if (null != ctx.multiplicity().SET()) {
                mul = AlloyFunPara.Mul.SET;
            } else {
                throw new AlloyUnexpTokenEx(ctx);
            }
        }

        return new AlloyFunPara(
                new Pos(ctx),
                null != ctx.PRIVATE(),
                (null != ctx.sigRef()) ? (AlloySigRefExpr) exprParseVis.visit(ctx.sigRef()) : null,
                (AlloyNameExpr) exprParseVis.visit(ctx.name()),
                hasBrack,
                hasParen,
                decls,
                mul,
                exprParseVis.visit(ctx.expr1()),
                (AlloyBlock) exprParseVis.visit(ctx.block()));
    }

    // ====================================================================================
    // Assert
    // ====================================================================================
    @Override
    public AlloyAssertPara visitAssertPara(AlloyParser.AssertParaContext ctx) {
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

    // ====================================================================================
    // Macro
    // ====================================================================================
    @Override
    public AlloyMacroPara visitMacroPara(AlloyParser.MacroParaContext ctx) {
        if (null != ctx.block()) {
            return new AlloyMacroPara(
                    new Pos(ctx),
                    null != ctx.PRIVATE(),
                    (AlloyNameExpr) exprParseVis.visit(ctx.name()),
                    null != ctx.names()
                            ? ParserUtil.visitAll(
                                    ctx.names().name(), exprParseVis, AlloyNameExpr.class)
                            : Collections.emptyList(),
                    null != ctx.LBRACK(),
                    null != ctx.LPAREN(),
                    (AlloyBlock) exprParseVis.visit(ctx.block()));
        } else if (null != ctx.expr1()) {
            return new AlloyMacroPara(
                    new Pos(ctx),
                    null != ctx.PRIVATE(),
                    (AlloyNameExpr) exprParseVis.visit(ctx.name()),
                    null != ctx.names()
                            ? ParserUtil.visitAll(
                                    ctx.names().name(), exprParseVis, AlloyNameExpr.class)
                            : Collections.emptyList(),
                    null != ctx.LBRACK(),
                    null != ctx.LPAREN(),
                    exprParseVis.visit(ctx.expr1()));
        } else {
            throw new AlloyUnexpTokenEx(ctx);
        }
    }
}
