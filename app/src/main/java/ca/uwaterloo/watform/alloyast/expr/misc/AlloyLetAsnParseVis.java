package ca.uwaterloo.watform.alloyast.expr.misc;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyLetExpr.AlloyLetAsn;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import ca.uwaterloo.watform.utils.*;

public final class AlloyLetAsnParseVis extends AlloyBaseVisitor<AlloyLetAsn> {
	@Override
	public AlloyLetAsn visitAssignment(AlloyParser.AssignmentContext ctx) {
		AlloyExprParseVis exprParsVis = new AlloyExprParseVis();

		return new AlloyLetAsn(
				new Pos(ctx),
				(AlloyNameExpr) exprParsVis.visit(ctx.name()),
				exprParsVis.visit(ctx.expr1()));
	}
}
