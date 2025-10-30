package ca.uwaterloo.watform.alloyast;

import antlr.generated.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

public final class AlloyFileParseVis extends DashBaseVisitor<AlloyFile> {
    @Override
    public AlloyFile visitAlloyFile(DashParser.AlloyFileContext ctx) {
        AlloyParagraphParseVis ppv = new AlloyParagraphParseVis();
        List<AlloyParagraph> paragraphs = new ArrayList<>();
        for (DashParser.ParagraphContext parCtx : ctx.paragraph()) {
            try {
                paragraphs.add(ppv.visit(parCtx));
            } catch (Reporter.ErrorUser eu) {
                Reporter.INSTANCE.addError(eu);
            }
        }
        if (paragraphs.isEmpty()) {
            return new AlloyFile(paragraphs);
        } else {
            return new AlloyFile(new Pos(ctx), paragraphs);
        }
    }
}
