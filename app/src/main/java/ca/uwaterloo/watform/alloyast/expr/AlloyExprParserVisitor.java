package ca.uwaterloo.watform.alloyast.expr;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.join.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyPrimeExpr;

public final class AlloyExprParserVisitor extends AlloyBaseVisitor<AlloyExpr> {
	@Override
	public AlloyExpr visitAndFormula(AlloyParser.AndFormulaContext ctx) {
		System.out.println("Visiting AlloyAndExpr");
		this.visit(ctx.expr(0));
		this.visit(ctx.expr(1));
		return new AlloyAndExpr();
	}

	@Override
	public AlloyExpr visitDotJoin(AlloyParser.DotJoinContext ctx) {
		System.out.println("Visiting AlloyDotJoinExpr");
		this.visit(ctx.expr(0));
		this.visit(ctx.expr(1));
		return new AlloyDotJoinExpr();
	}

	@Override 
	public AlloyExpr visitCardinalityValue(AlloyParser.CardinalityValueContext ctx) {
		System.out.println("Visiting AlloyCardinalityExpr");
		this.visit(ctx.expr());
		return new AlloyCardinalityExpr();
	}


	@Override 
	public AlloyExpr visitPrimeValue(AlloyParser.PrimeValueContext ctx) {
		System.out.println("Visiting AlloyPrimeExpr");
		this.visit(ctx.expr());
		return new AlloyPrimeExpr();
	}
}
