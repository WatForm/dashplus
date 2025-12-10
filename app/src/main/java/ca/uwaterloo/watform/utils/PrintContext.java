package ca.uwaterloo.watform.utils;

import de.uka.ilkd.pp.Layouter;
import de.uka.ilkd.pp.WriterBackend;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;

public final class PrintContext {
    private final Layouter<IOException> layouter;
    public static final int lineWidth = 80;
    // this is used to guarantee a size that cannot fit within a line
    public static final int largeSize = PrintContext.lineWidth * 2;
    public static final int indentSize = 4;

    public PrintContext(Writer w, int lineWidth, int indentSize) {
        this.layouter = new Layouter<>(new WriterBackend(w, lineWidth), indentSize);
    }

    public PrintContext(Writer w) {
        this(w, PrintContext.lineWidth, PrintContext.indentSize);
    }

    public void append(String s) {
        try {
            layouter.print(s);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void begin() {
        begin(PrintContext.indentSize);
    }

    public void begin(int indent) {
        layouter.begin(
                Layouter.BreakConsistency.CONSISTENT, Layouter.IndentationBase.FROM_POS, indent);
    }

    public void align() {
        this.begin(0);
    }

    public void end() {
        try {
            layouter.end();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * @param width space to insert if not broken
     * @param offset offset relative to current indentation level
     */
    public void brk(int width, int offset) {
        try {
            layouter.brk(width, offset);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void brk() {
        this.brk(1, 0);
    }

    public void brkNoSpace() {
        this.brk(0, 0);
    }

    public void brkNoIndent() {
        this.brk(1, -PrintContext.indentSize);
    }

    public void nl() {
        try {
            layouter.nl();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void nlNoIndent() {
        this.brk(PrintContext.largeSize, -PrintContext.indentSize);
    }

    public void blankLine() {
        this.nl();
        this.brkNoIndent();
    }

    public void flush() {
        try {
            layouter.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
