package ca.uwaterloo.watform.alloyast.expr.helper;

import org.antlr.v4.runtime.tree.TerminalNode;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import antlr.generated.AlloyParser.ExprContext;
import antlr.generated.AlloyParser.NameContext;

import ca.uwaterloo.watform.alloyast.expr.*;

public final class AlloyAsnExprHelperParsVis extends AlloyBaseVisitor<AlloyAsnExprHelper> {
	@Override 
	public AlloyAsnExprHelper visitAssignment(AlloyParser.AssignmentContext ctx) {
		System.out.println("Visiting AssignmentContext");
		NameContext nc = ctx.name(); // canoot visit deeper
		TerminalNode equal = ctx.EQUAL();
		new AlloyExprParsVis().visit(ctx.expr());

		return new AlloyAsnExprHelper();
	}
}
