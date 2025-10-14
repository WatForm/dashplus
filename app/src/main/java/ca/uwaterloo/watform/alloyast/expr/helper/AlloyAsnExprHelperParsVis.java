package ca.uwaterloo.watform.alloyast.expr.helper;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import antlr.generated.AlloyParser.NameContext;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;

import org.antlr.v4.runtime.tree.TerminalNode;

public final class AlloyAsnExprHelperParsVis extends AlloyBaseVisitor<AlloyAsnExprHelper> {
	@Override
	public AlloyAsnExprHelper visitAssignment(AlloyParser.AssignmentContext ctx) {
		AlloyExprParsVis exprParsVis = new AlloyExprParsVis();

		return new AlloyAsnExprHelper(new Pos(ctx), 
			(AlloyNameExpr) exprParsVis.visit(ctx.name()),
			exprParsVis.visit(ctx.expr1()));
	}
}
