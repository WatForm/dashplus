package ca.uwaterloo.watform.alloyast;

public abstract class AlloyASTNode {
	public Pos pos = Pos.UNKNOWN;

	public AlloyASTNode(Pos pos) {
		this.pos = pos;
	}

	public final Pos getPos() {
		return this.pos;
	}

	@Override
	public abstract String toString();
}
