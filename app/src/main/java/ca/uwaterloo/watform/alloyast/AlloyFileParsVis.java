package ca.uwaterloo.watform.alloyast;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.paragraph.*;

public final class AlloyFileParsVis extends AlloyBaseVisitor<AlloyFile> {

	@Override
	public AlloyFile visitAlloyFile(AlloyParser.AlloyFileContext ctx) {
		AlloyParagraphParsVis ppv = new AlloyParagraphParsVis();
		List<AlloyParagraph> paragraphs = new ArrayList<>();
		for (AlloyParser.ParagraphContext parCtx : ctx.paragraph()) {
			paragraphs.add(ppv.visit(parCtx));
		}
		Token st = ctx.getStart();
		return new AlloyFile(new Pos(ctx), paragraphs);
	}
}

