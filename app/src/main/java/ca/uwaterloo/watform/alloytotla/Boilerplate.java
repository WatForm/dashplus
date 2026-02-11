package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.*;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlamodel.*;
import java.util.*;

public class Boilerplate {

    private static final TlaVar S() {
        return TlaVar(SPECIAL + "S");
    }

    private static final TlaVar X() {
        return TlaVar(SPECIAL + "x");
    }

    private static final TlaVar Y() {
        return TlaVar(SPECIAL + "y");
    }

    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {

        List<TlaConst> setConsts =
                mapBy(Auxiliary.getTopLevelSigNames(alloyModel), s -> TlaConst(sigSet(s)));

        // _univ == A_set \\union B_set... where A, B... are top-level sigs
        tlaModel.addDefn(TlaDefn(UNIV, repeatedUnion(setConsts)));

        // _none == {}
        tlaModel.addDefn(TlaDefn(NONE, NULL_SET()));

        // _iden = {<<x,x>> : x \in _univ}
        tlaModel.addDefn(TlaDefn(IDEN, TlaSetMap(X(), _UNIV(), TlaTuple(X(), X()))));

        tlaModel.addDefn(some());
        tlaModel.addDefn(lone());
        tlaModel.addDefn(one());
        tlaModel.addDefn(no());
    }

    public static TlaAppl _SOME(TlaExp e) {
        return TlaAppl(SOME, Arrays.asList(e));
    }

    public static TlaAppl _LONE(TlaExp e) {
        return TlaAppl(LONE, Arrays.asList(e));
    }

    public static TlaAppl _ONE(TlaExp e) {
        return TlaAppl(ONE, Arrays.asList(e));
    }

    public static TlaAppl _NO(TlaExp e) {
        return TlaAppl(NO, Arrays.asList(e));
    }

    public static TlaAppl _UNIV() {
        return TlaAppl(UNIV);
    }

    public static TlaAppl _IDEN() {
        return TlaAppl(IDEN);
    }

    public static TlaAppl _NONE() {
        return TlaAppl(NONE);
    }

    private static TlaExp allEqual(TlaVar v, TlaVar v1, TlaVar v2) {
        return TlaForAll(v1, v, TlaForAll(v2, v, v1.EQUALS(v2)));
    }

    private static TlaDefn some() {
        // some
        return new TlaDefn(TlaDecl(SOME, Arrays.asList(S())), TlaNot(allEqual(S(), X(), Y())));
    }

    private static TlaDefn lone() {
        // _lone(S)
        return new TlaDefn(TlaDecl(LONE, Arrays.asList(S())), allEqual(S(), X(), Y()));
    }

    private static TlaDefn one() {
        // _one(S)
        return new TlaDefn(
                TlaDecl(ONE, Arrays.asList(S())),
                allEqual(S(), X(), Y()).AND(S().NOT_EQUALS(NULL_SET())));
    }

    private static TlaDefn no() {
        // _one(S)
        return new TlaDefn(
                TlaDecl(NO, Arrays.asList(S())),
                allEqual(S(), X(), Y()).AND(S().EQUALS(NULL_SET())));
    }
}
