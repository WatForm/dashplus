package ca.uwaterloo.watform.alloyast.paragraph.fact;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyFactPara extends AlloyParagraph {
	public final String factName;
	public final AlloyBlock block;

	public AlloyFactPara(Pos pos, String factName, AlloyBlock block) {
		super(pos);
		this.factName = factName;
		this.block = block;
	}

	@Override
	public void toString(StringBuilder sb, int indent) {
		sb.append("fact " + factName + ("" != factName ? (" ") : ""));
		this.block.toString(sb, indent);
	}
}
