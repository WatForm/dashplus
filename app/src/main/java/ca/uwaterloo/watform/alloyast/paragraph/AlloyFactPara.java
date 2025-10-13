package ca.uwaterloo.watform.alloyast.paragraph;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;

public final class AlloyFactPara extends AlloyParagraph {
	public final String factName;
	public final AlloyBlock block;

	public AlloyFactPara(Pos pos, String factName, AlloyBlock block) {
		super(pos);
		this.factName = factName;
		this.block = block;
	}

	@Override
	public String toString() {
		return "fact " + factName + " " + this.block.toString();
	}
}
