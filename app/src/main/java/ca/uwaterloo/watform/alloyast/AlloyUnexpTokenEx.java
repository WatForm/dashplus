package ca.uwaterloo.watform.alloyast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public final class AlloyUnexpTokenEx extends IllegalStateException {
	public AlloyUnexpTokenEx(ParserRuleContext ctx) {
		super(buildMessage(ctx));
	}

	public AlloyUnexpTokenEx(TerminalNode node) {
		super(buildMessage(node));
	}

	private static String buildMessage(ParserRuleContext ctx) {
		return "Unexpected token at line "
				+ ctx.getStart().getLine()
				+ ", position "
				+ ctx.getStart().getCharPositionInLine()
				+ ": "
				+ ctx.getText();
	}

	private static String buildMessage(TerminalNode node) {
		return "Unexpected token at line "
				+ node.getSymbol().getLine()
				+ ", position "
				+ node.getSymbol().getCharPositionInLine()
				+ ": "
				+ node.getText();
	}
}
