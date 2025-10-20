package ca.uwaterloo.watform.dashast;


import java.util.Collections;
import java.util.List;

import ca.uwaterloo.watform.utils.Pos;
import ca.uwaterloo.watform.utils.ASTNode;
import ca.uwaterloo.watform.utils.CommonStrings;
import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyParagraph;

public final class DashFile { // extends AlloyFile {
	public String filename = "";
	public final List<DashParagraph> paragraphs;

	public DashFile(List<DashParagraph> paragraphs) {
		//super((List<AlloyParagraph>)paragraphs);
		this.paragraphs = Collections.unmodifiableList(paragraphs);
	}

	// @Override
	public void toString(StringBuilder sb, int indent) {
		for (DashParagraph p : paragraphs) {
			p.toString(sb, indent);
			sb.append(CommonStrings.NEWLINE + CommonStrings.NEWLINE);
		}
	}
}
