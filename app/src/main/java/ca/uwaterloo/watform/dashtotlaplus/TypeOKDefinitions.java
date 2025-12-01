package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaAppl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDecl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDefn;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusInSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusUnionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusBoolean;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusStringLiteral;
import ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators.TLAPlusSet;
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

    private static TLAPlusUnionSet unionNone(TlaExp e) {
        return new TLAPlusUnionSet(
                e, new TLAPlusSet(Arrays.asList(new TLAPlusStringLiteral(TranslationStrings.NONE))));
    }

    public static void addTypeOKFormula(TlaModel tlaPlusModel) {
        TlaExp conf_exp =
                new TLAPlusInSet(
                        TranslationStrings.getConf(), new TlaFormulaAppl(TranslationStrings.getSetConf()));
        TlaExp trans_taken_exp =
                new TLAPlusInSet(
                        TranslationStrings.getTransTaken(),
                        new TlaFormulaAppl(TranslationStrings.getSetTransTaken()));
        TlaExp scope_exp =
                new TLAPlusInSet(
                        TranslationStrings.getScopeUsed(), new TlaFormulaAppl(TranslationStrings.getSetConf()));
        TlaExp stable_exp = new TLAPlusInSet(TranslationStrings.getStable(), new TLAPlusBoolean());

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
                                new TLAPlusSet(
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
