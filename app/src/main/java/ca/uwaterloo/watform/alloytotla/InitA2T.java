package ca.uwaterloo.watform.alloytotla;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class InitA2T extends FieldsA2T {
	protected void addInitDefn(TlaModel tlaModel, AlloyCmdPara.CommandDecl cmdDecl) {

    tlaModel.addComment("Init", verbose);

    List<TlaExp> exps = new ArrayList<>();

    exps.add(TlaAppl(SCOPE));
    exps.add(TlaAppl(SIG_SETS_UNPRIMED));
    exps.add(TlaAppl(FIELD_TYPES));
    exps.add(TlaAppl(ALL_SIG_CONSTRAINTS));
    exps.add(TlaAppl(ALL_FACTS));

    tlaModel.addDefn(TlaDefn(INIT, repeatedAnd(exps)));
  }
}
