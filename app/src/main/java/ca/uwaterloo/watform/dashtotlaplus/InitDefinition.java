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

public class InitDefinition {
    public static void addInitFormula(DashModel dashModel, TLAPlusModel tlaPlusModel) {

		// stable = TRUE
        TLAPlusExpression stable_exp = new TLAPlusEquals(TranslationStrings.getStable(), new TLAPlusTrue());

		// trans_taken = {}
        TLAPlusExpression trans_taken_exp =
                new TLAPlusEquals(TranslationStrings.getTransTaken(), new TLAPlusStringLiteral(TranslationStrings.NONE));
        
		// scopes_used = {}
		TLAPlusExpression scopes_used_exp =
                new TLAPlusEquals(TranslationStrings.getScopeUsed(), TranslationStrings.getNullSet());

		// events = {}
        TLAPlusExpression events_exp = new TLAPlusEquals(TranslationStrings.getEvents(), TranslationStrings.getNullSet());

		// conf = {<initial states>}
        TLAPlusExpression conf_exp =
                new TLAPlusEquals(TranslationStrings.getEvents(), new TLAPlusStringLiteral("placeholder"));

        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(TranslationStrings.INIT),
                        TranslationStrings.repeatedAnd(
                                Arrays.asList(
                                        conf_exp,
                                        events_exp,
                                        scopes_used_exp,
                                        stable_exp,
                                        trans_taken_exp))));
    }
}
