package ca.uwaterloo.watform.alloyast.expr.helper;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.utils.*;
import antlr.generated.AlloyParser.NameContext;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;

import org.antlr.v4.runtime.tree.TerminalNode;

public final class AlloyAsnExprHelperParseVis extends AlloyBaseVisitor<AlloyAsnExprHelper> {
	@Override
	public AlloyAsnExprHelper visitAssignment(AlloyParser.AssignmentContext ctx) {
		AlloyExprParseVis exprParsVis = new AlloyExprParseVis();

		return new AlloyAsnExprHelper(new Pos(ctx), 
			(AlloyNameExpr) exprParsVis.visit(ctx.name()),
			exprParsVis.visit(ctx.expr1()));
	}
}
