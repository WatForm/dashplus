package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TLAPlusExp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaApplication;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDecl;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDefn;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusInSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusUnionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusBoolean;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusStringLiteral;
import ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators.TLAPlusSet;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.Arrays;
import java.util.List;

public class TypeOKDefinitions {

    public static void AddTypeOKFormula(DashModel dashModel, TLAPlusModel tlaPlusModel) {
        addSetStates(dashModel, tlaPlusModel);
        addSetTransitionTaken(dashModel, tlaPlusModel);
        addSetScopesUsed(dashModel, tlaPlusModel);
        addTypeOKFormula(tlaPlusModel);
    }

    private static TLAPlusUnionSet unionNone(TLAPlusExp e) {
        return new TLAPlusUnionSet(
                e, new TLAPlusSet(Arrays.asList(new TLAPlusStringLiteral(TranslationStrings.NONE))));
    }

    public static void addTypeOKFormula(TLAPlusModel tlaPlusModel) {
        TLAPlusExp conf_exp =
                new TLAPlusInSet(
                        TranslationStrings.getConf(), new TLAPlusFormulaApplication(TranslationStrings.getSetConf()));
        TLAPlusExp trans_taken_exp =
                new TLAPlusInSet(
                        TranslationStrings.getTransTaken(),
                        new TLAPlusFormulaApplication(TranslationStrings.getSetTransTaken()));
        TLAPlusExp scope_exp =
                new TLAPlusInSet(
                        TranslationStrings.getScopeUsed(), new TLAPlusFormulaApplication(TranslationStrings.getSetConf()));
        TLAPlusExp stable_exp = new TLAPlusInSet(TranslationStrings.getStable(), new TLAPlusBoolean());

        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefn(
                        new TLAPlusFormulaDecl(TranslationStrings.TYPEOK),
                        TranslationStrings.repeatedAnd(
                                Arrays.asList(conf_exp, trans_taken_exp, stable_exp, scope_exp))));
    }

    public static void addSetStates(DashModel dashModel, TLAPlusModel tlaPlusModel) {
        // this function adds in a formula used for typeOK:
        // _set_conf = union of all leaf state formulae

        List<TLAPlusFormulaApplication> leafStateFormulaApplications =
                GeneralUtil.mapBy(
                        AuxiliaryDashAccessors.getLeafStateNames(dashModel),
                        name -> new TLAPlusFormulaApplication(TranslationStrings.getStateFormulaName(name)));

        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefn(
                        new TLAPlusFormulaDecl(TranslationStrings.getSetConf()),
                        TranslationStrings.repeatedUnion(leafStateFormulaApplications)));
    }

    public static void addSetTransitionTaken(DashModel dashModel, TLAPlusModel tlaPlusModel) {
        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefn(
                        new TLAPlusFormulaDecl(TranslationStrings.getSetTransTaken()),
                        unionNone(
                                new TLAPlusSet(
                                        GeneralUtil.mapBy(
                                                AuxiliaryDashAccessors.getTransitionNames(
                                                        dashModel),
                                                s ->
                                                        new TLAPlusFormulaApplication(
                                                                TranslationStrings.getTakenTransFormulaName(
                                                                        s)))))));
    }

    public static void addSetScopesUsed(DashModel dashModel, TLAPlusModel tlaPlusModel) {
        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefn(
                        new TLAPlusFormulaDecl(TranslationStrings.getSetScopesUsed()),
                        unionNone(new TLAPlusFormulaApplication(TranslationStrings.getSetConf()))));
    }
}
