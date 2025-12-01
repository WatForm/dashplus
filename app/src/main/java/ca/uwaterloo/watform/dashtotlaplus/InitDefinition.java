package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TLAPlusExp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDecl;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDefn;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusEquals;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusStringLiteral;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusTrue;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;
import java.util.Arrays;

public class InitDefinition {
    public static void addInitFormula(DashModel dashModel, TLAPlusModel tlaPlusModel) {

		// stable = TRUE
        TLAPlusExp stable_exp = new TLAPlusEquals(TranslationStrings.getStable(), new TLAPlusTrue());

		// trans_taken = {}
        TLAPlusExp trans_taken_exp =
                new TLAPlusEquals(TranslationStrings.getTransTaken(), new TLAPlusStringLiteral(TranslationStrings.NONE));
        
		// scopes_used = {}
		TLAPlusExp scopes_used_exp =
                new TLAPlusEquals(TranslationStrings.getScopeUsed(), TranslationStrings.getNullSet());

		// events = {}
        TLAPlusExp events_exp = new TLAPlusEquals(TranslationStrings.getEvents(), TranslationStrings.getNullSet());

		// conf = {<initial states>}
        TLAPlusExp conf_exp =
                new TLAPlusEquals(TranslationStrings.getEvents(), new TLAPlusStringLiteral("placeholder"));

        tlaPlusModel.addFormulaDefinition(
                new TLAPlusFormulaDefn(
                        new TLAPlusFormulaDecl(TranslationStrings.INIT),
                        TranslationStrings.repeatedAnd(
                                Arrays.asList(
                                        conf_exp,
                                        events_exp,
                                        scopes_used_exp,
                                        stable_exp,
                                        trans_taken_exp))));
    }
}
