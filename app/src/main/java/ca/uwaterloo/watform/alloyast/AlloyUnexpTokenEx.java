package ca.uwaterloo.watform.alloyast;

import org.antlr.v4.runtime.ParserRuleContext;

public final class AlloyUnexpTokenEx extends IllegalStateException {
	public AlloyUnexpTokenEx(ParserRuleContext ctx) {
		super(buildMessage(ctx));
	}

	private static String buildMessage(ParserRuleContext ctx) {
		return "Unexpected token at line"
				+ ctx.getStart().getLine()
				+ ", position "
				+ ctx.getStart().getCharPositionInLine()
				+ ": "
				+ ctx.getText();
	}
}
