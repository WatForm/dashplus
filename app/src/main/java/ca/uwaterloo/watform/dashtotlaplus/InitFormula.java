package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDeclaration;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDefinition;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusEquals;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusStringLiteral;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusTrue;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;
import java.util.Arrays;

public class InitFormula {
    public static void addInitFormula(DashModel dashModel, TLAPlusModel tlaPlusModel) {
        TLAPlusExpression stable_exp = new TLAPlusEquals(Common.getStable(), new TLAPlusTrue());
        TLAPlusExpression trans_taken_exp =
                new TLAPlusEquals(Common.getTransTaken(), new TLAPlusStringLiteral(Common.NONE));
        TLAPlusExpression scopes_used_exp =
                new TLAPlusEquals(Common.getScopeUsed(), new TLAPlusStringLiteral(Common.NONE));
        TLAPlusExpression events_exp = new TLAPlusEquals(Common.getEvents(), Common.getNullSet());
        TLAPlusExpression conf_exp =
                new TLAPlusEquals(Common.getEvents(), new TLAPlusStringLiteral("placeholder"));

        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(Common.INIT),
                        Common.repeatedAnd(
                                Arrays.asList(
                                        conf_exp,
                                        events_exp,
                                        scopes_used_exp,
                                        stable_exp,
                                        trans_taken_exp))));
    }
}
