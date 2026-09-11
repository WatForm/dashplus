package ca.uwaterloo.watform.alloytotla;

import java.util.ArrayList;
import java.util.List;


import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlamodel.*;


import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

// TODO rewrite without CmdPara as first-class object to refer to commands

import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;

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
