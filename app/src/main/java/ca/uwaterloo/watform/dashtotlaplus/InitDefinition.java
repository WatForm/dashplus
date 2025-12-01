package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDecl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDefn;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusEquals;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusStringLiteral;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusTrue;
import ca.uwaterloo.watform.tlaplusmodel.TlaModel;
import java.util.Arrays;

public class InitDefinition {
    public static void addInitFormula(DashModel dashModel, TlaModel tlaPlusModel) {

		// stable = TRUE
        TlaExp stable_exp = new TLAPlusEquals(TranslationStrings.getStable(), new TLAPlusTrue());

		// trans_taken = {}
        TlaExp trans_taken_exp =
                new TLAPlusEquals(TranslationStrings.getTransTaken(), new TLAPlusStringLiteral(TranslationStrings.NONE));
        
		// scopes_used = {}
		TlaExp scopes_used_exp =
                new TLAPlusEquals(TranslationStrings.getScopeUsed(), TranslationStrings.getNullSet());

		// events = {}
        TlaExp events_exp = new TLAPlusEquals(TranslationStrings.getEvents(), TranslationStrings.getNullSet());

		// conf = {<initial states>}
        TlaExp conf_exp =
                new TLAPlusEquals(TranslationStrings.getEvents(), new TLAPlusStringLiteral("placeholder"));

        tlaPlusModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        new TlaFormulaDecl(TranslationStrings.INIT),
                        TranslationStrings.repeatedAnd(
                                Arrays.asList(
                                        conf_exp,
                                        events_exp,
                                        scopes_used_exp,
                                        stable_exp,
                                        trans_taken_exp))));
    }
}
