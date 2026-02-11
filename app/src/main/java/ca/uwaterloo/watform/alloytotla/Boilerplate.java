package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.NULL_SET;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaAppl;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaDecl;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaForAll;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaNot;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaVar;

import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.Arrays;

public class Boilerplate {

    private static final String S = "S";
    private static final String X = "x";
    private static final String Y = "y";

    public static void translate(TlaModel tlaModel) {
        tlaModel.addDefn(some());
        tlaModel.addDefn(lone());
        tlaModel.addDefn(one());
        tlaModel.addDefn(none());
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

    public static TlaAppl _NONE(TlaExp e) {
        return TlaAppl(NONE, Arrays.asList(e));
    }

    private static TlaExp allEqual(String v, String v1, String v2) {
        return TlaForAll(
                TlaVar(v1),
                TlaVar(v),
                TlaForAll(TlaVar(v2), TlaVar(v), TlaVar(v1).EQUALS(TlaVar(v2))));
    }

    private static TlaExp isNull(String v) {
        return TlaVar(v).EQUALS(NULL_SET());
    }

    private static TlaDefn some() {
        // some
        return new TlaDefn(TlaDecl(SOME, Arrays.asList(TlaVar(S))), TlaNot(allEqual(S, X, Y)));
    }

    private static TlaDefn lone() {
        // _lone(S)
        return new TlaDefn(TlaDecl(LONE, Arrays.asList(TlaVar(S))), allEqual(S, X, Y));
    }

    private static TlaDefn one() {
        // _one(S)
        return new TlaDefn(
                TlaDecl(ONE, Arrays.asList(TlaVar(S))), allEqual(S, X, Y).AND(TlaNot(isNull(S))));
    }

    private static TlaDefn none() {
        // _none(S) == ! x != {}
        return new TlaDefn(TlaDecl(NONE, Arrays.asList(TlaVar(S))), isNull(S));
    }
}
