package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.TranslationStrings.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaDecl;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaAnd;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class NextDefinition {

    public static void translate(DashModel dashModel, TlaModel tlaModel) {
        // Next = small_step /\ typeOK
        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(NEXT),
                        new TlaAnd(new TlaAppl(SMALL_STEP), new TlaAppl(TYPE_OK))));
    }
}
