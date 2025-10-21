package ca.uwaterloo.watform.utils;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(sb, 0);
        return sb.toString();
    }

    public String toString(int indent) {
        StringBuilder sb = new StringBuilder();
        toString(sb, indent);
        return sb.toString();
    }

    public abstract void toString(StringBuilder sb, int indent);

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
                throw CodingError.failedCast(
                        "Cannot cast to ASTNode in ASTNode.join. \n" + e.toString());
            }
            if (iterator.hasNext()) {
                sb.append(separator);
            }
        }
    }
}
