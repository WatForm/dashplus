package ca.uwaterloo.watform.alloyast;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public final class Pos {
	public final int rowStart; // 1 indexed
	public final int colStart; // 0 indexed
	public final int rowEnd;   // 1 indexed
	public final int colEnd;   // 0 indexed
	public static final Pos UNKNOWN = new Pos();

	private Pos() {
		this.rowStart = 1;
		this.colStart = 0;
		this.rowEnd = 1;
		this.colEnd = 0;
	}

	public Pos(ParserRuleContext ctx) {
		Token start = ctx.getStart();
		Token stop = ctx.getStop();

		this.rowStart = start.getLine();
		this.colStart = start.getCharPositionInLine();

		// Approximate end column as start of stop + length of token text
		this.rowEnd = stop.getLine();
		this.colEnd = stop.getCharPositionInLine() + stop.getText().length();
	}

	public Pos(TerminalNode tn) {
		Token token = tn.getSymbol();

		this.rowStart = token.getLine();
		this.colStart = token.getCharPositionInLine();

		this.rowEnd = this.rowStart; // single token usually on one line
		this.colEnd = this.colStart + token.getText().length();
	}
}
