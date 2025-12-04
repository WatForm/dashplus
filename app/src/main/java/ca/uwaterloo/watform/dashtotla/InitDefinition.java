package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.TranslationStrings.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaDecl;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaEquals;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaLiteral;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaTrue;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.Arrays;

public class InitDefinition {
    public static void addInitFormula(DashModel dashModel, TlaModel tlaModel) {

        // stable = TRUE
        TlaExp stable_exp = new TlaEquals(new TlaVar(STABLE), new TlaTrue());

        // trans_taken = {}
        TlaExp trans_taken_exp = new TlaEquals(new TlaVar(TRANS_TAKEN), NULL_SET);

        // scopes_used = {}
        TlaExp scopes_used_exp = new TlaEquals(new TlaVar(SCOPE_USED), NULL_SET);

        // events = {}
        TlaExp events_exp = new TlaEquals(new TlaVar(EVENTS), NULL_SET);

        // conf = {<initial states>}
        TlaExp conf_exp = new TlaEquals(new TlaVar(CONF), new TlaLiteral("placeholder"));

        tlaModel.addFormulaDefinition(
                new TlaDefn(
                        new TlaDecl(INIT),
                        TranslationStrings.repeatedAnd(
                                Arrays.asList(
                                        conf_exp,
                                        events_exp,
                                        scopes_used_exp,
                                        stable_exp,
                                        trans_taken_exp))));
    }
}
