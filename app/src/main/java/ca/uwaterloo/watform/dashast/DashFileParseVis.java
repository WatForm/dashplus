package ca.uwaterloo.watform.dashast;

import antlr.generated.*;
import antlr.generated.DashParser;
import ca.uwaterloo.watform.alloyast.AlloyCtorError;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

public final class DashFileParseVis extends DashBaseVisitor<DashFile> {
  // this is needed for calculating import paths
  // so does not matter if .dsh or .als
  public final String fullFileName;

  public DashFileParseVis(String fullFileName) {
    super();
    this.fullFileName = fullFileName;
  }

  @Override
  public DashFile visitDashFile(DashParser.DashFileContext ctx) {
    DashParaParseVis ppv = new DashParaParseVis(this.fullFileName);
    List<AlloyPara> paragraphs = new ArrayList<>();
    for (DashParser.ParagraphContext parCtx : ctx.paragraph()) {
      try {
        paragraphs.add(ppv.visit(parCtx));
        // catch and continue
        // for other paragraphs
      } catch (AlloyCtorError alloyCtorError) {
        Reporter.INSTANCE.addError(alloyCtorError);
      } catch (DashASTError dashASTError) {
        Reporter.INSTANCE.addError(dashASTError);
      }
    }
    if (paragraphs.isEmpty()) {
      return new DashFile(paragraphs, this.fullFileName);
    } else {
      return new DashFile(new Pos(ctx), paragraphs, this.fullFileName);
    }
  }
}
