package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaApplication;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDeclaration;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDefinition;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusInSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusUnionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusBoolean;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusStringLiteral;
import ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators.TLAPlusSet;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.Arrays;
import java.util.List;

public class TypeOKFormula {

    public static void AddTypeOKFormula(DashModel dashModel, TLAPlusModel tlaPlusModel) {
        addSetStates(dashModel, tlaPlusModel);
        addSetTransitionTaken(dashModel, tlaPlusModel);
        addSetScopesUsed(dashModel, tlaPlusModel);
        addTypeOKFormula(tlaPlusModel);
    }

    private static TLAPlusUnionSet unionNone(TLAPlusExpression e) {
        return new TLAPlusUnionSet(
                e, new TLAPlusSet(Arrays.asList(new TLAPlusStringLiteral(Common.NONE))));
    }

    public static void addTypeOKFormula(TLAPlusModel tlaPlusModel) {
        TLAPlusExpression conf_exp =
                new TLAPlusInSet(
                        Common.getConf(), new TLAPlusFormulaApplication(Common.getSetConf()));
        TLAPlusExpression trans_taken_exp =
                new TLAPlusInSet(
                        Common.getTransTaken(),
                        new TLAPlusFormulaApplication(Common.getSetTransTaken()));
        TLAPlusExpression scope_exp =
                new TLAPlusInSet(
                        Common.getScopeUsed(), new TLAPlusFormulaApplication(Common.getSetConf()));
        TLAPlusExpression stable_exp = new TLAPlusInSet(Common.getStable(), new TLAPlusBoolean());

        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(Common.TYPEOK),
                        Common.repeatedAnd(
                                Arrays.asList(conf_exp, trans_taken_exp, stable_exp, scope_exp))));
    }

    public static void addSetStates(DashModel dashModel, TLAPlusModel tlaPlusModel) {
        // this function adds in a formula used for typeOK:
        // _set_conf = union of all leaf state formulae

        List<TLAPlusFormulaApplication> leafStateFormulaApplications =
                GeneralUtil.mapBy(
                        AuxiliaryDashAccessors.getLeafStateNames(dashModel),
                        name -> new TLAPlusFormulaApplication(Common.getStateFormulaName(name)));

        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(Common.getSetConf()),
                        Common.repeatedUnion(leafStateFormulaApplications)));
    }

    public static void addSetTransitionTaken(DashModel dashModel, TLAPlusModel tlaPlusModel) {
        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(Common.getSetTransTaken()),
                        unionNone(
                                new TLAPlusSet(
                                        GeneralUtil.mapBy(
                                                AuxiliaryDashAccessors.getTransitionNames(
                                                        dashModel),
                                                s ->
                                                        new TLAPlusFormulaApplication(
                                                                Common.getTakenTransFormulaName(
                                                                        s)))))));
    }

    public static void addSetScopesUsed(DashModel dashModel, TLAPlusModel tlaPlusModel) {
        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(Common.getSetScopesUsed()),
                        unionNone(new TLAPlusFormulaApplication(Common.getSetConf()))));
    }
}
