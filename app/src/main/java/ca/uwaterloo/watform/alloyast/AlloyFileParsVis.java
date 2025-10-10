package ca.uwaterloo.watform.alloyast;

import java.nio.file.Path;

import org.antlr.v4.runtime.Token;

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
		Token st = ctx.getStart();
		return new AlloyFile(new Pos(ctx));
	}
}

