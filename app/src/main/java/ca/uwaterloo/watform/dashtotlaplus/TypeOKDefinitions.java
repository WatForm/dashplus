package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaAppl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDefn;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaInSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaUnionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TlaBoolean;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TlaLiteral;
import ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators.TlaSet;
import ca.uwaterloo.watform.tlaplusmodel.TlaModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.Arrays;
import java.util.List;

public class TypeOKDefinitions {

    public static void AddTypeOKFormula(DashModel dashModel, TlaModel tlaPlusModel) {
        addSetStates(dashModel, tlaPlusModel);
        addSetTransitionTaken(dashModel, tlaPlusModel);
        addSetScopesUsed(dashModel, tlaPlusModel);
        addTypeOKFormula(tlaPlusModel);
    }

    private static TlaUnionSet unionNone(TlaExp e) {
        return new TlaUnionSet(
                e, new TlaSet(Arrays.asList(new TlaLiteral(TranslationStrings.NONE))));
    }

    public static void addTypeOKFormula(TlaModel tlaPlusModel) {

        TlaExp conf_exp =
                new TlaInSet(
                        TranslationStrings.CONF.globalVar(), TranslationStrings.CONF.typeAppl());

        TlaExp trans_taken_exp =
                new TlaInSet(
                        TranslationStrings.TRANS_TAKEN.globalVar(),
                        TranslationStrings.TRANS_TAKEN.typeAppl());

        TlaExp scope_exp =
                new TlaInSet(
                        TranslationStrings.SCOPE_USED.globalVar(),
                        TranslationStrings.CONF.typeAppl());

        TlaExp stable_exp = new TlaInSet(TranslationStrings.STABLE.globalVar(), new TlaBoolean());

        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        TranslationStrings.TYPE_OK.decl(),
                        TranslationStrings.repeatedAnd(
                                Arrays.asList(conf_exp, trans_taken_exp, stable_exp, scope_exp))));
    }

    public static void addSetStates(DashModel dashModel, TlaModel tlaPlusModel) {
        // this function adds in a formula used for typeOK:
        // _set_conf = union of all leaf state formulae

        List<TlaFormulaAppl> leafStateFormulaApplications =
                GeneralUtil.mapBy(
                        AuxiliaryDashAccessors.getLeafStateNames(dashModel),
                        name -> new TlaFormulaAppl(TranslationStrings.getStateFormulaName(name)));

        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        TranslationStrings.CONF.typeDecl(),
                        TranslationStrings.repeatedUnion(leafStateFormulaApplications)));
    }

    public static void addSetTransitionTaken(DashModel dashModel, TlaModel tlaPlusModel) {
        tlaPlusModel.addFormulaDefinition(
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

    public static void addSetScopesUsed(DashModel dashModel, TlaModel tlaPlusModel) {
        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        TranslationStrings.SCOPE_USED.typeDecl(),
                        unionNone(TranslationStrings.CONF.typeAppl())));
    }
}
