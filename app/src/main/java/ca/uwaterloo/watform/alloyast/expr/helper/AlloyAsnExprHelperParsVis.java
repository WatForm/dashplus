package ca.uwaterloo.watform.alloyast.expr.helper;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import antlr.generated.AlloyParser.NameContext;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import org.antlr.v4.runtime.tree.TerminalNode;

public final class AlloyAsnExprHelperParsVis extends AlloyBaseVisitor<AlloyAsnExprHelper> {
	@Override
	public AlloyAsnExprHelper visitAssignment(AlloyParser.AssignmentContext ctx) {
		System.out.println("Visiting AssignmentContext");
		NameContext nc = ctx.name(); // canoot visit deeper
		TerminalNode equal = ctx.EQUAL();
		new AlloyExprParsVis().visit(ctx.expr1());

		return new AlloyAsnExprHelper(new Pos(ctx));
	}
}
