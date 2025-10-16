package ca.uwaterloo.watform.alloyast;
import ca.uwaterloo.watform.utils.*;

public abstract class AlloyASTNode {
	public Pos pos = Pos.UNKNOWN;

	public AlloyASTNode(Pos pos) {
		this.pos = pos;
	}

	public final Pos getPos() {
		return this.pos;
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		toString(sb, 0);
		return sb.toString();
	}

	public abstract void toString(StringBuilder sb, int indent);
}
