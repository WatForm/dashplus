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

    public static final TlaVar X() {
        return TlaVar(SPECIAL + "x");
    }

    public static final TlaVar Y() {
        return TlaVar(SPECIAL + "y");
    }

    public static final TlaVar R1() {
        return TlaVar(SPECIAL + "R1");
    }

    public static final TlaVar R2() {
        return TlaVar(SPECIAL + "R2");
    }

    public static final TlaVar E1() {
        return TlaVar(SPECIAL + "e1");
    }

    public static final TlaVar E2() {
        return TlaVar(SPECIAL + "e2");
    }

    public static final TlaVar R() {
        return TlaVar(SPECIAL + "R");
    }

    public static void translate(AlloyModel alloyModel, TlaModel tlaModel) {

        List<TlaConst> setConsts = mapBy(alloyModel.topLevelSigs(), s -> TlaConst(sigSet(s)));

        tlaModel.addDefn(univ(setConsts));
        tlaModel.addDefn(none());
        tlaModel.addDefn(iden());
        tlaModel.addDefn(some());
        tlaModel.addDefn(lone());
        tlaModel.addDefn(one());
        tlaModel.addDefn(no());
        tlaModel.addDefn(transpose());
        tlaModel.addDefn(domain_restriction());
        tlaModel.addDefn(range_restriction());
        tlaModel.addDefn(inner_product_map());
        tlaModel.addDefn(inner_product_filter());
        tlaModel.addDefn(inner_product());
        tlaModel.addDefn(relational_override());
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

    public static TlaAppl _RANGE_RESTRICTION(TlaExp relation, TlaExp set) {
        return TlaAppl(RANGE_RESTRICTION, Arrays.asList(relation, set));
    }

    public static TlaAppl _DOMAIN_RESTRICTION(TlaExp relation, TlaExp set) {
        return TlaAppl(DOMAIN_RESTRICTION, Arrays.asList(relation, set));
    }

    public static TlaAppl _TRANSPOSE(TlaExp relation) {
        return TlaAppl(TRANSPOSE, Arrays.asList(relation));
    }

    public static TlaAppl _RELATIONAL_OVERRIDE(TlaExp r1, TlaExp r2) {
        return TlaAppl(RELATIONAL_OVERRIDE, Arrays.asList(r1, r2));
    }

    public static TlaAppl _INNER_PRODUCT(TlaExp r1, TlaExp r2) {
        return TlaAppl(INNER_PRODUCT, Arrays.asList(r1, r2));
    }

    private static TlaDefn range_restriction() {

        // _range_restrict(R,S) : {e \in R : e[Len(e)] \in S}
        TlaExp body =
                TlaSetFilter(TlaQuantOpHead(X(), R()), X().INDEX(TlaStdLibs.Len(X())).IN(S()));
        return new TlaDefn(TlaDecl(RANGE_RESTRICTION, Arrays.asList(R(), S())), body);
    }

    private static TlaDefn domain_restriction() {
        // _domain_restrict(S,R) : {x \in R : x[1] \in S}
        TlaExp body = TlaSetFilter(TlaQuantOpHead(X(), R()), X().INDEX(TlaIntLiteral(1)).IN(S()));
        return new TlaDefn(TlaDecl(DOMAIN_RESTRICTION, Arrays.asList(S(), R())), body);
    }

    private static TlaDefn inner_product() {
        TlaExp inner = TlaNullSet(); // todo fill this out
        return new TlaDefn(
                TlaDecl(INNER_PRODUCT, Arrays.asList(R1(), R2())),
                TlaSetMap(
                        TlaQuantOpHeadTuple(Arrays.asList(E1(), E2()), inner),
                        TlaAppl(INNER_PRODUCT_MAP, Arrays.asList(E1(), E2()))));
    }

    private static TlaDefn inner_product_filter() {
        return new TlaDefn(
                TlaDecl(INNER_PRODUCT_FILTER, Arrays.asList(E1(), E2())),
                E1().INDEX(TlaStdLibs.Len(E1())).EQUALS(E2().INDEX(TlaIntLiteral(1))));
    }

    private static TlaDefn inner_product_map() {
        return new TlaDefn(
                TlaDecl(INNER_PRODUCT_MAP, Arrays.asList(E1(), E2())), TlaConcatSeq(E1(), E2()));
    }

    private static TlaDefn relational_override() {
        // _override(R1,R2) : R1 \ {x \in R1 : \E y \in R2 : x[1] = y[1]} \\union R2
        TlaExp set =
                TlaSetFilter(
                        TlaQuantOpHead(X(), R1()),
                        TlaExists(
                                TlaQuantOpHead(Y(), R2()),
                                X().INDEX(TlaIntLiteral(1)).EQUALS(Y().INDEX(TlaIntLiteral(1)))));
        TlaExp body = R1().DIFF(set).UNION(R2());
        return new TlaDefn(TlaDecl(RELATIONAL_OVERRIDE, Arrays.asList(R1(), R2())), body);
    }

    private static TlaDefn transpose() {
        // _transpose(R) == {<<y,x>> : <<x,y>> \in R}
        TlaExp body =
                TlaSetMap(
                        TlaQuantOpHeadTuple(Arrays.asList(X(), Y()), R()),
                        TlaTuple(Arrays.asList(Y(), X())));
        return new TlaDefn(TlaDecl(TRANSPOSE, Arrays.asList(R())), body);
    }

    private static TlaDefn none() {
        // _none == {}
        return TlaDefn(NONE, TlaNullSet());
    }

    private static TlaDefn iden() {
        // _iden = {<<x,x>> : x \in _univ}
        return TlaDefn(IDEN, TlaSetMap(TlaQuantOpHead(X(), _UNIV()), TlaTuple(X(), X())));
    }

    private static TlaDefn univ(List<TlaConst> setConsts) {
        // _univ == A_set \\union B_set... where A, B... are top-level sigs
        return TlaDefn(UNIV, repeatedUnion(setConsts));
    }

    private static TlaExp allEqual(TlaVar v, TlaVar v1, TlaVar v2) {
        return TlaForAll(TlaQuantOpHeadFlat(Arrays.asList(v1, v2), v), v1.EQUALS(v2));
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
                allEqual(S(), X(), Y()).AND(S().NOT_EQUALS(TlaNullSet())));
    }

    private static TlaDefn no() {
        // _one(S)
        return new TlaDefn(
                TlaDecl(NO, Arrays.asList(S())),
                allEqual(S(), X(), Y()).AND(S().EQUALS(TlaNullSet())));
    }
}
