package ca.uwaterloo.watform.alloyast;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.paragraph.ParagraphParserVisitor;

public final class AlloyFileParserVisitor extends AlloyBaseVisitor<AlloyFile> {
	@Override
	public AlloyFile visitAlloyFile(AlloyParser.AlloyFileContext ctx) {
		System.out.println("Visiting AlloyFile");
		ParagraphParserVisitor ppv = new ParagraphParserVisitor();
		for (AlloyParser.ParagraphContext parCtx : ctx.paragraph()) {
			ppv.visit(parCtx);
		}
		return new AlloyFile();
	}
}

