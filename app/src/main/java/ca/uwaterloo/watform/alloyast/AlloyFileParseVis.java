package ca.uwaterloo.watform.alloyast;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

public final class AlloyFileParseVis extends AlloyBaseVisitor<AlloyFile> {
    @Override
    public AlloyFile visitAlloyFile(AlloyParser.AlloyFileContext ctx) {
        AlloyParagraphParseVis ppv = new AlloyParagraphParseVis();
        List<AlloyParagraph> paragraphs = new ArrayList<>();
        for (AlloyParser.ParagraphContext parCtx : ctx.paragraph()) {
            paragraphs.add(ppv.visit(parCtx));
        }
        if (paragraphs.isEmpty()) {
            return new AlloyFile(paragraphs);
        } else {
            return new AlloyFile(new Pos(ctx), paragraphs);
        }
    }
}
