package ca.uwaterloo.watform.alloyast.expr;

import antlr.generated.*;
import antlr.generated.DashParser.*;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyLetExpr.AlloyLetAsn;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.tree.TerminalNode;

public class AlloyExprParseVis extends DashBaseVisitor<AlloyExpr> {

    // ====================================================================================
    // Bind
    // ====================================================================================
    @Override
    public AlloyExpr visitBindExpr(DashParser.BindExprContext ctx) {
        return this.visit(ctx.bind());
    }

    @Override
    public AlloyLetExpr visitLet(DashParser.LetContext ctx) {
        return new AlloyLetExpr(
                new Pos(ctx),
                ParserUtil.visitAll(ctx.assignment(), new AlloyLetAsnParseVis(), AlloyLetAsn.class),
                this.visit(ctx.body()));
    }

    @Override
    public AlloyQuantificationExpr visitQuantificationExpr(
            DashParser.QuantificationExprContext ctx) {
        List<AlloyDecl> decls =
                null != ctx.decls()
                        ? ParserUtil.visitAll(ctx.decls().decl(), this, AlloyDecl.class)
                        : Collections.emptyList();
        if (null != ctx.ALL()) {
            return new AlloyQuantificationExpr(
                    new Pos(ctx), AlloyQuantificationExpr.Quant.ALL, decls, this.visit(ctx.body()));
        } else if (null != ctx.NO()) {
            return new AlloyQuantificationExpr(
                    new Pos(ctx), AlloyQuantificationExpr.Quant.NO, decls, this.visit(ctx.body()));
        } else if (null != ctx.SOME()) {
            return new AlloyQuantificationExpr(
                    new Pos(ctx),
                    AlloyQuantificationExpr.Quant.SOME,
                    decls,
                    this.visit(ctx.body()));
        } else if (null != ctx.LONE()) {
            return new AlloyQuantificationExpr(
                    new Pos(ctx),
                    AlloyQuantificationExpr.Quant.LONE,
                    decls,
                    this.visit(ctx.body()));
        } else if (null != ctx.ONE()) {
            return new AlloyQuantificationExpr(
                    new Pos(ctx), AlloyQuantificationExpr.Quant.ONE, decls, this.visit(ctx.body()));
        } else if (null != ctx.SUM()) {
            return new AlloyQuantificationExpr(
                    new Pos(ctx), AlloyQuantificationExpr.Quant.SUM, decls, this.visit(ctx.body()));
        } else {
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }
    }

    // ====================================================================================
    // expr1
    // ====================================================================================

    // ============================
    // Iff
    // ============================
    @Override
    public AlloyIffExpr visitIffExpr(DashParser.IffExprContext ctx) {
        return new AlloyIffExpr(new Pos(ctx), this.visit(ctx.expr1(0)), this.visit(ctx.expr1(1)));
    }

    @Override
    public AlloyIffExpr visitIffBindExpr(DashParser.IffBindExprContext ctx) {
        return new AlloyIffExpr(new Pos(ctx), this.visit(ctx.expr1()), this.visit(ctx.bind()));
    }

    // ============================
    // Or
    // ============================

    @Override
    public AlloyOrExpr visitOrExpr(DashParser.OrExprContext ctx) {
        return new AlloyOrExpr(new Pos(ctx), this.visit(ctx.expr1(0)), this.visit(ctx.expr1(1)));
    }

    @Override
    public AlloyOrExpr visitOrBindExpr(DashParser.OrBindExprContext ctx) {
        return new AlloyOrExpr(new Pos(ctx), this.visit(ctx.expr1()), this.visit(ctx.bind()));
    }

    // ============================
    // StateSeq
    // ============================

    @Override
    public AlloyStateSeqExpr visitStateSeqExpr(DashParser.StateSeqExprContext ctx) {
        return new AlloyStateSeqExpr(
                new Pos(ctx), this.visit(ctx.expr1(0)), this.visit(ctx.expr1(1)));
    }

    @Override
    public AlloyStateSeqExpr visitStateSeqBindExpr(DashParser.StateSeqBindExprContext ctx) {
        return new AlloyStateSeqExpr(new Pos(ctx), this.visit(ctx.expr1()), this.visit(ctx.bind()));
    }

    // ====================================================================================
    // impliesExpr
    // ====================================================================================
    @Override
    public AlloyExpr visitImpExprOpenOrClose(DashParser.ImpExprOpenOrCloseContext ctx) {
        return this.visit(ctx.impliesExpr());
    }

    @Override
    public AlloyExpr visitImpExprCloseFromImplies(DashParser.ImpExprCloseFromImpliesContext ctx) {
        return this.visit(ctx.impliesExprClose());
    }

    @Override
    public AlloyExpr visitImpExprOpenFromImplies(DashParser.ImpExprOpenFromImpliesContext ctx) {
        return this.visit(ctx.impliesExprOpen());
    }

    @Override
    public AlloyIteExpr visitIteCloseExpr(DashParser.IteCloseExprContext ctx) {
        return new AlloyIteExpr(
                new Pos(ctx),
                this.visit(ctx.expr2()),
                this.visit(ctx.impliesExprClose(0)),
                this.visit(ctx.impliesExprClose(1)));
    }

    @Override
    public AlloyIteExpr visitIteBindCloseExpr(DashParser.IteBindCloseExprContext ctx) {
        return new AlloyIteExpr(
                new Pos(ctx),
                this.visit(ctx.expr2()),
                this.visit(ctx.impliesExprClose()),
                this.visit(ctx.bind()));
    }

    @Override
    public AlloyExpr visitExpr2FromImpClose(DashParser.Expr2FromImpCloseContext ctx) {
        return this.visit(ctx.expr2());
    }

    @Override
    public AlloyIteExpr visitIteOpenExpr(DashParser.IteOpenExprContext ctx) {
        return new AlloyIteExpr(
                new Pos(ctx),
                this.visit(ctx.expr2()),
                this.visit(ctx.impliesExprClose()),
                this.visit(ctx.impliesExprOpen()));
    }

    @Override
    public AlloyImpliesExpr visitImpExpr(DashParser.ImpExprContext ctx) {
        return new AlloyImpliesExpr(
                new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.impliesExpr()));
    }

    @Override
    public AlloyImpliesExpr visitImpBindExpr(DashParser.ImpBindExprContext ctx) {
        return new AlloyImpliesExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
    }

    // ====================================================================================
    // baseExpr
    // ====================================================================================

    @Override
    public AlloyNumExpr visitNumber(DashParser.NumberContext ctx) {
        return new AlloyNumExpr(
                new Pos(ctx),
                null == ctx.MINUS()
                        ? ctx.NUMBER().getText()
                        : AlloyStrings.MINUS + ctx.NUMBER().getText());
    }

    @Override
    public AlloyStrLiteralExpr visitStrLiteralExpr(DashParser.StrLiteralExprContext ctx) {
        return new AlloyStrLiteralExpr(new Pos(ctx), ctx.STRING_LITERAL().getText());
    }

    @Override
    public AlloyIdenExpr visitIdenExpr(DashParser.IdenExprContext ctx) {
        return new AlloyIdenExpr(new Pos(ctx));
    }

    @Override
    public AlloyThisExpr visitThisExpr(DashParser.ThisExprContext ctx) {
        return new AlloyThisExpr(new Pos(ctx));
    }

    @Override
    public AlloyFunMinExpr visitFunMinExpr(DashParser.FunMinExprContext ctx) {
        return new AlloyFunMinExpr(new Pos(ctx));
    }

    @Override
    public AlloyFunMaxExpr visitFunMaxExpr(DashParser.FunMaxExprContext ctx) {
        return new AlloyFunMaxExpr(new Pos(ctx));
    }

    @Override
    public AlloyFunNextExpr visitFunNextExpr(DashParser.FunNextExprContext ctx) {
        return new AlloyFunNextExpr(new Pos(ctx));
    }

    @Override
    public AlloyParenExpr visitParenExpr(DashParser.ParenExprContext ctx) {
        return new AlloyParenExpr(new Pos(ctx), this.visit(ctx.expr1()));
    }

    @Override
    public AlloyExpr visitSigRefExpr(DashParser.SigRefExprContext ctx) {
        return this.visit(ctx.sigRef());
    }

    @Override
    public AlloyAtNameExpr visitAtNameExpr(DashParser.AtNameExprContext ctx) {
        return new AlloyAtNameExpr(new Pos(ctx), (AlloyQnameExpr) this.visit(ctx.qname()));
    }

    @Override
    public AlloyBlock visitBlockExpr(DashParser.BlockExprContext ctx) {
        return (AlloyBlock) this.visit(ctx.block());
    }

    @Override
    public AlloyComprehensionExpr visitComprehensionExpr(DashParser.ComprehensionExprContext ctx) {
        return new AlloyComprehensionExpr(
                new Pos(ctx),
                ParserUtil.visitAll(ctx.declMul(), this, AlloyDecl.class),
                (null != ctx.body()) ? this.visit(ctx.body()) : null);
    }

    // ============================
    // Block
    // ============================
    @Override
    public AlloyBlock visitBlock(DashParser.BlockContext ctx) {
        return new AlloyBlock(
                new Pos(ctx), ParserUtil.visitAll(ctx.expr1(), this, AlloyExpr.class));
    }

    // ============================
    // SigRef
    // ============================
    @Override
    public AlloyVarExpr visitSigRef(DashParser.SigRefContext ctx) {
        if (null != ctx.qname()) {
            return (AlloyVarExpr) this.visit(ctx.qname());
        }
        return (AlloyVarExpr) this.visit(ctx.getChild(0));
    }

    @Override
    public AlloyNameExpr visitName(DashParser.NameContext ctx) {
        return new AlloyNameExpr(new Pos(ctx), ctx.ID().getText());
    }

    @Override
    public AlloyQnameExpr visitSimpleQname(DashParser.SimpleQnameContext ctx) {
        return new AlloyQnameExpr(new Pos(ctx), (AlloyNameExpr) this.visit(ctx.name()));
    }

    @Override
    public AlloyQnameExpr visitQualifiedQname(DashParser.QualifiedQnameContext ctx) {
        List<AlloyVarExpr> nameExprList = new ArrayList<>();
        if (null != ctx.SEQ()) {
            nameExprList.add(new AlloySeqExpr(new Pos(ctx.SEQ())));
        } else if (null != ctx.THIS()) {
            nameExprList.add(new AlloyThisExpr(new Pos(ctx.THIS())));
        }
        for (NameContext nameCtx : ctx.name()) {
            nameExprList.add((AlloyNameExpr) this.visit(nameCtx));
        }
        return new AlloyQnameExpr(new Pos(ctx), nameExprList);
    }

    // ====================================================================================
    // transExpr
    // ====================================================================================

    @Override
    public AlloyUnaryExpr visitTransExpr(DashParser.TransExprContext ctx) {
        if (null != ctx.TRANSPOSE()) {
            return new AlloyTransExpr(new Pos(ctx), this.visit(ctx.getChild(1)));
        } else if (null != ctx.TRANS_CLOS()) {
            return new AlloyTransClosExpr(new Pos(ctx), this.visit(ctx.getChild(1)));
        } else if (null != ctx.REFL_TRANS_CLOS()) {
            return new AlloyReflTransClosExpr(new Pos(ctx), this.visit(ctx.getChild(1)));
        } else {
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }
    }

    // ============================
    // PrimeExpr
    // ============================
    @Override
    public AlloyPrimeExpr visitPrimeExpr(DashParser.PrimeExprContext ctx) {
        return new AlloyPrimeExpr(new Pos(ctx), this.visit(ctx.getChild(0)));
    }

    // ====================================================================================
    // expr2
    // ====================================================================================

    // ============================
    // Dot
    // ============================
    @Override
    public AlloyDotExpr visitDotExpr(DashParser.DotExprContext ctx) {
        return new AlloyDotExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.getChild(2)));
    }

    // ============================
    // BracketExpr
    // ============================

    @Override
    public AlloyBracketExpr visitBracketExpr(DashParser.BracketExprContext ctx) {
        return new AlloyBracketExpr(
                new Pos(ctx),
                this.visit(ctx.expr2()),
                ParserUtil.visitAll(ctx.expr1(), this, AlloyExpr.class));
    }

    @Override
    public AlloyBracketExpr visitBracketBuiltinExpr(DashParser.BracketBuiltinExprContext ctx) {
        return new AlloyBracketExpr(
                new Pos(ctx),
                this.visit(ctx.getChild(0)),
                ParserUtil.visitAll(ctx.expr1(), this, AlloyExpr.class));
    }

    // ============================
    // RngRestr
    // ============================

    @Override
    public AlloyRngRestrExpr visitRangExpr(DashParser.RangExprContext ctx) {
        return new AlloyRngRestrExpr(
                new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
    }

    @Override
    public AlloyRngRestrExpr visitRangBindExpr(DashParser.RangBindExprContext ctx) {
        return new AlloyRngRestrExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
    }

    // ============================
    // DomRestr
    // ============================
    @Override
    public AlloyDomRestrExpr visitDomExpr(DashParser.DomExprContext ctx) {
        return new AlloyDomRestrExpr(
                new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
    }

    @Override
    public AlloyDomRestrExpr visitDomBindExpr(DashParser.DomBindExprContext ctx) {
        return new AlloyDomRestrExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
    }

    // ============================
    // ArrowExpr
    // ============================
    private AlloyArrowExpr.Mul parseMultiplicity(DashParser.MultiplicityContext multCtx) {
        if (multCtx == null) {
            return AlloyArrowExpr.Mul.DEFAULTSET;
        }
        if (multCtx.LONE() != null) return AlloyArrowExpr.Mul.LONE;
        if (multCtx.SOME() != null) return AlloyArrowExpr.Mul.SOME;
        if (multCtx.ONE() != null) return AlloyArrowExpr.Mul.ONE;
        if (multCtx.SET() != null) return AlloyArrowExpr.Mul.SET;
        return AlloyArrowExpr.Mul.DEFAULTSET;
    }

    @Override
    public AlloyArrowExpr visitArrowExpr(DashParser.ArrowExprContext ctx) {
        AlloyArrowExpr.Mul mul1 = AlloyArrowExpr.Mul.DEFAULTSET;
        AlloyArrowExpr.Mul mul2 = AlloyArrowExpr.Mul.DEFAULTSET;

        int arrowPosition = ctx.arrow().RARROW().getSymbol().getStartIndex();

        for (DashParser.MultiplicityContext multCtx : ctx.arrow().multiplicity()) {
            if (multCtx.getStart().getStartIndex() < arrowPosition) {
                mul1 = parseMultiplicity(multCtx);
            } else {
                mul2 = parseMultiplicity(multCtx);
            }
        }

        return new AlloyArrowExpr(
                new Pos(ctx), this.visit(ctx.expr2(0)), mul1, mul2, this.visit(ctx.expr2(1)));
    }

    @Override
    public AlloyArrowExpr visitArrowBindExpr(DashParser.ArrowBindExprContext ctx) {
        AlloyArrowExpr.Mul mul1 = AlloyArrowExpr.Mul.DEFAULTSET;
        AlloyArrowExpr.Mul mul2 = AlloyArrowExpr.Mul.DEFAULTSET;

        int arrowPosition = ctx.arrow().RARROW().getSymbol().getStartIndex();

        for (DashParser.MultiplicityContext multCtx : ctx.arrow().multiplicity()) {
            if (multCtx.getStart().getStartIndex() < arrowPosition) {
                mul1 = parseMultiplicity(multCtx);
            } else {
                mul2 = parseMultiplicity(multCtx);
            }
        }

        return new AlloyArrowExpr(
                new Pos(ctx), this.visit(ctx.expr2()), mul1, mul2, this.visit(ctx.bind()));
    }

    // ============================
    // IntersExpr
    // ============================
    @Override
    public AlloyIntersExpr visitIntersectExpr(DashParser.IntersectExprContext ctx) {
        return new AlloyIntersExpr(
                new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
    }

    @Override
    public AlloyIntersExpr visitIntersectBindExpr(DashParser.IntersectBindExprContext ctx) {
        return new AlloyIntersExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
    }

    // ============================
    // RelOvrdExpr
    // ============================
    @Override
    public AlloyRelOvrdExpr visitRelationOverrideExpr(DashParser.RelationOverrideExprContext ctx) {
        return new AlloyRelOvrdExpr(
                new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
    }

    @Override
    public AlloyRelOvrdExpr visitRelationOverrideBindExpr(
            DashParser.RelationOverrideBindExprContext ctx) {
        return new AlloyRelOvrdExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
    }

    // ============================
    // NumericExpr
    // ============================
    @Override
    public AlloyExpr visitNumericExpr(DashParser.NumericExprContext ctx) {
        if (null != ctx.CARDINALITY()) {
            return new AlloyNumCardinalityExpr(new Pos(ctx), this.visit(ctx.expr2()));
        } else if (null != ctx.SUM()) {
            return new AlloyNumSumExpr(new Pos(ctx), this.visit(ctx.expr2()));
        } else if (null != ctx.INT()) {
            return new AlloyNumIntExpr(new Pos(ctx), this.visit(ctx.expr2()));
        } else {
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }
    }

    // ============================
    // FunMul, FunDiv, FunRem
    // ============================
    @Override
    public AlloyBinaryExpr visitMulDivRemExpr(DashParser.MulDivRemExprContext ctx) {
        if (null != ctx.FUNMUL()) {
            return new AlloyFunMulExpr(
                    new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
        } else if (null != ctx.FUNDIV()) {
            return new AlloyFunDivExpr(
                    new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
        } else if (null != ctx.FUNREM()) {
            return new AlloyFunRemExpr(
                    new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
        } else {
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }
    }

    @Override
    public AlloyBinaryExpr visitMulDivRemBindExpr(DashParser.MulDivRemBindExprContext ctx) {
        if (null != ctx.FUNMUL()) {
            return new AlloyFunMulExpr(
                    new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else if (null != ctx.FUNDIV()) {
            return new AlloyFunDivExpr(
                    new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else if (null != ctx.FUNREM()) {
            return new AlloyFunRemExpr(
                    new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else {
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }
    }

    // ============================
    // Union, Diff, FunAdd, FunSub
    // ============================
    @Override
    public AlloyBinaryExpr visitPlusMinusExpr(DashParser.PlusMinusExprContext ctx) {
        if (null != ctx.PLUS()) {
            return new AlloyUnionExpr(
                    new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
        } else if (null != ctx.MINUS()) {
            return new AlloyDiffExpr(
                    new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
        } else if (null != ctx.FUNADD()) {
            return new AlloyFunAddExpr(
                    new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
        } else if (null != ctx.FUNSUB()) {
            return new AlloyFunSubExpr(
                    new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
        } else {
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }
    }

    @Override
    public AlloyBinaryExpr visitPlusMinusBindExpr(DashParser.PlusMinusBindExprContext ctx) {
        if (null != ctx.PLUS()) {
            return new AlloyUnionExpr(
                    new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else if (null != ctx.MINUS()) {
            return new AlloyDiffExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else if (null != ctx.FUNADD()) {
            return new AlloyFunAddExpr(
                    new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else if (null != ctx.FUNSUB()) {
            return new AlloyFunSubExpr(
                    new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else {
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }
    }

    // ============================
    // SHL, SHR, SHA
    // ============================
    @Override
    public AlloyBinaryExpr visitShiftExpr(DashParser.ShiftExprContext ctx) {
        if (null != ctx.SHL()) {
            return new AlloyShLExpr(
                    new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
        } else if (null != ctx.SHR()) {
            return new AlloyShRExpr(
                    new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
        } else if (null != ctx.SHA()) {
            return new AlloyShAExpr(
                    new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
        } else {
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }
    }

    @Override
    public AlloyBinaryExpr visitShiftBindExpr(DashParser.ShiftBindExprContext ctx) {
        if (null != ctx.SHL()) {
            return new AlloyShLExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else if (null != ctx.SHR()) {
            return new AlloyShRExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else if (null != ctx.SHA()) {
            return new AlloyShAExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else {
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }
    }

    // ============================
    // Qt
    // ============================
    @Override
    public AlloyQtExpr visitQuantifiedExpr(DashParser.QuantifiedExprContext ctx) {
        AlloyQtExpr.Quant qt = AlloyQtExpr.Quant.ALL;
        if (null != ctx.ALL()) {
            qt = AlloyQtExpr.Quant.ALL;
        } else if (null != ctx.NO()) {
            qt = AlloyQtExpr.Quant.NO;
        } else if (null != ctx.SOME()) {
            qt = AlloyQtExpr.Quant.SOME;
        } else if (null != ctx.LONE()) {
            qt = AlloyQtExpr.Quant.LONE;
        } else if (null != ctx.ONE()) {
            qt = AlloyQtExpr.Quant.ONE;
        } else if (null != ctx.SET()) {
            qt = AlloyQtExpr.Quant.SET;
        } else if (null != ctx.SEQ()) {
            qt = AlloyQtExpr.Quant.SEQ;
        } else {
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }
        return new AlloyQtExpr(new Pos(ctx), qt, this.visit(ctx.expr2()));
    }

    // ============================
    // Comparison & Equals & NotEquals
    // ============================
    @Override
    public AlloyBinaryExpr visitCompExpr(DashParser.CompExprContext ctx) {
        AlloyComparisonExpr.Negation neg;
        if (null != ctx.comparison().NOT_EXCL() || null != ctx.comparison().NOT()) {
            neg = AlloyComparisonExpr.Negation.NOT_EXCL;
        } else {
            neg = AlloyComparisonExpr.Negation.NONE;
        }

        // Equal & NotEquals
        if (null != ctx.comparison().EQUAL()) {
            if (neg == AlloyComparisonExpr.Negation.NOT_EXCL) {
                return new AlloyNotEqualsExpr(
                        new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
            } else {
                return new AlloyEqualsExpr(
                        new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
            }
        }

        AlloyComparisonExpr.Comp comp;
        if (null != ctx.comparison().IN()) {
            comp = AlloyComparisonExpr.Comp.IN;
        } else if (null != ctx.comparison().LT()) {
            comp = AlloyComparisonExpr.Comp.LESS_THAN;
        } else if (null != ctx.comparison().GT()) {
            comp = AlloyComparisonExpr.Comp.GREATER_THAN;
        } else if (null != ctx.comparison().LE()) {
            comp = AlloyComparisonExpr.Comp.LESS_EQUAL;
        } else if (null != ctx.comparison().EL()) {
            comp = AlloyComparisonExpr.Comp.EQUAL_LESS;
        } else if (null != ctx.comparison().GE()) {
            comp = AlloyComparisonExpr.Comp.GREATER_EQUAL;
        } else {
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }

        return new AlloyComparisonExpr(
                new Pos(ctx), this.visit(ctx.expr2(0)), neg, comp, this.visit(ctx.expr2(1)));
    }

    // ============================
    // Neg and UnTemp
    // ============================
    @Override
    public AlloyUnaryExpr visitUnTempExpr(DashParser.UnTempExprContext ctx) {
        if (null != ctx.NOT_EXCL() || null != ctx.NOT()) {
            return new AlloyNegExpr(new Pos(ctx), this.visit(ctx.expr2()));
        } else if (null != ctx.ALWAYS()) {
            return new AlloyAlwaysExpr(new Pos(ctx), this.visit(ctx.expr2()));
        } else if (null != ctx.EVENTUALLY()) {
            return new AlloyEventuallyExpr(new Pos(ctx), this.visit(ctx.expr2()));
        } else if (null != ctx.AFTER()) {
            return new AlloyAfterExpr(new Pos(ctx), this.visit(ctx.expr2()));
        } else if (null != ctx.HISTORICALLY()) {
            return new AlloyHistoricallyExpr(new Pos(ctx), this.visit(ctx.expr2()));
        } else if (null != ctx.ONCE()) {
            return new AlloyOnceExpr(new Pos(ctx), this.visit(ctx.expr2()));
        } else if (null != ctx.BEFORE()) {
            return new AlloyBeforeExpr(new Pos(ctx), this.visit(ctx.expr2()));
        } else {
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }
    }

    @Override
    public AlloyUnaryExpr visitUnTempBindExpr(DashParser.UnTempBindExprContext ctx) {
        if (null != ctx.NOT_EXCL() || null != ctx.NOT()) {
            return new AlloyNegExpr(new Pos(ctx), this.visit(ctx.bind()));
        } else if (null != ctx.ALWAYS()) {
            return new AlloyAlwaysExpr(new Pos(ctx), this.visit(ctx.bind()));
        } else if (null != ctx.EVENTUALLY()) {
            return new AlloyEventuallyExpr(new Pos(ctx), this.visit(ctx.bind()));
        } else if (null != ctx.AFTER()) {
            return new AlloyAfterExpr(new Pos(ctx), this.visit(ctx.bind()));
        } else if (null != ctx.HISTORICALLY()) {
            return new AlloyHistoricallyExpr(new Pos(ctx), this.visit(ctx.bind()));
        } else if (null != ctx.ONCE()) {
            return new AlloyOnceExpr(new Pos(ctx), this.visit(ctx.bind()));
        } else if (null != ctx.BEFORE()) {
            return new AlloyBeforeExpr(new Pos(ctx), this.visit(ctx.bind()));
        } else {
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }
    }

    // ============================
    // BinTemp
    // ============================
    @Override
    public AlloyBinaryExpr visitBinTempExpr(DashParser.BinTempExprContext ctx) {
        if (null != ctx.UNTIL()) {
            return new AlloyUntilExpr(
                    new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
        } else if (null != ctx.SINCE()) {
            return new AlloySinceExpr(
                    new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
        } else if (null != ctx.TRIGGERED()) {
            return new AlloyTriggeredExpr(
                    new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
        } else if (null != ctx.RELEASES()) {
            return new AlloyReleasesExpr(
                    new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
        } else {
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }
    }

    @Override
    public AlloyBinaryExpr visitBinTempBindExpr(DashParser.BinTempBindExprContext ctx) {
        if (null != ctx.UNTIL()) {
            return new AlloyUntilExpr(
                    new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else if (null != ctx.SINCE()) {
            return new AlloySinceExpr(
                    new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else if (null != ctx.TRIGGERED()) {
            return new AlloyTriggeredExpr(
                    new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else if (null != ctx.RELEASES()) {
            return new AlloyReleasesExpr(
                    new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else {
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }
    }

    // ============================
    // And
    // ============================
    @Override
    public AlloyAndExpr visitAndExpr(DashParser.AndExprContext ctx) {
        return new AlloyAndExpr(new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
    }

    @Override
    public AlloyAndExpr visitAndBindExpr(DashParser.AndBindExprContext ctx) {
        return new AlloyAndExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
    }

    // ============================
    // Body
    // ============================

    @Override
    public AlloyExpr visitBlockBody(DashParser.BlockBodyContext ctx) {
        return this.visit(ctx.block());
    }

    @Override
    public AlloyExpr visitBarBody(DashParser.BarBodyContext ctx) {
        return this.visit(ctx.expr1());
    }

    // ============================
    // Decl
    // ============================
    @Override
    public AlloyDecl visitDecl(DashParser.DeclContext ctx) {
        return (AlloyDecl) this.visit(ctx.getChild(0));
    }

    @Override
    public AlloyDecl visitDeclMul(DashParser.DeclMulContext ctx) {
        AlloyExprParseVis exprParseVis = new AlloyExprParseVis();

        final boolean isVar = null != ctx.VAR() ? true : false;

        final boolean isPrivate = null != ctx.PRIVATE() ? true : false;

        List<AlloyQnameExpr> qnames =
                ParserUtil.visitAll(ctx.qnames().qname(), exprParseVis, AlloyQnameExpr.class);

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
                qnames,
                isDisj2,
                quant,
                exprParseVis.visit(ctx.expr1()));
    }

    @Override
    public AlloyDecl visitDeclExact(DashParser.DeclExactContext ctx) {
        AlloyExprParseVis exprParseVis = new AlloyExprParseVis();

        final boolean isPrivate = null != ctx.PRIVATE() ? true : false;

        List<AlloyQnameExpr> qnames =
                ParserUtil.visitAll(ctx.qnames().qname(), exprParseVis, AlloyQnameExpr.class);

        return new AlloyDecl(
                new Pos(ctx),
                false,
                isPrivate,
                false,
                qnames,
                false,
                AlloyDecl.Quant.EXACTLY,
                exprParseVis.visit(ctx.expr1()));
    }

    // ============================
    // Terminal Nodes
    // ============================

    // Nothing to Override
    // save the trouble of switch statements in other visits; just call
    // this.visit(getChild(int))
    public AlloyExpr visitTerminal(TerminalNode tn) {
        int tokenType = tn.getSymbol().getType();

        switch (tokenType) {
            case DashParser.DISJ:
                return new AlloyDisjExpr(new Pos(tn));

            case DashParser.PRED_TOTALORDER:
                return new AlloyPredTotOrdExpr(new Pos(tn));

            case DashParser.INT:
                return new AlloyIntExpr(new Pos(tn));

            case DashParser.SUM:
                return new AlloySumExpr(new Pos(tn));

            case DashParser.SEQ:
                return new AlloySeqExpr(new Pos(tn));

            case DashParser.THIS:
                return new AlloyThisExpr(new Pos(tn));

            case DashParser.UNIV:
                return new AlloyUnivExpr(new Pos(tn));

            case DashParser.STRING:
                return new AlloyStringExpr(new Pos(tn));

            case DashParser.STEPS:
                return new AlloyStepsExpr(new Pos(tn));

            case DashParser.SIGINT:
                return new AlloySigIntExpr(new Pos(tn));

            case DashParser.SEQ_INT:
                return new AlloySeqIntExpr(new Pos(tn));

            case DashParser.NONE:
                return new AlloyNoneExpr(new Pos(tn));

            default:
                throw AlloyASTImplError.invalidCase(new Pos(tn));
        }
    }
}
