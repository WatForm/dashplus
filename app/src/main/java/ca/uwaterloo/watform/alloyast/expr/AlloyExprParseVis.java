package ca.uwaterloo.watform.alloyast.expr;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import antlr.generated.AlloyParser.NameContext;
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

public final class AlloyExprParseVis extends AlloyBaseVisitor<AlloyExpr> {

    // ====================================================================================
    // Bind
    // ====================================================================================
    @Override
    public AlloyExpr visitBindExpr(AlloyParser.BindExprContext ctx) {
        return this.visit(ctx.bind());
    }

    @Override
    public AlloyLetExpr visitLet(AlloyParser.LetContext ctx) {
        return new AlloyLetExpr(
                new Pos(ctx),
                ParserUtil.visitAll(ctx.assignment(), new AlloyLetAsnParseVis(), AlloyLetAsn.class),
                this.visit(ctx.body()));
    }

    @Override
    public AlloyQuantificationExpr visitQuantificationExpr(
            AlloyParser.QuantificationExprContext ctx) {
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
            throw new AlloyUnexpTokenEx(ctx);
        }
    }

    // ====================================================================================
    // expr1
    // ====================================================================================

    // ============================
    // Iff
    // ============================
    @Override
    public AlloyIffExpr visitIffExpr(AlloyParser.IffExprContext ctx) {
        return new AlloyIffExpr(new Pos(ctx), this.visit(ctx.expr1(0)), this.visit(ctx.expr1(1)));
    }

    @Override
    public AlloyIffExpr visitIffBindExpr(AlloyParser.IffBindExprContext ctx) {
        return new AlloyIffExpr(new Pos(ctx), this.visit(ctx.expr1()), this.visit(ctx.bind()));
    }

    // ============================
    // Or
    // ============================

    @Override
    public AlloyOrExpr visitOrExpr(AlloyParser.OrExprContext ctx) {
        return new AlloyOrExpr(new Pos(ctx), this.visit(ctx.expr1(0)), this.visit(ctx.expr1(1)));
    }

    @Override
    public AlloyOrExpr visitOrBindExpr(AlloyParser.OrBindExprContext ctx) {
        return new AlloyOrExpr(new Pos(ctx), this.visit(ctx.expr1()), this.visit(ctx.bind()));
    }

    // ============================
    // StateSeq
    // ============================

    @Override
    public AlloyStateSeqExpr visitStateSeqExpr(AlloyParser.StateSeqExprContext ctx) {
        return new AlloyStateSeqExpr(
                new Pos(ctx), this.visit(ctx.expr1(0)), this.visit(ctx.expr1(1)));
    }

    @Override
    public AlloyStateSeqExpr visitStateSeqBindExpr(AlloyParser.StateSeqBindExprContext ctx) {
        return new AlloyStateSeqExpr(new Pos(ctx), this.visit(ctx.expr1()), this.visit(ctx.bind()));
    }

    // ====================================================================================
    // impliesExpr
    // ====================================================================================
    @Override
    public AlloyExpr visitImpExprOpenOrClose(AlloyParser.ImpExprOpenOrCloseContext ctx) {
        return this.visit(ctx.impliesExpr());
    }

    @Override
    public AlloyExpr visitImpExprCloseFromImplies(AlloyParser.ImpExprCloseFromImpliesContext ctx) {
        return this.visit(ctx.impliesExprClose());
    }

    @Override
    public AlloyExpr visitImpExprOpenFromImplies(AlloyParser.ImpExprOpenFromImpliesContext ctx) {
        return this.visit(ctx.impliesExprOpen());
    }

    @Override
    public AlloyIteExpr visitIteCloseExpr(AlloyParser.IteCloseExprContext ctx) {
        return new AlloyIteExpr(
                new Pos(ctx),
                this.visit(ctx.expr2()),
                this.visit(ctx.impliesExprClose(0)),
                this.visit(ctx.impliesExprClose(1)));
    }

    @Override
    public AlloyIteExpr visitIteBindCloseExpr(AlloyParser.IteBindCloseExprContext ctx) {
        return new AlloyIteExpr(
                new Pos(ctx),
                this.visit(ctx.expr2()),
                this.visit(ctx.impliesExprClose()),
                this.visit(ctx.bind()));
    }

    @Override
    public AlloyExpr visitExpr2FromImpClose(AlloyParser.Expr2FromImpCloseContext ctx) {
        return this.visit(ctx.expr2());
    }

    @Override
    public AlloyIteExpr visitIteOpenExpr(AlloyParser.IteOpenExprContext ctx) {
        return new AlloyIteExpr(
                new Pos(ctx),
                this.visit(ctx.expr2()),
                this.visit(ctx.impliesExprClose()),
                this.visit(ctx.impliesExprOpen()));
    }

    @Override
    public AlloyImpliesExpr visitImpExpr(AlloyParser.ImpExprContext ctx) {
        return new AlloyImpliesExpr(
                new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.impliesExpr()));
    }

    @Override
    public AlloyImpliesExpr visitImpBindExpr(AlloyParser.ImpBindExprContext ctx) {
        return new AlloyImpliesExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
    }

    // ====================================================================================
    // baseExpr
    // ====================================================================================

    @Override
    public AlloyNumExpr visitNumber(AlloyParser.NumberContext ctx) {
        return new AlloyNumExpr(new Pos(ctx), null == ctx.MINUS(), ctx.NUMBER().getText());
    }

    @Override
    public AlloyStrLiteralExpr visitStrLiteralExpr(AlloyParser.StrLiteralExprContext ctx) {
        return new AlloyStrLiteralExpr(new Pos(ctx), ctx.STRING_LITERAL().getText());
    }

    @Override
    public AlloyIdenExpr visitIdenExpr(AlloyParser.IdenExprContext ctx) {
        return new AlloyIdenExpr(new Pos(ctx));
    }

    @Override
    public AlloyThisExpr visitThisExpr(AlloyParser.ThisExprContext ctx) {
        return new AlloyThisExpr(new Pos(ctx));
    }

    @Override
    public AlloyFunMinExpr visitFunMinExpr(AlloyParser.FunMinExprContext ctx) {
        return new AlloyFunMinExpr(new Pos(ctx));
    }

    @Override
    public AlloyFunMaxExpr visitFunMaxExpr(AlloyParser.FunMaxExprContext ctx) {
        return new AlloyFunMaxExpr(new Pos(ctx));
    }

    @Override
    public AlloyFunNextExpr visitFunNextExpr(AlloyParser.FunNextExprContext ctx) {
        return new AlloyFunNextExpr(new Pos(ctx));
    }

    @Override
    public AlloyParenExpr visitParenExpr(AlloyParser.ParenExprContext ctx) {
        return new AlloyParenExpr(new Pos(ctx), this.visit(ctx.expr1()));
    }

    @Override
    public AlloyExpr visitSigRefExpr(AlloyParser.SigRefExprContext ctx) {
        return this.visit(ctx.sigRef());
    }

    @Override
    public AlloyAtNameExpr visitAtNameExpr(AlloyParser.AtNameExprContext ctx) {
        return new AlloyAtNameExpr(new Pos(ctx), (AlloyNameExpr) this.visit(ctx.name()));
    }

    @Override
    public AlloyBlock visitBlockExpr(AlloyParser.BlockExprContext ctx) {
        return (AlloyBlock) this.visit(ctx.block());
    }

    @Override
    public AlloyComprehensionExpr visitComprehensionExpr(AlloyParser.ComprehensionExprContext ctx) {
        return new AlloyComprehensionExpr(
                new Pos(ctx),
                ParserUtil.visitAll(ctx.declMul(), this, AlloyDecl.class),
                (null != ctx.body()) ? this.visit(ctx.body()) : null);
    }

    // ============================
    // Block
    // ============================
    @Override
    public AlloyBlock visitBlock(AlloyParser.BlockContext ctx) {
        return new AlloyBlock(
                new Pos(ctx), ParserUtil.visitAll(ctx.expr1(), this, AlloyExpr.class));
    }

    // ============================
    // SigRef
    // ============================
    @Override
    public AlloyVarExpr visitSigRef(AlloyParser.SigRefContext ctx) {
        if (null != ctx.qname()) {
            return (AlloyVarExpr) this.visit(ctx.qname());
        }
        return (AlloyVarExpr) this.visit(ctx.getChild(0));
    }

    @Override
    public AlloyNameExpr visitName(AlloyParser.NameContext ctx) {
        return new AlloyNameExpr(new Pos(ctx), ctx.ID().getText());
    }

    @Override
    public AlloyQnameExpr visitSimpleQname(AlloyParser.SimpleQnameContext ctx) {
        return new AlloyQnameExpr(new Pos(ctx), (AlloyNameExpr) this.visit(ctx.name()));
    }

    @Override
    public AlloyQnameExpr visitQualifiedQname(AlloyParser.QualifiedQnameContext ctx) {
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
    public AlloyUnaryExpr visitTransExpr(AlloyParser.TransExprContext ctx) {
        if (null != ctx.TRANS()) {
            return new AlloyTransExpr(new Pos(ctx), this.visit(ctx.getChild(1)));
        } else if (null != ctx.TRANS_CLOS()) {
            return new AlloyTransClosExpr(new Pos(ctx), this.visit(ctx.getChild(1)));
        } else if (null != ctx.REFL_TRANS_CLOS()) {
            return new AlloyReflTransClosExpr(new Pos(ctx), this.visit(ctx.getChild(1)));
        } else {
            throw new AlloyUnexpTokenEx(ctx);
        }
    }

    // ============================
    // PrimeExpr
    // ============================
    @Override
    public AlloyPrimeExpr visitPrimeExpr(AlloyParser.PrimeExprContext ctx) {
        return new AlloyPrimeExpr(new Pos(ctx), this.visit(ctx.getChild(0)));
    }

    // ====================================================================================
    // expr2
    // ====================================================================================

    // ============================
    // Dot
    // ============================
    @Override
    public AlloyDotExpr visitDotExpr(AlloyParser.DotExprContext ctx) {
        return new AlloyDotExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.getChild(2)));
    }

    // ============================
    // BracketExpr
    // ============================

    @Override
    public AlloyBracketExpr visitBracketExpr(AlloyParser.BracketExprContext ctx) {
        return new AlloyBracketExpr(
                new Pos(ctx),
                this.visit(ctx.expr2()),
                ParserUtil.visitAll(ctx.expr1(), this, AlloyExpr.class));
    }

    @Override
    public AlloyBracketExpr visitBracketBuiltinExpr(AlloyParser.BracketBuiltinExprContext ctx) {
        return new AlloyBracketExpr(
                new Pos(ctx),
                this.visit(ctx.getChild(0)),
                ParserUtil.visitAll(ctx.expr1(), this, AlloyExpr.class));
    }

    // ============================
    // RngRestr
    // ============================

    @Override
    public AlloyRngRestrExpr visitRangExpr(AlloyParser.RangExprContext ctx) {
        return new AlloyRngRestrExpr(
                new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
    }

    @Override
    public AlloyRngRestrExpr visitRangBindExpr(AlloyParser.RangBindExprContext ctx) {
        return new AlloyRngRestrExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
    }

    // ============================
    // DomRestr
    // ============================
    @Override
    public AlloyDomRestrExpr visitDomExpr(AlloyParser.DomExprContext ctx) {
        return new AlloyDomRestrExpr(
                new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
    }

    @Override
    public AlloyDomRestrExpr visitDomBindExpr(AlloyParser.DomBindExprContext ctx) {
        return new AlloyDomRestrExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
    }

    // ============================
    // ArrowExpr
    // ============================
    private AlloyArrowExpr.Mul parseMultiplicity(AlloyParser.MultiplicityContext multCtx) {
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
    public AlloyArrowExpr visitArrowExpr(AlloyParser.ArrowExprContext ctx) {
        AlloyArrowExpr.Mul mul1 = AlloyArrowExpr.Mul.DEFAULTSET;
        AlloyArrowExpr.Mul mul2 = AlloyArrowExpr.Mul.DEFAULTSET;

        int arrowPosition = ctx.arrow().RARROW().getSymbol().getStartIndex();

        for (AlloyParser.MultiplicityContext multCtx : ctx.arrow().multiplicity()) {
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
    public AlloyArrowExpr visitArrowBindExpr(AlloyParser.ArrowBindExprContext ctx) {
        AlloyArrowExpr.Mul mul1 = AlloyArrowExpr.Mul.DEFAULTSET;
        AlloyArrowExpr.Mul mul2 = AlloyArrowExpr.Mul.DEFAULTSET;

        int arrowPosition = ctx.arrow().RARROW().getSymbol().getStartIndex();

        for (AlloyParser.MultiplicityContext multCtx : ctx.arrow().multiplicity()) {
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
    public AlloyIntersExpr visitIntersectExpr(AlloyParser.IntersectExprContext ctx) {
        return new AlloyIntersExpr(
                new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
    }

    @Override
    public AlloyIntersExpr visitIntersectBindExpr(AlloyParser.IntersectBindExprContext ctx) {
        return new AlloyIntersExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
    }

    // ============================
    // RelOvrdExpr
    // ============================
    @Override
    public AlloyRelOvrdExpr visitRelationOverrideExpr(AlloyParser.RelationOverrideExprContext ctx) {
        return new AlloyRelOvrdExpr(
                new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
    }

    @Override
    public AlloyRelOvrdExpr visitRelationOverrideBindExpr(
            AlloyParser.RelationOverrideBindExprContext ctx) {
        return new AlloyRelOvrdExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
    }

    // ============================
    // NumericExpr
    // ============================
    @Override
    public AlloyExpr visitNumericExpr(AlloyParser.NumericExprContext ctx) {
        if (null != ctx.CARDINALITY()) {
            return new AlloyNumCardinalityExpr(new Pos(ctx), this.visit(ctx.expr2()));
        } else if (null != ctx.SUM()) {
            return new AlloyNumSumExpr(new Pos(ctx), this.visit(ctx.expr2()));
        } else if (null != ctx.INT()) {
            return new AlloyNumIntExpr(new Pos(ctx), this.visit(ctx.expr2()));
        } else {
            throw new AlloyUnexpTokenEx(ctx);
        }
    }

    // ============================
    // FunMul, FunDiv, FunRem
    // ============================
    @Override
    public AlloyBinaryExpr visitMulDivRemExpr(AlloyParser.MulDivRemExprContext ctx) {
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
            throw new AlloyUnexpTokenEx(ctx);
        }
    }

    @Override
    public AlloyBinaryExpr visitMulDivRemBindExpr(AlloyParser.MulDivRemBindExprContext ctx) {
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
            throw new AlloyUnexpTokenEx(ctx);
        }
    }

    // ============================
    // Union, Diff, FunAdd, FunSub
    // ============================
    @Override
    public AlloyBinaryExpr visitPlusMinusExpr(AlloyParser.PlusMinusExprContext ctx) {
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
            throw new AlloyUnexpTokenEx(ctx);
        }
    }

    @Override
    public AlloyBinaryExpr visitPlusMinusBindExpr(AlloyParser.PlusMinusBindExprContext ctx) {
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
            throw new AlloyUnexpTokenEx(ctx);
        }
    }

    // ============================
    // SHL, SHR, SHA
    // ============================
    @Override
    public AlloyBinaryExpr visitShiftExpr(AlloyParser.ShiftExprContext ctx) {
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
            throw new AlloyUnexpTokenEx(ctx);
        }
    }

    @Override
    public AlloyBinaryExpr visitShiftBindExpr(AlloyParser.ShiftBindExprContext ctx) {
        if (null != ctx.SHL()) {
            return new AlloyShLExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else if (null != ctx.SHR()) {
            return new AlloyShRExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else if (null != ctx.SHA()) {
            return new AlloyShAExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
        } else {
            throw new AlloyUnexpTokenEx(ctx);
        }
    }

    // ============================
    // Qt
    // ============================
    @Override
    public AlloyQtExpr visitQuantifiedExpr(AlloyParser.QuantifiedExprContext ctx) {
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
            throw new AlloyUnexpTokenEx(ctx);
        }
        return new AlloyQtExpr(new Pos(ctx), qt, this.visit(ctx.expr2()));
    }

    // ============================
    // Comparison
    // ============================
    @Override
    public AlloyComparisonExpr visitCompExpr(AlloyParser.CompExprContext ctx) {
        AlloyComparisonExpr.Negation neg;
        if (null != ctx.comparison().NOT_EXCL()) {
            neg = AlloyComparisonExpr.Negation.NOT_EXCL;
        } else if (null != ctx.comparison().NOT()) {
            neg = AlloyComparisonExpr.Negation.NOT;
        } else {
            neg = AlloyComparisonExpr.Negation.NONE;
        }

        AlloyComparisonExpr.Comp comp;
        if (null != ctx.comparison().IN()) {
            comp = AlloyComparisonExpr.Comp.IN;
        } else if (null != ctx.comparison().EQUAL()) {
            comp = AlloyComparisonExpr.Comp.EQUAL;
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
            throw new AlloyUnexpTokenEx(ctx);
        }

        return new AlloyComparisonExpr(
                new Pos(ctx), this.visit(ctx.expr2(0)), neg, comp, this.visit(ctx.expr2(1)));
    }

    // ============================
    // Neg and UnTemp
    // ============================
    @Override
    public AlloyUnaryExpr visitUnTempExpr(AlloyParser.UnTempExprContext ctx) {
        if (null != ctx.NOT_EXCL()) {
            return new AlloyNegExpr(
                    new Pos(ctx), AlloyNegExpr.Negation.NOT_EXCL, this.visit(ctx.expr2()));
        } else if (null != ctx.NOT()) {
            return new AlloyNegExpr(
                    new Pos(ctx), AlloyNegExpr.Negation.NOT, this.visit(ctx.expr2()));
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
            throw new AlloyUnexpTokenEx(ctx);
        }
    }

    @Override
    public AlloyUnaryExpr visitUnTempBindExpr(AlloyParser.UnTempBindExprContext ctx) {
        if (null != ctx.NOT_EXCL()) {
            return new AlloyNegExpr(
                    new Pos(ctx), AlloyNegExpr.Negation.NOT_EXCL, this.visit(ctx.bind()));
        } else if (null != ctx.NOT()) {
            return new AlloyNegExpr(
                    new Pos(ctx), AlloyNegExpr.Negation.NOT, this.visit(ctx.bind()));
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
            throw new AlloyUnexpTokenEx(ctx);
        }
    }

    // ============================
    // BinTemp
    // ============================
    @Override
    public AlloyBinaryExpr visitBinTempExpr(AlloyParser.BinTempExprContext ctx) {
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
            throw new AlloyUnexpTokenEx(ctx);
        }
    }

    @Override
    public AlloyBinaryExpr visitBinTempBindExpr(AlloyParser.BinTempBindExprContext ctx) {
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
            throw new AlloyUnexpTokenEx(ctx);
        }
    }

    // ============================
    // And
    // ============================
    @Override
    public AlloyAndExpr visitAndExpr(AlloyParser.AndExprContext ctx) {
        return new AlloyAndExpr(new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
    }

    @Override
    public AlloyAndExpr visitAndBindExpr(AlloyParser.AndBindExprContext ctx) {
        return new AlloyAndExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
    }

    // ============================
    // Body
    // ============================

    @Override
    public AlloyExpr visitBlockBody(AlloyParser.BlockBodyContext ctx) {
        return this.visit(ctx.block());
    }

    @Override
    public AlloyExpr visitBarBody(AlloyParser.BarBodyContext ctx) {
        return this.visit(ctx.expr1());
    }

    // ============================
    // Decl
    // ============================
    @Override
    public AlloyDecl visitDecl(AlloyParser.DeclContext ctx) {
        return (AlloyDecl) this.visit(ctx.getChild(0));
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

    // ============================
    // Terminal Nodes
    // ============================

    // Nothing to Override
    // save the trouble of switch statements in other visits; just call
    // this.visit(getChild(int))
    public AlloyExpr visitTerminal(TerminalNode tn) {
        int tokenType = tn.getSymbol().getType();

        switch (tokenType) {
            case AlloyParser.DISJ:
                return new AlloyDisjExpr(new Pos(tn));

            case AlloyParser.PRED_TOTALORDER:
                return new AlloyPredTotOrdExpr(new Pos(tn));

            case AlloyParser.INT:
                return new AlloyIntExpr(new Pos(tn));

            case AlloyParser.SUM:
                return new AlloySumExpr(new Pos(tn));

            case AlloyParser.SEQ:
                return new AlloySeqExpr(new Pos(tn));

            case AlloyParser.THIS:
                return new AlloyThisExpr(new Pos(tn));

            case AlloyParser.UNIV:
                return new AlloyUnivExpr(new Pos(tn));

            case AlloyParser.STRING:
                return new AlloyStringExpr(new Pos(tn));

            case AlloyParser.STEPS:
                return new AlloyStepsExpr(new Pos(tn));

            case AlloyParser.SIGINT:
                return new AlloySigIntExpr(new Pos(tn));

            case AlloyParser.SEQ_INT:
                return new AlloySeqIntExpr(new Pos(tn));

            case AlloyParser.NONE:
                return new AlloyNoneExpr(new Pos(tn));

            default:
                throw new AlloyUnexpTokenEx(tn);
        }
    }
}
