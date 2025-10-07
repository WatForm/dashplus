package ca.uwaterloo.watform.alloyast;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyParagraphParsVis;

public final class AlloyFileParsVis extends AlloyBaseVisitor<AlloyFile> {
	@Override
	public AlloyFile visitAlloyFile(AlloyParser.AlloyFileContext ctx) {
		System.out.println("Visiting AlloyFileContext");
		AlloyParagraphParsVis ppv = new AlloyParagraphParsVis();
		for (AlloyParser.ParagraphContext parCtx : ctx.paragraph()) {
			ppv.visit(parCtx);
		}
		return new AlloyFile();
	}
}

