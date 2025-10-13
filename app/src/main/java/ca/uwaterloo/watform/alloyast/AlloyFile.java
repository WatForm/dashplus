package ca.uwaterloo.watform.alloyast;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ca.uwaterloo.watform.alloyast.paragraph.*;

public final class AlloyFile extends AlloyASTNode {
	public String filename = "";
	public final List<AlloyParagraph> paragraphs;

	public AlloyFile(Pos pos, List<AlloyParagraph> paragraphs) {
		super(pos);
		this.paragraphs = Collections.unmodifiableList(paragraphs);
	}

	@Override
	public String toString() {
		return paragraphs.stream().map(AlloyParagraph::toString).collect(Collectors.joining("\n"));
	}
}

