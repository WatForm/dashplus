package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.TranslationStrings.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaDecl;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaInSet;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaSubsetEq;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaBoolean;
import ca.uwaterloo.watform.tlaast.tlaplusnaryops.TlaSet;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.Arrays;
import java.util.List;

public class TypeOKDefinitions {

    public static void translate(DashModel dashModel, TlaModel tlaModel) {
        typeConf(dashModel, tlaModel);
        typeTransTaken(dashModel, tlaModel);
        typeScopesUsed(dashModel, tlaModel);
        addTypeOKFormula(tlaModel);
    }

    public static void addTypeOKFormula(TlaModel tlaModel) {

        TlaExp conf_exp = new TlaSubsetEq(new TlaVar(CONF), new TlaAppl(parameterVariable(CONF)));

        TlaExp trans_taken_exp =
                new TlaInSet(new TlaVar(TRANS_TAKEN), new TlaAppl(parameterVariable(TRANS_TAKEN)));

        TlaExp scope_exp =
                new TlaSubsetEq(new TlaVar(SCOPE_USED), new TlaAppl(parameterVariable(SCOPE_USED)));

        TlaExp stable_exp = new TlaInSet(new TlaVar(STABLE), new TlaBoolean());

        tlaModel.addFormulaDefinition(
                new TlaDefn(
                        new TlaDecl(TYPE_OK),
                        repeatedAnd(
                                Arrays.asList(conf_exp, trans_taken_exp, stable_exp, scope_exp))));
    }

    public static void typeConf(DashModel dashModel, TlaModel tlaModel) {

        // _all_conf = <union of all leaf state formulae>

        List<String> leafStateFQNs = AuxiliaryDashAccessors.getLeafStateNames(dashModel);

        tlaModel.addFormulaDefinition(
                new TlaDefn(
                        new TlaDecl(CONF),
                        repeatedUnion(
                                GeneralUtil.mapBy(
                                        leafStateFQNs, x -> new TlaAppl(getStateFormulaName(x))))));
    }

    public static void typeTransTaken(DashModel dashModel, TlaModel tlaModel) {
        tlaModel.addFormulaDefinition(
                new TlaDefn(
                        new TlaDecl(TRANS_TAKEN),
                        new TlaSet(
                                GeneralUtil.mapBy(
                                        AuxiliaryDashAccessors.getTransitionNames(dashModel),
                                        s ->
                                                new TlaAppl(
                                                        TranslationStrings.getTakenTransFormulaName(
                                                                s))))));
    }

    public static void typeScopesUsed(DashModel dashModel, TlaModel tlaModel) {

        // _all_scopes_used == _all_conf
        tlaModel.addFormulaDefinition(
                new TlaDefn(new TlaDecl(typeFormula(SCOPE_USED)), new TlaDecl(typeFormula(CONF))));
    }
}
