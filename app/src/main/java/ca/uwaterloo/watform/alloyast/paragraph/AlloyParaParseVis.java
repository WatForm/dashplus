package ca.uwaterloo.watform.alloyast.paragraph;

import static ca.uwaterloo.watform.utils.ParserUtil.*;

import antlr.generated.*;
import antlr.generated.DashBaseVisitor;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParseVis;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdDeclParseVis;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloyast.paragraph.module.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara.AlloyModuleArg;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigQualParseVis;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigRelParseVis;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;

public class AlloyParaParseVis extends DashBaseVisitor<AlloyPara> {
    protected final AlloyExprParseVis exprParseVis = new AlloyExprParseVis();
    protected final AlloySigRefsParseVis sigRefsParseVis = new AlloySigRefsParseVis();

    @Override
    public AlloyPara visitParagraph(DashParser.ParagraphContext ctx) {
        return this.visit(ctx.getChild(0));
    }

    // ====================================================================================
    // Module
    // ====================================================================================
    @Override
    public AlloyModulePara visitModulePara(DashParser.ModuleParaContext ctx) {
        return new AlloyModulePara(
                new Pos(ctx),
                (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
                visitAll(ctx.moduleArg(), new AlloyModuleArgParseVis(), AlloyModuleArg.class));
    }

    // ====================================================================================
    // Import
    // ====================================================================================
    @Override
    public AlloyImportPara visitImportPara(DashParser.ImportParaContext ctx) {
        return new AlloyImportPara(
                new Pos(ctx),
                null != ctx.PRIVATE(),
                (AlloyQnameExpr) exprParseVis.visit(ctx.qname(0)),
                ((null != ctx.sigRefs())
                        ? this.sigRefsParseVis.visit(ctx.sigRefs())
                        : Collections.emptyList()),
                ((null != ctx.qname(1))
                        ? (AlloyQnameExpr) exprParseVis.visit(ctx.qname(1))
                        : null));
    }

    // ====================================================================================
    // Sig
    // ====================================================================================
    @Override
    public AlloySigPara visitSigPara(DashParser.SigParaContext ctx) {
        return new AlloySigPara(
                new Pos(ctx),
                visitAll(ctx.sigQualifier(), new AlloySigQualParseVis(), AlloySigPara.Qual.class),
                null != ctx.qnames()
                        ? visitAll(ctx.qnames().qname(), exprParseVis, AlloyQnameExpr.class)
                        : Collections.emptyList(),
                null != ctx.sigRel() ? new AlloySigRelParseVis().visit(ctx.sigRel()) : null,
                visitAll(ctx.decl(), exprParseVis, AlloyDecl.class),
                null != ctx.block() ? (AlloyBlock) exprParseVis.visit(ctx.block()) : null);
    }

    // ====================================================================================
    // Enum
    // ====================================================================================
    @Override
    public AlloyEnumPara visitEnumPara(DashParser.EnumParaContext ctx) {
        return new AlloyEnumPara(
                null != ctx.PRIVATE(),
                (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
                null != ctx.qnames()
                        ? visitAll(ctx.qnames().qname(), exprParseVis, AlloyQnameExpr.class)
                        : Collections.emptyList());
    }

    // ====================================================================================
    // Fact
    // ====================================================================================
    @Override
    public AlloyFactPara visitFactPara(DashParser.FactParaContext ctx) {
        if (null != ctx.qname()) {
            return new AlloyFactPara(
                    new Pos(ctx),
                    (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
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
    public AlloyPredPara visitPredPara(DashParser.PredParaContext ctx) {
        List<AlloyDecl> decls = Collections.emptyList();
        if (null != ctx.arguments()) {
            if (null != ctx.arguments().decls()) {
                decls = visitAll(ctx.arguments().decls().decl(), exprParseVis, AlloyDecl.class);
            }
        }

        return new AlloyPredPara(
                new Pos(ctx),
                null != ctx.PRIVATE(),
                (null != ctx.sigRef()) ? (AlloySigRefExpr) exprParseVis.visit(ctx.sigRef()) : null,
                (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
                decls,
                (AlloyBlock) exprParseVis.visit(ctx.block()));
    }

    // ====================================================================================
    // Fun
    // ====================================================================================
    @Override
    public AlloyFunPara visitFunPara(DashParser.FunParaContext ctx) {
        List<AlloyDecl> decls = Collections.emptyList();
        if (null != ctx.arguments()) {
            if (null != ctx.arguments().decls()) {
                decls = visitAll(ctx.arguments().decls().decl(), exprParseVis, AlloyDecl.class);
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
                throw AlloyASTImplError.invalidCase(new Pos(ctx));
            }
        }

        return new AlloyFunPara(
                new Pos(ctx),
                null != ctx.PRIVATE(),
                (null != ctx.sigRef()) ? (AlloySigRefExpr) exprParseVis.visit(ctx.sigRef()) : null,
                (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
                decls,
                mul,
                exprParseVis.visit(ctx.expr1()),
                (AlloyBlock) exprParseVis.visit(ctx.block()));
    }

    // ====================================================================================
    // Assert
    // ====================================================================================
    @Override
    public AlloyAssertPara visitAssertPara(DashParser.AssertParaContext ctx) {
        if (null != ctx.qname()) {
            return new AlloyAssertPara(
                    new Pos(ctx),
                    (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
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
    public AlloyMacroPara visitMacroPara(DashParser.MacroParaContext ctx) {
        if (null != ctx.block()) {
            return new AlloyMacroPara(
                    new Pos(ctx),
                    null != ctx.PRIVATE(),
                    (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
                    null != ctx.qnames()
                            ? visitAll(ctx.qnames().qname(), exprParseVis, AlloyQnameExpr.class)
                            : Collections.emptyList(),
                    (AlloyBlock) exprParseVis.visit(ctx.block()));
        } else if (null != ctx.expr1()) {
            return new AlloyMacroPara(
                    new Pos(ctx),
                    null != ctx.PRIVATE(),
                    (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
                    null != ctx.qnames()
                            ? visitAll(ctx.qnames().qname(), exprParseVis, AlloyQnameExpr.class)
                            : Collections.emptyList(),
                    exprParseVis.visit(ctx.expr1()));
        } else {
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }
    }

    // ====================================================================================
    // Command
    // ====================================================================================
    @Override
    public AlloyCmdPara visitCommandPara(DashParser.CommandParaContext ctx) {
        return new AlloyCmdPara(
                new Pos(ctx),
                visitAll(
                        ctx.commandDecl(),
                        new AlloyCmdDeclParseVis(),
                        AlloyCmdPara.CommandDecl.class));
    }
}
