package ca.uwaterloo.watform.dashast;

import antlr.generated.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

public final class DashFileParseVis extends DashBaseVisitor<DashFile> {
    @Override
    public DashFile visitDashFile(DashParser.DashFileContext ctx) {
        DashParagraphParseVis ppv = new DashParagraphParseVis();
        List<AlloyParagraph> paragraphs = new ArrayList<>();
        for (DashParser.ParagraphContext parCtx : ctx.paragraph()) {
            try {
                paragraphs.add(ppv.visit(parCtx));
            } catch (Reporter.ErrorUser eu) {
                Reporter.INSTANCE.addError(eu);
            }
        }
        if (paragraphs.isEmpty()) {
            return new DashFile(paragraphs);
        } else {
            return new DashFile(new Pos(ctx), paragraphs);
        }
    }
}
