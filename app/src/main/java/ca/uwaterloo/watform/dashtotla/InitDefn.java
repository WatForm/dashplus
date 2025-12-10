package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaDecl;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaEquals;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaLiteral;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaTrue;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class InitDefn {
    public static void translate(List<String> varNames, DashModel dashModel, TlaModel tlaModel) {

        // stable = TRUE
        TlaExp stable_exp = new TlaEquals(new TlaVar(STABLE), new TlaTrue());

        // trans_taken = {}
        TlaExp trans_taken_exp = new TlaEquals(new TlaVar(TRANS_TAKEN), NULL_SET);

        // scopes_used = {}
        TlaExp scopes_used_exp = new TlaEquals(new TlaVar(SCOPES_USED), NULL_SET);

        // events = {}
        TlaExp events_exp = new TlaEquals(new TlaVar(EVENTS), NULL_SET);

        // conf = {<initial states>}
        TlaExp conf_exp = new TlaEquals(new TlaVar(CONF), new TlaLiteral("placeholder"));

        List<TlaExp> expressions = new ArrayList<>();
        if (varNames.contains(CONF)) expressions.add(conf_exp);
        if (varNames.contains(SCOPES_USED)) expressions.add(scopes_used_exp);
        if (varNames.contains(STABLE)) expressions.add(stable_exp);
        if (varNames.contains(TRANS_TAKEN)) expressions.add(trans_taken_exp);
        if (varNames.contains(EVENTS)) expressions.add(events_exp);

        System.out.println(expressions);

        tlaModel.addDefn(new TlaDefn(new TlaDecl(INIT), repeatedAnd(expressions)));
    }
}
