package ca.uwaterloo.watform.alloyast;

import antlr.generated.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AlloyFileParseVis extends DashBaseVisitor<AlloyFile> {
    public final Path filePath;

    public AlloyFileParseVis(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public AlloyFile visitAlloyFile(DashParser.AlloyFileContext ctx) {
        AlloyParagraphParseVis ppv = new AlloyParagraphParseVis();
        List<AlloyParagraph> paragraphs = new ArrayList<>();
        for (DashParser.AlloyParagraphContext parCtx : ctx.alloyParagraph()) {
            try {
                paragraphs.add(ppv.visit(parCtx));
            } catch (Reporter.ErrorUser eu) {
                eu.setFilePath(this.filePath);
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
