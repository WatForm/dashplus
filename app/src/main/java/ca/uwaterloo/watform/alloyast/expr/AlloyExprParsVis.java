package ca.uwaterloo.watform.alloyast.expr;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import antlr.generated.AlloyParser.AssignmentContext;
import antlr.generated.AlloyParser.DeclContext;
import antlr.generated.AlloyParser.NameContext;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.helper.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.misc.AlloyDeclParsVis;
import java.util.ArrayList;
import java.util.List;

public final class AlloyExprParsVis extends AlloyBaseVisitor<AlloyExpr> {

	// ====================================================================================
	// Bind
	// ====================================================================================
	@Override
	public AlloyExpr visitBindExpr(AlloyParser.BindExprContext ctx) {
		return this.visit(ctx.bind());
	}

	@Override
	public AlloyExpr visitLet(AlloyParser.LetContext ctx) {

		AlloyAsnExprHelperParsVis asnExprHelperParsVis = new AlloyAsnExprHelperParsVis();

		List<AlloyAsnExprHelper> asns = new ArrayList<>();
		for (AssignmentContext asn : ctx.assignment()) {
			asns.add(asnExprHelperParsVis.visit(asn));
		}
		return new AlloyLetExpr(new Pos(ctx), asns, this.visit(ctx.body()));
	}

	@Override
	public AlloyExpr visitQuantificationExpr(AlloyParser.QuantificationExprContext ctx) {
		List<AlloyDecl> decls = new ArrayList<>();
		AlloyDeclParsVis declParsVis = new AlloyDeclParsVis();
		for (DeclContext declCtx : ctx.decl()) {
			decls.add((AlloyDecl) declParsVis.visit(declCtx));
		}

		if (null != ctx.ALL()) {
			return new AlloyQuantificationExpr(
					new Pos(ctx), AlloyQuantificationExpr.Quant.ALL, decls, this.visit(ctx.body()));
		} else if (null != ctx.NO()) {
			return new AlloyQuantificationExpr(
					new Pos(ctx), AlloyQuantificationExpr.Quant.NO, decls, this.visit(ctx.body()));
		} else if (null != ctx.SOME()) {
			return new AlloyQuantificationExpr(
					new Pos(ctx), AlloyQuantificationExpr.Quant.SOME, decls, this.visit(ctx.body()));
		} else if (null != ctx.LONE()) {
			return new AlloyQuantificationExpr(
					new Pos(ctx), AlloyQuantificationExpr.Quant.LONE, decls, this.visit(ctx.body()));
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

	// ====================================================================================
	// impliesExpr
	// ====================================================================================

	// ====================================================================================
	// expr2
	// ====================================================================================

	// ============================
	// PrimeExpr
	// ============================
	@Override
	public AlloyPrimeExpr visitPrimeExpr(AlloyParser.PrimeExprContext ctx) {
		return new AlloyPrimeExpr(new Pos(ctx), this.visit(ctx.expr2()));
	}

	// ============================
	// Dot
	// ============================
	@Override
	public AlloyExpr visitDotBuiltinExpr(AlloyParser.DotBuiltinExprContext ctx) {
		if (null != ctx.DISJ()) {
			return new AlloyDotJoinExpr(
					new Pos(ctx), this.visit(ctx.expr2()), new AlloySumExpr(new Pos(ctx.DISJ())));
		} else if (null != ctx.PRED_TOTALORDER()) {
			return new AlloyDotJoinExpr(
					new Pos(ctx), this.visit(ctx.expr2()), new AlloySumExpr(new Pos(ctx.PRED_TOTALORDER())));
		} else if (null != ctx.INT()) {
			return new AlloyDotJoinExpr(
					new Pos(ctx), this.visit(ctx.expr2()), new AlloySumExpr(new Pos(ctx.INT())));
		} else if (null != ctx.SUM()) {
			return new AlloyDotJoinExpr(
					new Pos(ctx), this.visit(ctx.expr2()), new AlloySumExpr(new Pos(ctx.SUM())));
		} else {
			throw new AlloyUnexpTokenEx(ctx);
		}
	}

	@Override
	public AlloyExpr visitDotBindExpr(AlloyParser.DotBindExprContext ctx) {
		return new AlloyDotJoinExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
	}

	@Override
	public AlloyExpr visitDotExpr(AlloyParser.DotExprContext ctx) {
		return new AlloyDotJoinExpr(new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
	}

	// ============================
	// NumericExpr (incomplete: needs SUM and INT)
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
	// And
	// ============================
	@Override
	public AlloyExpr visitAndExpr(AlloyParser.AndExprContext ctx) {
		return new AlloyAndExpr(new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
	}

	@Override
	public AlloyExpr visitAndBindExpr(AlloyParser.AndBindExprContext ctx) {
		return new AlloyAndExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
	}

	// ============================
	// SigRef
	// ============================
	@Override
	public AlloyExpr visitSigRef(AlloyParser.SigRefContext ctx) {
		if (null != ctx.qname()) {
			return this.visit(ctx.qname());
		} else if (null != ctx.UNIV()) {
			return new AlloyUnivExpr(new Pos(ctx));
		} else if (null != ctx.STRING()) {
			return new AlloyStringExpr(new Pos(ctx));
		} else if (null != ctx.STEPS()) {
			return new AlloyStepsExpr(new Pos(ctx));
		} else if (null != ctx.SIGINT()) {
			return new AlloySigIntExpr(new Pos(ctx));
		} else if (null != ctx.SEQ_INT()) {
			return new AlloySeqIntExpr(new Pos(ctx));
		} else if (null != ctx.NONE()) {
			return new AlloyNoneExpr(new Pos(ctx));
		} else {
			throw new AlloyUnexpTokenEx(ctx);
		}
	}

	@Override
	public AlloyNameExpr visitName(AlloyParser.NameContext ctx) {
		return new AlloyNameExpr(new Pos(ctx), ctx.ID().getText());
	}

	@Override
	public AlloyExpr visitSimpleQname(AlloyParser.SimpleQnameContext ctx) {
		return new AlloyQnameExpr(new Pos(ctx), (AlloyNameExpr) this.visit(ctx.name()));
	}

	@Override
	public AlloyExpr visitQualifiedQname(AlloyParser.QualifiedQnameContext ctx) {
		List<AlloyNameExpr> nameExprList = new ArrayList<>();
		if (null != ctx.SEQ()) {
			nameExprList.add(new AlloyThisExpr(new Pos(ctx.SEQ())));
		} else if (null != ctx.THIS()) {
			nameExprList.add(new AlloyThisExpr(new Pos(ctx.THIS())));
		}
		for (NameContext nameCtx : ctx.name()) {
			nameExprList.add((AlloyNameExpr) this.visit(nameCtx));
		}
		return new AlloyQnameExpr(new Pos(ctx), nameExprList);
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
	// Block
	// ============================
	@Override
	public AlloyExpr visitBlock(AlloyParser.BlockContext ctx) {
		List<AlloyExpr> exprs = new ArrayList<>();
		for (AlloyParser.Expr1Context exprCtx : ctx.expr1()) {
			exprs.add(this.visit(exprCtx));
		}
		return new AlloyBlock(new Pos(ctx), exprs);
	}
}
