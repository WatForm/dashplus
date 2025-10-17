package ca.uwaterloo.watform.utils;

import java.util.List;

public abstract class ASTNode {
	public Pos pos = Pos.UNKNOWN;

	public ASTNode(Pos pos) {
		this.pos = pos;
	}

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

	public static <T extends ASTNode> void join(StringBuilder sb, int indent, List<T> items, String separator) {
		if (items == null || items.isEmpty()) {
			return;
		}
		var iterator = items.iterator();
		while (iterator.hasNext()) {
			iterator.next().toString(sb, indent);
			if (iterator.hasNext()) {
				sb.append(separator);
			}
		}
	}
}
