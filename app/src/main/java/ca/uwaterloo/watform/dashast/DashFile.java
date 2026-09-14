package ca.uwaterloo.watform.dashast;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.Pos;
import ca.uwaterloo.watform.utils.PrintContext;
import java.util.List;

public final class DashFile extends AlloyFile {
  public final DashState stateRoot;
  public String dashFullFileName = "";

  public DashFile(Pos pos, List<AlloyPara> paragraphs, String dashFullFileName) {
    super(
        pos, filterBy(paragraphs, alloyPara -> !(alloyPara instanceof DashPara)), dashFullFileName);
    List<DashPara> dashParas = extractItemsOfClass(paragraphs, DashPara.class);
    if (1 != dashParas.size()) {
      System.out.println(dashParas);
      throw DashASTError.exactlyOneStateRoot();
    }
    this.stateRoot = (DashState) dashParas.get(0);
    this.dashFullFileName = dashFullFileName;
  }

  public DashFile(List<AlloyPara> paragraphs, String dashFullFileName) {
    this(Pos.UNKNOWN, paragraphs, dashFullFileName);
  }

  public List<AlloyPara> getAlloyParas() {
    return super.paras;
  }

  @Override
  public void pp(PrintContext pCtx) {
    super.pp(pCtx);
    stateRoot.pp(pCtx);
  }
}
