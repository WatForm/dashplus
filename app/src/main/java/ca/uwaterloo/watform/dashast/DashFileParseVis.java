package ca.uwaterloo.watform.dashast;

import antlr.generated.*;
import ca.uwaterloo.watform.alloyast.AlloyCtorError;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class DashFileParseVis extends DashBaseVisitor<DashFile> {
    public final Path filePath;

    public DashFileParseVis(Path filePath) {
        super();
        this.filePath = filePath;
    }

    @Override
    public DashFile visitDashFile(DashParser.DashFileContext ctx) {
        DashParaParseVis ppv = new DashParaParseVis();
        List<AlloyPara> paragraphs = new ArrayList<>();
        for (DashParser.ParagraphContext parCtx : ctx.paragraph()) {
            try {
                paragraphs.add(ppv.visit(parCtx));
            } catch (AlloyCtorError alloyCtorError) {
                alloyCtorError.setFilePath(this.filePath);
                Reporter.INSTANCE.addError(alloyCtorError);
            }
        }
        if (paragraphs.isEmpty()) {
            return new DashFile(paragraphs);
        } else {
            return new DashFile(new Pos(ctx), paragraphs);
        }
    }
}
