package ca.uwaterloo.watform.utils;

import java.io.StringWriter;
import java.util.List;

public abstract class ASTNode {
    public Pos pos = Pos.UNKNOWN;

    public ASTNode(Pos pos) {
        this.pos = pos;
    }

    public ASTNode() {}

    public final Pos getPos() {
        return this.pos;
    }

    public abstract void pp(PrintContext pCtx);

    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        PrintContext pCtx = new PrintContext(sw);
        this.ppNewBlock(pCtx);
        pCtx.flush();
        return sw.toString();
    }

    public String toString(int lineWidth, int indentSize) {
        StringWriter sw = new StringWriter();
        PrintContext pCtx = new PrintContext(sw, lineWidth, indentSize);
        this.ppNewBlock(pCtx);
        pCtx.flush();
        return sw.toString();
    }

    public void ppNewBlock(PrintContext pCtx) {
        pCtx.begin();
        this.pp(pCtx);
        pCtx.end();
    }

    public void toString(StringBuilder sb, int indent) {}

    // There's a similar method in GeneralUtil.java, but ASTNodes need to take
    // in an indent param
    public static <T> void join(StringBuilder sb, int indent, List<T> items, String separator) {
        if (items == null || items.isEmpty()) {
            return;
        }
        var iterator = items.iterator();
        while (iterator.hasNext()) {
            try {
                ((ASTNode) iterator.next()).toString(sb, indent);
            } catch (ClassCastException e) {
                throw ImplementationError.failedCast(
                        "Cannot cast to ASTNode in ASTNode.join. \n" + e.toString());
            }
            if (iterator.hasNext()) {
                sb.append(separator);
            }
        }
    }
}
