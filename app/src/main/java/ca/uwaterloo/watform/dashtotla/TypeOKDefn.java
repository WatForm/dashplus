package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaDecl;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaEquals;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaInSet;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaOr;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaSubsetEq;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaBoolean;
import ca.uwaterloo.watform.tlaast.tlanaryops.TlaSet;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
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
                new TlaDefn(
                        new TlaDecl(typeFormula(CONF)),
                        repeatedUnion(
                                GeneralUtil.mapBy(leafStateFQNs, x -> new TlaAppl(tlaFQN(x))))));
    }

    public static void typeTransTaken(DashModel dashModel, TlaModel tlaModel) {

        // _all_trans_taken == {_taken_<ti>,...}
        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(typeFormula(TRANS_TAKEN)),
                        new TlaSet(
                                GeneralUtil.mapBy(
                                        AuxDashAccessors.getTransitionNames(dashModel),
                                        s -> new TlaAppl(takenTransTlaFQN(s))))));
    }

    public static void typeScopesUsed(DashModel dashModel, TlaModel tlaModel) {

        // _all_scopes_used == _all_conf
        tlaModel.addDefn(
                new TlaDefn(new TlaDecl(typeFormula(SCOPES_USED)), new TlaDecl(typeFormula(CONF))));

        // this may be subject to change later
    }

    public static void TypeOK(List<String> varNames, TlaModel tlaModel) {

        // _conf \subseteq _all_conf
        TlaExp conf_exp = new TlaSubsetEq(new TlaVar(CONF), new TlaAppl(typeFormula(CONF)));

        // _trans_taken \in _all_trans_taken \/ _trans_taken = {}
        // _trans_taken = {} is for stutter
        TlaExp trans_taken_exp =
                new TlaOr(
                        new TlaInSet(
                                new TlaVar(TRANS_TAKEN), new TlaAppl(typeFormula(TRANS_TAKEN))),
                        new TlaEquals(new TlaVar(TRANS_TAKEN), NULL_SET));

        // _scope_used \subseteq _all_scope_used
        TlaExp scope_exp =
                new TlaSubsetEq(new TlaVar(SCOPES_USED), new TlaAppl(typeFormula(SCOPES_USED)));

        // _stable \in BOOLEAN
        TlaExp stable_exp = new TlaInSet(new TlaVar(STABLE), new TlaBoolean());

        List<TlaExp> expressions = new ArrayList<>();
        if (varNames.contains(CONF)) expressions.add(conf_exp);
        if (varNames.contains(STABLE)) expressions.add(stable_exp);
        if (varNames.contains(SCOPES_USED)) expressions.add(scope_exp);
        if (varNames.contains(TRANS_TAKEN)) expressions.add(trans_taken_exp);

        tlaModel.addDefn(new TlaDefn(new TlaDecl(TYPE_OK), repeatedAnd(expressions)));
    }
}
