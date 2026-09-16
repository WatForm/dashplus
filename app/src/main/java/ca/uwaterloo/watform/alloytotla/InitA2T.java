package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlamodel.*;
import java.util.ArrayList;
import java.util.List;

// TODO rewrite without CmdPara as first-class object to refer to commands

public class InitA2T extends FieldsA2T {

  public InitA2T(AlloyModel alloyModel, Optimization optimization, boolean verbose, boolean debug) {
    super(alloyModel, optimization, verbose, debug);
  }

  protected void addInitDefn(TlaModel tlaModel) {

    tlaModel.addComment("Init", verbose);

    List<TlaExp> exps = new ArrayList<>();

    exps.add(TlaAppl(SCOPE));
    // exps.add(TlaAppl(SIG_SETS_UNPRIMED));
    // exps.add(TlaAppl(FIELD_TYPES));
    // exps.add(TlaAppl(ALL_SIG_CONSTRAINTS));
    // exps.add(TlaAppl(ALL_FACTS));

    tlaModel.addDefn(TlaDefn(INIT, repeatedAnd(exps)));
  }
}
