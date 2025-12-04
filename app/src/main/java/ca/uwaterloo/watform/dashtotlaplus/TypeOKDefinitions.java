package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaAppl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDefn;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaInSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaSubsetEq;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaUnionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TlaBoolean;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TlaLiteral;
import ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators.TlaSet;
import ca.uwaterloo.watform.tlaplusmodel.TlaModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.Arrays;
import java.util.List;

public class TypeOKDefinitions {

    public static void translate(DashModel dashModel, TlaModel tlaModel) {
        typeStates(dashModel, tlaModel);
        typeTransitionTaken(dashModel, tlaModel);
        typeScopesUsed(dashModel, tlaModel);
        addTypeOKFormula(tlaModel);
    }

    private static TlaUnionSet unionNone(TlaExp e) {
        return new TlaUnionSet(
                e, new TlaSet(Arrays.asList(new TlaLiteral(TranslationStrings.NONE))));
    }

    public static void addTypeOKFormula(TlaModel tlaModel) {

        TlaExp conf_exp =
                new TlaSubsetEq(
                        TranslationStrings.CONF.globalVar(), TranslationStrings.CONF.typeAppl());

        TlaExp trans_taken_exp =
                new TlaInSet(
                        TranslationStrings.TRANS_TAKEN.globalVar(),
                        TranslationStrings.TRANS_TAKEN.typeAppl());

        TlaExp scope_exp =
                new TlaSubsetEq(
                        TranslationStrings.SCOPE_USED.globalVar(),
                        TranslationStrings.CONF.typeAppl());

        TlaExp stable_exp = new TlaInSet(TranslationStrings.STABLE.globalVar(), new TlaBoolean());

        tlaModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        TranslationStrings.TYPE_OK.decl(),
                        TranslationStrings.repeatedAnd(
                                Arrays.asList(conf_exp, trans_taken_exp, stable_exp, scope_exp))));
    }

    public static void typeStates(DashModel dashModel, TlaModel tlaModel) {
        // this function adds in a formula used for typeOK:
        // _all_conf = union of all leaf state formulae

        List<TlaFormulaAppl> leafStateFormulaApplications =
                GeneralUtil.mapBy(
                        AuxiliaryDashAccessors.getLeafStateNames(dashModel),
                        name -> new TlaFormulaAppl(TranslationStrings.getStateFormulaName(name)));

        tlaModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        TranslationStrings.CONF.typeDecl(),
                        TranslationStrings.repeatedUnion(leafStateFormulaApplications)));
    }

    public static void typeTransitionTaken(DashModel dashModel, TlaModel tlaModel) {
        tlaModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        TranslationStrings.TRANS_TAKEN.typeDecl(),
                        unionNone(
                                new TlaSet(
                                        GeneralUtil.mapBy(
                                                AuxiliaryDashAccessors.getTransitionNames(
                                                        dashModel),
                                                s ->
                                                        new TlaFormulaAppl(
                                                                TranslationStrings
                                                                        .getTakenTransFormulaName(
                                                                                s)))))));
    }

    public static void typeScopesUsed(DashModel dashModel, TlaModel tlaModel) {
        tlaModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        TranslationStrings.SCOPE_USED.typeDecl(),
                        unionNone(TranslationStrings.CONF.typeAppl())));
    }
}
