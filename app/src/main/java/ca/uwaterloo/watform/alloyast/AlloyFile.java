package ca.uwaterloo.watform.alloyast;

import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.AlloyStrings;

import java.util.Collections;
import java.util.List;

public final class AlloyFile extends AlloyASTNode {
	public String filename = "";
	public final List<AlloyParagraph> paragraphs;

	public AlloyFile(Pos pos, List<AlloyParagraph> paragraphs) {
		super(pos);
		this.paragraphs = Collections.unmodifiableList(paragraphs);
	}

	@Override
	public void toString(StringBuilder sb, int indent) {
		for (AlloyParagraph p : paragraphs) {
			p.toString(sb, indent);
			sb.append(AlloyStrings.NEWLINE + AlloyStrings.NEWLINE);
		}
	}
}
