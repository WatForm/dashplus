package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.*;
import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.tlaunops.TlaSubsetUnary;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class TypeOKDefn {

    public static void translate(List<String> vars, DashModel dashModel, TlaModel tlaModel) {

        // these are separate functions since the presence of the variables themselves are subject
        // to optimization

        if (vars.contains(CONF)) typeConf(dashModel, tlaModel);
        if (vars.contains(TRANS_TAKEN)) typeTransTaken(dashModel, tlaModel);
        if (vars.contains(SCOPES_USED)) typeScopesUsed(dashModel, tlaModel);
        TypeOK(vars, tlaModel);
    }

    public static void typeConf(DashModel dashModel, TlaModel tlaModel) {

        // _all_conf = <union of all leaf state formulae>

        List<String> leafStateFQNs = AuxDashAccessors.getLeafStateNames(dashModel);

        tlaModel.addDefn(
                TlaDefn(
                        typeDefn(CONF),
                        repeatedUnion(mapBy(leafStateFQNs, x -> TlaAppl(tlaFQN(x))))));
    }

    public static void typeTransTaken(DashModel dashModel, TlaModel tlaModel) {

        // _all_trans_taken == {_taken_<trans-i>,...,_none_transition}
        List<String> transTakenNames =
                mapBy(AuxDashAccessors.getTransitionNames(dashModel), x -> takenTransTlaFQN(x));
        transTakenNames.add((NONE_TRANSITION));

        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl(typeDefn(TRANS_TAKEN)),
                        TlaSet(mapBy(transTakenNames, x -> TlaAppl(x)))));
    }

    public static void typeScopesUsed(DashModel dashModel, TlaModel tlaModel) {

        // _all_scopes_used == _all_conf
        tlaModel.addDefn(TlaDefn(TlaDecl(typeDefn(SCOPES_USED)), TlaDecl(typeDefn(CONF))));

        // this may be subject to change later
    }

    public static void TypeOK(List<String> vars, TlaModel tlaModel) {

        List<TlaExp> exps = new ArrayList<>();

        if (vars.contains(CONF))
            exps.add(
                    // _conf \in SUBSET _all_conf
                    CONF().IN(new TlaSubsetUnary(TlaAppl(typeDefn(CONF)))));

        if (vars.contains(STABLE))
            exps.add(
                    // _stable \in BOOLEAN
                    STABLE().IN(TlaBoolean()));

        if (vars.contains(SCOPES_USED))
            exps.add(
                    // _scope_used \subseteq _all_scope_used
                    SCOPES_USED().IN(new TlaSubsetUnary(TlaAppl(typeDefn(SCOPES_USED)))));

        if (vars.contains(TRANS_TAKEN))
            exps.add(
                    // _trans_taken \in _all_trans_taken
                    TRANS_TAKEN().IN(TlaAppl(typeDefn(TRANS_TAKEN))));

        if (vars.contains(EVENTS))
            exps.add(
                    // _events \in _environmental_events union _internal_events
                    EVENTS().IN(ENVIRONMENTAL_EVENTS().UNION(INTERNAL_EVENTS())));

        tlaModel.addDefn(TlaDefn(TYPE_OK, repeatedAnd(exps)));
    }
}
