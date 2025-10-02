package ca.uwaterloo.watform.alloyast.expr;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.join.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;

public final class ExprParserVisitor extends AlloyBaseVisitor<Expr> {
	@Override
	public Expr visitAndFormula(AlloyParser.AndFormulaContext ctx) {
		System.out.println("Visiting And");
		this.visit(ctx.expr(0));
		this.visit(ctx.expr(1));
		return new AndExpr();
	}

	@Override
	public Expr visitDotJoin(AlloyParser.DotJoinContext ctx) {
		System.out.println("Visiting DotJoin");
		this.visit(ctx.expr(0));
		this.visit(ctx.expr(1));
		return new DotJoinExpr();
	}

	@Override 
	public Expr visitCardinalityValue(AlloyParser.CardinalityValueContext ctx) {
		System.out.println("Visiting CardinalityValue");
		this.visit(ctx.expr());
		return new CardinalityExpr();
	}
}
