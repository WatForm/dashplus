package org.dashToAlloy.ast.expr;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;

public class BlockParserVisitor extends AlloyBaseVisitor<Block> {
	@Override
	public Block visitBlock(AlloyParser.BlockContext ctx) {
		System.out.println("Visiting Block");
		return new Block();
	}
}

