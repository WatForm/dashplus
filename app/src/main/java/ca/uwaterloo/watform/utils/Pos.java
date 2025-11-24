package ca.uwaterloo.watform.utils;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public final class Pos {
    public final int rowStart; // 1 indexed
    public final int colStart; // 0 indexed
    public final int rowEnd; // 1 indexed
    public final int colEnd; // 0 indexed
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

    public Pos(int rowStart, int colStart, int rowEnd, int colEnd) {
        this.rowStart = rowStart;
        this.colStart = colStart;
        this.rowEnd = rowEnd;
        this.colEnd = colEnd;
    }

    public Pos(edu.mit.csail.sdg.alloy4.Pos alloyJarPos) {
        this.rowStart = alloyJarPos.y;
        this.rowEnd = alloyJarPos.y2;
        this.colStart = alloyJarPos.x - 1; // change to 0 indexed
        this.colEnd = alloyJarPos.x2 - 1; // change to 0 indexed
    }

    @Override
    public String toString() {
        return "Pos: \n  rowStart: "
                + this.rowStart
                + "\n  colStart: "
                + this.colStart
                + "\n  rowEnd: "
                + this.rowEnd
                + "\n  colEnd: "
                + this.colEnd
                + "\n";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Pos otherPos)) {
            return false;
        }
        return this.rowStart == otherPos.rowStart
                && this.colStart == otherPos.colStart
                && this.rowEnd == otherPos.rowEnd
                && this.colEnd == otherPos.colEnd;
    }
}
