package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.TranslationStrings.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaDecl;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaAnd;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaOr;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaFalse;
import ca.uwaterloo.watform.tlaast.tlaunops.TlaNot;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.List;

public class SmallStepDefinition {
    public static void translate(DashModel dashModel, TlaModel tlaModel) {

        List<String> transitions = AuxiliaryDashAccessors.getTransitionNames(dashModel);

        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(SOME_TRANSITION),
                        repeatedOr(GeneralUtil.mapBy(transitions, t -> new TlaAppl(tlaFQN(t))))));

        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(SOME_PRE_TRANSITION),
                        repeatedOr(
                                GeneralUtil.mapBy(
                                        transitions, t -> new TlaAppl(PreTransFormulaName(t))))));

        tlaModel.addDefn(new TlaDefn(new TlaDecl(STUTTER), new TlaFalse()));

        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(SMALL_STEP),
                        new TlaOr(
                                new TlaDecl(SOME_TRANSITION),
                                new TlaAnd(
                                        new TlaDecl(STUTTER),
                                        new TlaNot(new TlaDecl(SOME_PRE_TRANSITION))))));
    }
}
