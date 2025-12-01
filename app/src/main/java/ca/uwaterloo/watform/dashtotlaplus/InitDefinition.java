package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDecl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDefn;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaEquals;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TlaLiteral;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TlaTrue;
import ca.uwaterloo.watform.tlaplusmodel.TlaModel;
import java.util.Arrays;

public class InitDefinition {
    public static void addInitFormula(DashModel dashModel, TlaModel tlaPlusModel) {

        // stable = TRUE
        TlaExp stable_exp = new TlaEquals(TranslationStrings.getStable(), new TlaTrue());

        // trans_taken = {}
        TlaExp trans_taken_exp =
                new TlaEquals(
                        TranslationStrings.getTransTaken(),
                        new TlaLiteral(TranslationStrings.NONE));

        // scopes_used = {}
        TlaExp scopes_used_exp =
                new TlaEquals(TranslationStrings.getScopeUsed(), TranslationStrings.getNullSet());

        // events = {}
        TlaExp events_exp =
                new TlaEquals(TranslationStrings.getEvents(), TranslationStrings.getNullSet());

        // conf = {<initial states>}
        TlaExp conf_exp =
                new TlaEquals(TranslationStrings.getEvents(), new TlaLiteral("placeholder"));

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
