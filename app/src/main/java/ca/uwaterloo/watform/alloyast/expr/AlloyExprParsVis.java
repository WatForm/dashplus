package ca.uwaterloo.watform.alloyast.expr;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import antlr.generated.AlloyParser.AssignmentContext;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.helper.*;
import ca.uwaterloo.watform.alloyast.expr.join.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
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
	@Override
	public AlloyExpr visitAndExpr(AlloyParser.AndExprContext ctx) {
		System.out.println("Visiting AndExprContext");
		this.visit(ctx.expr2(0));
		this.visit(ctx.expr2(1));
		return new AlloyAndExpr();
	}

	@Override
	public AlloyExpr visitAndBindExpr(AlloyParser.AndBindExprContext ctx) {
		System.out.println("Visiting AndBindExprContext");
		this.visit(ctx.expr2());
		this.visit(ctx.bind());
		return new AlloyAndExpr();
	}

	@Override
	public AlloyExpr visitDotBuiltinExpr(AlloyParser.DotBuiltinExprContext ctx) {
		System.out.println("Visiting DotBuiltinExprContext");
		this.visit(ctx.expr2());
		if (null != ctx.DOT()) {
			this.visit(ctx.DOT());
		} else if (null != ctx.DISJ()) {
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
		return new AlloyDotJoinExpr();
	}

	@Override
	public AlloyExpr visitDotBindExpr(AlloyParser.DotBindExprContext ctx) {
		System.out.println("Visiting DotBindExprContext");
		this.visit(ctx.expr2());
		this.visit(ctx.bind());
		return new AlloyDotJoinExpr();
	}

	@Override
	public AlloyExpr visitDotExpr(AlloyParser.DotExprContext ctx) {
		System.out.println("Visiting DotJoinContext");
		this.visit(ctx.expr2(0));
		this.visit(ctx.expr2(1));
		return new AlloyDotJoinExpr();
	}

	@Override
	public AlloyExpr visitNumericExpr(AlloyParser.NumericExprContext ctx) {
		System.out.println("Visiting NumericExprContext");
		if (null != ctx.CARDINALITY()) {
			this.visit(ctx.expr2());
			return new AlloyCardinalityExpr();
		} else if (null != ctx.SUM()) {
			return new AlloyCardinalityExpr();
		} else if (null != ctx.INT()) {
			return new AlloyCardinalityExpr();
		} else {
			throw new UnexpectedTokenException(ctx);
		}
	}

	@Override
	public AlloyExpr visitPrimeExpr(AlloyParser.PrimeExprContext ctx) {
		System.out.println("Visiting PrimeExprContext");
		this.visit(ctx.expr2());
		return new AlloyPrimeExpr();
	}

	@Override 
	public AlloyExpr visitLet(AlloyParser.LetContext ctx) {
		System.out.println("Visiting LetContext");

		AlloyAsnExprHelperParsVis asnExprHelperParsVis = new AlloyAsnExprHelperParsVis();

		for (AssignmentContext asn : ctx.assignment()) {
			asnExprHelperParsVis.visit(asn);
		}

		this.visit(ctx.body());

		return new AlloyLetExpr();
	}

	// ============================
	// Block
	// ============================
	@Override
	public AlloyExpr visitBlock(AlloyParser.BlockContext ctx) {
		System.out.println("Visiting BlockContext");
		for (AlloyParser.Expr1Context exprCtx : ctx.expr1()) {
			this.visit(exprCtx);
		}
		return new AlloyBlock();
	}

	// ============================
	// SigRef
	// ============================
	@Override 
	public AlloyExpr visitSigRef(AlloyParser.SigRefContext ctx) {
		System.out.println("Visiting SigRefContext");
		if(null != ctx.qname()) {
			return this.visit(ctx.qname());
		} else if(null != ctx.UNIV()) {
			return new AlloyUnivExpr();
		} else if (null != ctx.STRING()) {
			return new AlloyStringExpr();
		} else if (null != ctx.STEPS()) {
			return new AlloyStepsExpr();
		} else if (null != ctx.SIGINT()) {
			return new AlloySigIntExpr();
		} else if (null != ctx.SEQ_INT()) {
			return new AlloySeqIntExpr();
		} else if (null != ctx.NONE()) {
			return new AlloyNoneExpr();
		} else {
			throw new UnexpectedTokenException(ctx);
		}
	}

	@Override
	public AlloyExpr visitSimpleQname(AlloyParser.SimpleQnameContext ctx) {
		System.out.println("Visiting SimpleQnameContext");
		return new AlloyQnameExpr();
	}

	@Override
	public AlloyExpr visitQualifiedQname(AlloyParser.QualifiedQnameContext ctx) {
		System.out.println("Visiting QualifiedQnameContext");
		return new AlloyQnameExpr();
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
}
