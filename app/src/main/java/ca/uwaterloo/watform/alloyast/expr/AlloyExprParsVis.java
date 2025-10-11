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
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;

final class UnexpectedTokenException extends IllegalStateException {
	public UnexpectedTokenException(ParserRuleContext ctx) {
		super(buildMessage(ctx));
	}

	private static String buildMessage(ParserRuleContext ctx) {
		return "Unexpected token at line"
				+ ctx.getStart().getLine()
				+ ", position "
				+ ctx.getStart().getCharPositionInLine()
				+ ": "
				+ ctx.getText();
	}
}

public final class AlloyExprParsVis extends AlloyBaseVisitor<AlloyExpr> {

	// ============================
	// Bind (incomplete: AlloyDecl needs ptrs, toString and its own parsVis
	// ============================
	@Override
	public AlloyExpr visitBindExpr(AlloyParser.BindExprContext ctx) {
		return this.visit(ctx.bind());
	}

	@Override
	public AlloyExpr visitLet(AlloyParser.LetContext ctx) {
		System.out.println("Visiting LetContext");

		AlloyAsnExprHelperParsVis asnExprHelperParsVis = new AlloyAsnExprHelperParsVis();

		List<AlloyAsnExprHelper> asns = new ArrayList<>();
		for (AssignmentContext asn : ctx.assignment()) {
			asns.add(asnExprHelperParsVis.visit(asn));
		}
		return new AlloyLetExpr(new Pos(ctx), asns, this.visit(ctx.body()));
	}

	@Override
	public AlloyExpr visitQuantificationExpr(AlloyParser.QuantificationExprContext ctx) {
		System.out.println("Visiting QuantificationExprContext");
		List<AlloyDecl> decls = new ArrayList<>();
		for (DeclContext declCtx : ctx.decl()) {
			decls.add((AlloyDecl) this.visit(declCtx));
		}

		if (null != ctx.ALL()) {
			return new AlloyQuantificationExpr(
					new Pos(ctx), AlloyQuantificationExpr.Op.ALL, decls, this.visit(ctx.body()));
		} else if (null != ctx.NO()) {
			return new AlloyQuantificationExpr(
					new Pos(ctx), AlloyQuantificationExpr.Op.NO, decls, this.visit(ctx.body()));
		} else if (null != ctx.SOME()) {
			return new AlloyQuantificationExpr(
					new Pos(ctx), AlloyQuantificationExpr.Op.SOME, decls, this.visit(ctx.body()));
		} else if (null != ctx.LONE()) {
			return new AlloyQuantificationExpr(
					new Pos(ctx), AlloyQuantificationExpr.Op.LONE, decls, this.visit(ctx.body()));
		} else if (null != ctx.ONE()) {
			return new AlloyQuantificationExpr(
					new Pos(ctx), AlloyQuantificationExpr.Op.ONE, decls, this.visit(ctx.body()));
		} else if (null != ctx.SUM()) {
			return new AlloyQuantificationExpr(
					new Pos(ctx), AlloyQuantificationExpr.Op.SUM, decls, this.visit(ctx.body()));
		} else {
			throw new UnexpectedTokenException(ctx);
		}
	}

	// ============================
	// expr1
	// ============================

	// ============================
	// impliesExpr
	// ============================

	// ============================
	// expr2
	// ============================

	// ============================
	// PrimeExpr
	// ============================
	@Override
	public AlloyPrimeExpr visitPrimeExpr(AlloyParser.PrimeExprContext ctx) {
		System.out.println("Visiting PrimeExprContext");
		return new AlloyPrimeExpr(new Pos(ctx), this.visit(ctx.expr2()));
	}

	// ============================
	// Dot (incomplete: needs DISJ, PRED_TOTALORDER, etc)
	// ============================
	@Override
	public AlloyExpr visitDotBuiltinExpr(AlloyParser.DotBuiltinExprContext ctx) {
		System.out.println("Visiting DotBuiltinExprContext");
		this.visit(ctx.expr2());
		if (null != ctx.DISJ()) {
			this.visit(ctx.DISJ());
		} else if (null != ctx.PRED_TOTALORDER()) {
			this.visit(ctx.PRED_TOTALORDER());
		} else if (null != ctx.INT()) {
			this.visit(ctx.INT());
		} else if (null != ctx.SUM()) {
			this.visit(ctx.SUM());
		} else {
			throw new UnexpectedTokenException(ctx);
		}
		return new AlloyDotJoinExpr(new Pos(ctx));
	}

	@Override
	public AlloyExpr visitDotBindExpr(AlloyParser.DotBindExprContext ctx) {
		System.out.println("Visiting DotBindExprContext");
		return new AlloyDotJoinExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
	}

	@Override
	public AlloyExpr visitDotExpr(AlloyParser.DotExprContext ctx) {
		System.out.println("Visiting DotJoinContext");
		return new AlloyDotJoinExpr(new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
	}

	// ============================
	// NumericExpr (incomplete: needs SUM and INT)
	// ============================
	@Override
	public AlloyExpr visitNumericExpr(AlloyParser.NumericExprContext ctx) {
		System.out.println("Visiting NumericExprContext");
		if (null != ctx.CARDINALITY()) {
			return new AlloyCardinalityExpr(new Pos(ctx), this.visit(ctx.expr2()));
		} else if (null != ctx.SUM()) {
			return new AlloyCardinalityExpr(new Pos(ctx));
		} else if (null != ctx.INT()) {
			return new AlloyCardinalityExpr(new Pos(ctx));
		} else {
			throw new UnexpectedTokenException(ctx);
		}
	}

	// ============================
	// And
	// ============================
	@Override
	public AlloyExpr visitAndExpr(AlloyParser.AndExprContext ctx) {
		System.out.println("Visiting AndExprContext");
		return new AlloyAndExpr(new Pos(ctx), this.visit(ctx.expr2(0)), this.visit(ctx.expr2(1)));
	}

	@Override
	public AlloyExpr visitAndBindExpr(AlloyParser.AndBindExprContext ctx) {
		System.out.println("Visiting AndBindExprContext");
		return new AlloyAndExpr(new Pos(ctx), this.visit(ctx.expr2()), this.visit(ctx.bind()));
	}

	// ============================
	// SigRef
	// ============================
	@Override
	public AlloyExpr visitSigRef(AlloyParser.SigRefContext ctx) {
		System.out.println("Visiting SigRefContext");
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
			throw new UnexpectedTokenException(ctx);
		}
	}

	@Override
	public AlloyNameExpr visitName(AlloyParser.NameContext ctx) {
		return new AlloyNameExpr(new Pos(ctx), ctx.ID().getText());
	}

	@Override
	public AlloyExpr visitSimpleQname(AlloyParser.SimpleQnameContext ctx) {
		System.out.println("Visiting SimpleQnameContext");
		return new AlloyQnameExpr(new Pos(ctx), (AlloyNameExpr) this.visit(ctx.name()));
	}

	@Override
	public AlloyExpr visitQualifiedQname(AlloyParser.QualifiedQnameContext ctx) {
		System.out.println("Visiting QualifiedQnameContext");
		List<AlloyNameExpr> nameExprList = new ArrayList<>();
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
		System.out.println("Visiting BlockBodyContext");
		return this.visit(ctx.block());
	}

	@Override
	public AlloyExpr visitBarBody(AlloyParser.BarBodyContext ctx) {
		System.out.println("Visiting BarBodyContext");
		return this.visit(ctx.expr1());
	}

	// ============================
	// Block
	// ============================
	@Override
	public AlloyExpr visitBlock(AlloyParser.BlockContext ctx) {
		System.out.println("Visiting BlockContext");
		List<AlloyExpr> exprs = new ArrayList<>();
		for (AlloyParser.Expr1Context exprCtx : ctx.expr1()) {
			exprs.add(this.visit(exprCtx));
		}
		return new AlloyBlock(new Pos(ctx), exprs);
	}
}
