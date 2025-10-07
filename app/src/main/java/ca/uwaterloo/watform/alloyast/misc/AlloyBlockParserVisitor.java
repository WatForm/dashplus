package ca.uwaterloo.watform.alloyast.misc;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.expr.*;

public final class AlloyBlockParserVisitor extends AlloyBaseVisitor<AlloyBlock> {
	@Override
	public AlloyBlock visitBlock(AlloyParser.BlockContext ctx) {
		System.out.println("Visiting Block");
		AlloyExprParserVisitor exprPV = new AlloyExprParserVisitor();
		for(AlloyParser.ExprContext exprCtx : ctx.expr()) {
			exprPV.visit(exprCtx);
		}
		return new AlloyBlock();
	}
}

