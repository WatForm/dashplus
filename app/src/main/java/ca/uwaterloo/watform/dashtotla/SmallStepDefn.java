package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaDecl;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaAdd;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaAnd;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaEquals;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaOr;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaIntLiteral;
import ca.uwaterloo.watform.tlaast.tlaplusnaryops.TlaUnchanged;
import ca.uwaterloo.watform.tlaast.tlaunops.TlaNot;
import ca.uwaterloo.watform.tlaast.tlaunops.TlaPrime;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.Arrays;
import java.util.List;

public class SmallStepDefn {
    public static void translate(DashModel dashModel, TlaModel tlaModel) {

        List<String> transitions = AuxDashAccessors.getTransitionNames(dashModel);

        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(SOME_TRANSITION),
                        repeatedOr(mapBy(transitions, t -> new TlaAppl(tlaFQN(t))))));

        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(SOME_PRE_TRANSITION),
                        repeatedOr(mapBy(transitions, t -> new TlaAppl(preTransTlaFQN(t))))));

        addStutter(tlaModel, dashModel);

        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(SMALL_STEP),
                        new TlaOr(
                                new TlaDecl(SOME_TRANSITION),
                                new TlaAnd(
                                        new TlaDecl(STUTTER),
                                        new TlaNot(new TlaDecl(SOME_PRE_TRANSITION))))));
    }

    public static void addStutter(TlaModel tlaModel, DashModel dashModel) {
        // ct' = ct + 1
        TlaExp ct_exp =
                new TlaEquals(
                        new TlaPrime(new TlaVar(CT)),
                        new TlaAdd(new TlaVar(CT), new TlaIntLiteral(1)));

        // UNCHANGED <<_conf,_stable,_scope_used>>
        TlaExp unchanged_exp =
                new TlaUnchanged(
                        Arrays.asList(
                                new TlaVar(CONF), new TlaVar(STABLE), new TlaVar(SCOPES_USED)));

        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(STUTTER), repeatedAnd(Arrays.asList(ct_exp, unchanged_exp))));
    }
}
