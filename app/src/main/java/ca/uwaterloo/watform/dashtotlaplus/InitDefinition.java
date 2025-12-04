package ca.uwaterloo.watform.dashtotlaplus;

import static ca.uwaterloo.watform.dashtotlaplus.TranslationStrings.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDefn;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaEquals;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TlaLiteral;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TlaTrue;
import ca.uwaterloo.watform.tlaplusmodel.TlaModel;
import java.util.Arrays;

public class InitDefinition {
    public static void addInitFormula(DashModel dashModel, TlaModel tlaModel) {

        // stable = TRUE
        TlaExp stable_exp = new TlaEquals(STABLE.globalVar(), new TlaTrue());

        // trans_taken = {}
        TlaExp trans_taken_exp = new TlaEquals(TRANS_TAKEN.globalVar(), NULL_SET);

        // scopes_used = {}
        TlaExp scopes_used_exp = new TlaEquals(SCOPE_USED.globalVar(), NULL_SET);

        // events = {}
        TlaExp events_exp = new TlaEquals(EVENTS.globalVar(), NULL_SET);

        // conf = {<initial states>}
        TlaExp conf_exp = new TlaEquals(EVENTS.globalVar(), new TlaLiteral("placeholder"));

        tlaModel.addFormulaDefinition(
                new TlaFormulaDefn(
                        INIT.decl(),
                        TranslationStrings.repeatedAnd(
                                Arrays.asList(
                                        conf_exp,
                                        events_exp,
                                        scopes_used_exp,
                                        stable_exp,
                                        trans_taken_exp))));
    }
}
