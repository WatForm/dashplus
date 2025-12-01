package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaAppl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDecl;
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
                        TranslationStrings.getConf(), new TlaFormulaAppl(TranslationStrings.getSetConf()));
        TlaExp trans_taken_exp =
                new TlaInSet(
                        TranslationStrings.getTransTaken(),
                        new TlaFormulaAppl(TranslationStrings.getSetTransTaken()));
        TlaExp scope_exp =
                new TlaInSet(
                        TranslationStrings.getScopeUsed(), new TlaFormulaAppl(TranslationStrings.getSetConf()));
        TlaExp stable_exp = new TlaInSet(TranslationStrings.getStable(), new TlaBoolean());

        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(TranslationStrings.TYPEOK),
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
                        new TlaFormulaDecl(TranslationStrings.getSetConf()),
                        TranslationStrings.repeatedUnion(leafStateFormulaApplications)));
    }

    public static void addSetTransitionTaken(DashModel dashModel, TlaModel tlaPlusModel) {
        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(TranslationStrings.getSetTransTaken()),
                        unionNone(
                                new TlaSet(
                                        GeneralUtil.mapBy(
                                                AuxiliaryDashAccessors.getTransitionNames(
                                                        dashModel),
                                                s ->
                                                        new TlaFormulaAppl(
                                                                TranslationStrings.getTakenTransFormulaName(
                                                                        s)))))));
    }

    public static void addSetScopesUsed(DashModel dashModel, TlaModel tlaPlusModel) {
        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(TranslationStrings.getSetScopesUsed()),
                        unionNone(new TlaFormulaAppl(TranslationStrings.getSetConf()))));
    }
}
