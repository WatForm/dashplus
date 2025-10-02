package org.dashToAlloy.alloyast.expr;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import org.dashToAlloy.alloyast.expr.*;

public final class BlockParserVisitor extends AlloyBaseVisitor<Block> {
	@Override
	public Block visitBlock(AlloyParser.BlockContext ctx) {
		System.out.println("Visiting Block");
		ExprParserVisitor exprPV = new ExprParserVisitor();
		for(AlloyParser.ExprContext exprCtx : ctx.expr()) {
			exprPV.visit(exprCtx);
		}
		return new Block();
	}
}

