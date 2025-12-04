package ca.uwaterloo.watform.dashtotlaplus;

import static ca.uwaterloo.watform.dashtotlaplus.TranslationStrings.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaAppl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDefn;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaInSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaSubsetEq;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TlaBoolean;
import ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators.TlaSet;
import ca.uwaterloo.watform.tlaplusmodel.TlaModel;
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

        TlaExp conf_exp = new TlaSubsetEq(CONF.globalVar(), CONF.typeAppl());

        TlaExp trans_taken_exp = new TlaInSet(TRANS_TAKEN.globalVar(), TRANS_TAKEN.typeAppl());

        TlaExp scope_exp = new TlaSubsetEq(SCOPE_USED.globalVar(), CONF.typeAppl());

        TlaExp stable_exp = new TlaInSet(STABLE.globalVar(), new TlaBoolean());

        tlaModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        TYPE_OK.decl(),
                        repeatedAnd(
                                Arrays.asList(conf_exp, trans_taken_exp, stable_exp, scope_exp))));
    }

    public static void typeConf(DashModel dashModel, TlaModel tlaModel) {

        // _all_conf = <union of all leaf state formulae>

        List<String> leafStateFQNs = AuxiliaryDashAccessors.getLeafStateNames(dashModel);

        tlaModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        CONF.typeDecl(),
                        repeatedUnion(
                                GeneralUtil.mapBy(
                                        leafStateFQNs,
                                        x -> new TlaFormulaAppl(getStateFormulaName(x))))));
    }

    public static void typeTransTaken(DashModel dashModel, TlaModel tlaModel) {
        tlaModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        TRANS_TAKEN.typeDecl(),
                        new TlaSet(
                                GeneralUtil.mapBy(
                                        AuxiliaryDashAccessors.getTransitionNames(dashModel),
                                        s ->
                                                new TlaFormulaAppl(
                                                        TranslationStrings.getTakenTransFormulaName(
                                                                s))))));
    }

    public static void typeScopesUsed(DashModel dashModel, TlaModel tlaModel) {

        // _all_scopes_used == _all_conf
        tlaModel.addFormulaDefinition(new TlaFormulaDefn(SCOPE_USED.typeDecl(), CONF.typeAppl()));
    }
}
