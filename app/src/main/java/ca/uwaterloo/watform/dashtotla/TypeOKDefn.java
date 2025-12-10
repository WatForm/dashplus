package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class TypeOKDefn {

    public static void translate(List<String> varNames, DashModel dashModel, TlaModel tlaModel) {

        // these are separate functions since the presence of the variables themselves are subject
        // to optimization

        if (varNames.contains(CONF)) typeConf(dashModel, tlaModel);
        if (varNames.contains(TRANS_TAKEN)) typeTransTaken(dashModel, tlaModel);
        if (varNames.contains(SCOPES_USED)) typeScopesUsed(dashModel, tlaModel);
        TypeOK(varNames, tlaModel);
    }

    public static void typeConf(DashModel dashModel, TlaModel tlaModel) {

        // _all_conf = <union of all leaf state formulae>

        List<String> leafStateFQNs = AuxDashAccessors.getLeafStateNames(dashModel);

        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl(typeFormula(CONF)),
                        repeatedUnion(mapBy(leafStateFQNs, x -> TlaAppl(tlaFQN(x))))));
    }

    public static void typeTransTaken(DashModel dashModel, TlaModel tlaModel) {

        // _all_trans_taken == {_taken_<ti>,...,_none_transition}
        List<String> transTakenNames =
                mapBy(AuxDashAccessors.getTransitionNames(dashModel), x -> takenTransTlaFQN(x));
        transTakenNames.add((NONE_TRANSITION));

        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl(typeFormula(TRANS_TAKEN)),
                        TlaSet(mapBy(transTakenNames, x -> TlaAppl(x)))));
    }

    public static void typeScopesUsed(DashModel dashModel, TlaModel tlaModel) {

        // _all_scopes_used == _all_conf
        tlaModel.addDefn(TlaDefn(TlaDecl(typeFormula(SCOPES_USED)), TlaDecl(typeFormula(CONF))));

        // this may be subject to change later
    }

    public static void TypeOK(List<String> varNames, TlaModel tlaModel) {

        // _conf \subseteq _all_conf
        TlaExp conf_exp = TlaSubsetEq(TlaVar(CONF), TlaAppl(typeFormula(CONF)));

        // _trans_taken \in _all_trans_taken
        TlaExp trans_taken_exp = TlaInSet(TlaVar(TRANS_TAKEN), TlaAppl(typeFormula(TRANS_TAKEN)));

        // _scope_used \subseteq _all_scope_used
        TlaExp scope_exp = TlaSubsetEq(TlaVar(SCOPES_USED), TlaAppl(typeFormula(SCOPES_USED)));

        // _stable \in BOOLEAN
        TlaExp stable_exp = TlaInSet(TlaVar(STABLE), TlaBoolean());

        List<TlaExp> expressions = new ArrayList<>();
        if (varNames.contains(CONF)) expressions.add(conf_exp);
        if (varNames.contains(STABLE)) expressions.add(stable_exp);
        if (varNames.contains(SCOPES_USED)) expressions.add(scope_exp);
        if (varNames.contains(TRANS_TAKEN)) expressions.add(trans_taken_exp);

        tlaModel.addDefn(TlaDefn(TlaDecl(TYPE_OK), repeatedAnd(expressions)));
    }
}
