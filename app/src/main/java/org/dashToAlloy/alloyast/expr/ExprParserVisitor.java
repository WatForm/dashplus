package org.dashToAlloy.alloyast.expr;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import org.dashToAlloy.alloyast.expr.*;
import org.dashToAlloy.alloyast.expr.binary.*;

public final class ExprParserVisitor extends AlloyBaseVisitor<Expr> {
	@Override
	public Expr visitAndFormula(AlloyParser.AndFormulaContext ctx) {
		System.out.println("Visiting And");
		this.visit(ctx.expr(0));
		this.visit(ctx.expr(1));
		return new AndExpr();
	}
}

