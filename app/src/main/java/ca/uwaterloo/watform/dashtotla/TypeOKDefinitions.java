package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.TranslationStrings.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaDecl;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaInSet;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaSubsetEq;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaBoolean;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaIntSet;
import ca.uwaterloo.watform.tlaast.tlaplusnaryops.TlaSet;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.Arrays;
import java.util.List;

public class TypeOKDefinitions {

    public static void translate(DashModel dashModel, TlaModel tlaModel) {

        // these are separate functions since the presence of the variables themselves are subject
        // to optimization
        typeConf(dashModel, tlaModel);
        typeTransTaken(dashModel, tlaModel);
        typeScopesUsed(dashModel, tlaModel);
        addTypeOKFormula(tlaModel);
    }

    public static void typeConf(DashModel dashModel, TlaModel tlaModel) {

        // _all_conf = <union of all leaf state formulae>

        List<String> leafStateFQNs = AuxiliaryDashAccessors.getLeafStateNames(dashModel);

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
                                        AuxiliaryDashAccessors.getTransitionNames(dashModel),
                                        s -> new TlaAppl(TakenTransFormulaName(s))))));
    }

    public static void typeScopesUsed(DashModel dashModel, TlaModel tlaModel) {

        // _all_scopes_used == _all_conf
        tlaModel.addDefn(
                new TlaDefn(new TlaDecl(typeFormula(SCOPES_USED)), new TlaDecl(typeFormula(CONF))));

        // this may be subject to change later
    }

    public static void addTypeOKFormula(TlaModel tlaModel) {

        // _TypeOK == (_conf \subseteq _all_conf) /\ (_trans_taken \in _all_trans_taken) /\ (_stable
        // \in BOOLEAN) /\ (_scope_used \subseteq _all_scope_used) /\ (_ct \in Int)

        TlaExp conf_exp = new TlaSubsetEq(new TlaVar(CONF), new TlaAppl(typeFormula(CONF)));

        TlaExp trans_taken_exp =
                new TlaInSet(new TlaVar(TRANS_TAKEN), new TlaAppl(typeFormula(TRANS_TAKEN)));

        TlaExp scope_exp =
                new TlaSubsetEq(new TlaVar(SCOPES_USED), new TlaAppl(typeFormula(SCOPES_USED)));

        TlaExp stable_exp = new TlaInSet(new TlaVar(STABLE), new TlaBoolean());

        TlaExp ct_exp = new TlaInSet(new TlaVar(CT), new TlaIntSet());

        tlaModel.addDefn(
                new TlaDefn(
                        new TlaDecl(TYPE_OK),
                        repeatedAnd(
                                Arrays.asList(
                                        conf_exp,
                                        trans_taken_exp,
                                        stable_exp,
                                        scope_exp,
                                        ct_exp))));
    }
}
