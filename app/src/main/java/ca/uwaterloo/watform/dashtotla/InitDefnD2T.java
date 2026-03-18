package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.*;
import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class InitDefnD2T extends EventDefnsD2T {

    public InitDefnD2T(DashModel dashModel, TlaModel tlaModel, boolean verbose, boolean debug) {
        super(dashModel, tlaModel, verbose, debug);
    }

    protected void translateInitDefn() {
        List<TlaExp> exps = new ArrayList<>();

        exps.add(VALID_UNPRIMED());

        exps.add(
                // _trans_taken = _none_transition
                TRANS_TAKEN().EQUALS(NONE_TRANSITION()));

        if (!dashModel.hasOnlyOneState()) {
            List<TlaAppl> initialStates =
                    mapBy(dashModel.rootLeafStatesEntered(), ref -> TlaAppl(tlaFQN(ref.name)));

            exps.add(
                    // conf = union <initial states>...
                    CONF().EQUALS(repeatedUnion(initialStates)));
        }

        if (dashModel.hasConcurrency()) {
            exps.add(
                    // stable = TRUE
                    STABLE().EQUALS(TlaTrue()));
            exps.add(
                    // scopes_used = {}
                    SCOPES_USED().EQUALS(TlaNullSet()));
        }

        if (dashModel.hasEvents()) {
            exps.add(
                    // _events \intersect _internal_events = {}
                    EVENTS().INTERSECTION(INTERNAL_EVENTS()).EQUALS(TlaNullSet()));

            // single env input assumption
            exps.add(SINGLE_ENV_INPUT());
        }

        tlaModel.addDefn(TlaDefn(INIT, repeatedAnd(exps)));
    }
}
