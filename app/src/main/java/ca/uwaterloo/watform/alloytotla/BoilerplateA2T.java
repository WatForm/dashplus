package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2THelpers.*;
import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlaast.SnowCatTypes.SCType;
import ca.uwaterloo.watform.tlamodel.*;
import java.util.*;

public class BoilerplateA2T extends BaseA2T {

  public BoilerplateA2T(
      AlloyModel alloyModel, Optimization optimization, boolean verbose, boolean debug) {
    super(alloyModel, optimization, verbose, debug);
  }

  public static final TlaVar S() {
    return TlaVar(SPECIAL + "S");
  }

  public static final TlaVar X() {
    return TlaVar(SPECIAL + "x");
  }

  public static final TlaVar Y() {
    return TlaVar(SPECIAL + "y");
  }

  public static final TlaVar Z() {
    return TlaVar(SPECIAL + "z");
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

  public static final TlaVar F1() {
    return TlaVar(SPECIAL + "f1");
  }

  public static final TlaVar F2() {
    return TlaVar(SPECIAL + "f2");
  }

  public static final TlaVar R() {
    return TlaVar(SPECIAL + "R");
  }

  public void addBoilerplate(TlaModel tlaModel) {

    tlaModel.addComment("translation macros", verbose);

    List<TlaVar> setVars = mapBy(alloyModel.topLevelSigs(), s -> TlaVar(tlaQname(s)));

    tlaModel.addDefn(univ(setVars));
    tlaModel.addDefn(none());
    tlaModel.addDefn(iden());
    tlaModel.addDefn(some());
    tlaModel.addDefn(lone());
    tlaModel.addDefn(one());
    tlaModel.addDefn(no());
    tlaModel.addDefn(transpose());
    tlaModel.addDefn(domain_restriction());
    tlaModel.addDefn(range_restriction());
    tlaModel.addDefn(dot_map());
    tlaModel.addDefn(dot_filter());
    tlaModel.addDefn(dot());
    tlaModel.addDefn(relational_override());
    tlaModel.addDefn(cross());
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

  public static TlaAppl _DOT(TlaExp r1, TlaExp r2) {
    return TlaAppl(DOT_MACRO, Arrays.asList(r1, r2));
  }

  public static TlaAppl _CROSS(TlaExp r1, TlaExp r2) {
    return TlaAppl(CROSS, Arrays.asList(r1, r2));
  }

  private static TlaDefn cross() {

    //  (Set(Seq(Str)), Set(Seq(Str))) => Set(Seq(Str));
    // _cross(_R1,_R2) == {_e1 \o _e2 : <<_e1,_e2>> \in _R1 \X _R2}

    SCType type = SnowCatTypes.OperatorTT2T(relationType());
    TlaDecl decl = TlaDecl(CROSS, Arrays.asList(R1(), R2()));
    TlaExp body =
        TlaSetMap(
            TlaQuantOpHeadTuple(Arrays.asList(E1(), E2()), TlaProductSet(R1(), R2())),
            TlaConcatSeq(E1(), E2()));

    return new TlaDefn(decl, body, type);
  }

  private static TlaDefn range_restriction() {

    //  (Set(Seq(Str)), Set(Seq(Str))) => Set(Seq(Str));
    // _range_restriction(_R,_S) == {_x \in _R : <<_x[Len(_x)]>> \in _S}

    SCType type = SnowCatTypes.OperatorTT2T(relationType());
    TlaDecl decl = TlaDecl(RANGE_RESTRICTION, Arrays.asList(R(), S()));
    TlaExp body =
        TlaSetFilter(TlaQuantOpHead(X(), R()), TlaTuple(X().INDEX(TlaStdLibs.Len(X()))).IN(S()));
    return new TlaDefn(decl, body, type);
  }

  private static TlaDefn domain_restriction() {

    //  (Set(Seq(Str)), Set(Seq(Str))) => Set(Seq(Str));
    // _domain_restriction(_S,_R) == {_x \in _R : <<_x[1]>> \in _S}

    SCType type = SnowCatTypes.OperatorTT2T(relationType());
    TlaDecl decl = TlaDecl(DOMAIN_RESTRICTION, Arrays.asList(S(), R()));
    TlaExp body =
        TlaSetFilter(TlaQuantOpHead(X(), R()), TlaTuple(X().INDEX(TlaIntLiteral(1))).IN(S()));
    return new TlaDefn(decl, body, type);
  }

  private static TlaDefn dot_filter() {

    //  ((Seq(Str)), (Seq(Str))) => Bool;
    // _dot_filter(_e1,_e2) == _e1[Len(_e1)] = _e2[1]

    SCType type =
        SnowCatTypes.Operator(Arrays.asList(elementType(), elementType()), SnowCatTypes.Bool());
    TlaDecl decl = TlaDecl(DOT_FILTER, Arrays.asList(E1(), E2()));
    TlaExp body = E1().INDEX(TlaStdLibs.Len(E1())).EQUALS(E2().INDEX(TlaIntLiteral(1)));

    return new TlaDefn(decl, body, type);
  }

  private static TlaDefn dot_map() {

    // ((Seq(Str)), (Seq(Str))) => Seq(Str);
    // _dot_map(_e1,_e2) == SubSeq(_e1,1,Len(_e1) - 1) \o SubSeq(_e2,2,Len(_e2))

    SCType type = SnowCatTypes.OperatorTT2T(elementType());
    TlaDecl decl = TlaDecl(DOT_MAP, Arrays.asList(E1(), E2()));
    TlaExp left =
        TlaStdLibs.SubSeq(
            E1(), TlaIntLiteral(1), TlaSubtract(TlaStdLibs.Len(E1()), TlaIntLiteral(1)));
    TlaExp right = TlaStdLibs.SubSeq(E2(), TlaIntLiteral(2), TlaStdLibs.Len(E2()));
    TlaExp body = TlaConcatSeq(left, right);

    return new TlaDefn(decl, body, type);
  }

  private static TlaDefn dot() {

    //  (Set(Seq(Str)), Set(Seq(Str))) => Set(Seq(Str));
    // _dot(_R1,_R2) == {_dot_map(_e1,_e2) : <<_e1,_e2>> \in {<<_f1,_f2>> \in _R1 \X _R2 :
    // _dot_filter(_f1,_f2)}}

    SCType type = SnowCatTypes.OperatorTT2T(relationType());
    TlaDecl decl = TlaDecl(DOT_MACRO, Arrays.asList(R1(), R2()));
    TlaExp inner =
        TlaSetFilter(
            TlaQuantOpHeadTuple(Arrays.asList(F1(), F2()), TlaProductSet(R1(), R2())),
            TlaAppl(DOT_FILTER, Arrays.asList(F1(), F2())));
    TlaExp body =
        TlaSetMap(
            TlaQuantOpHeadTuple(Arrays.asList(E1(), E2()), inner),
            TlaAppl(DOT_MAP, Arrays.asList(E1(), E2())));

    return new TlaDefn(decl, body, type);
  }

  private static TlaDefn relational_override() {

    // (Set(Seq(Str)), Set(Seq(Str))) => Set(Seq(Str));
    // _relational_override(_R1,_R2) == (_R1 \ {_x \in _R1 : \E _y \in _R2 : (_x[1] = _y[1])})
    // \\union _R2

    SCType type = SnowCatTypes.OperatorTT2T(relationType());
    TlaDecl decl = TlaDecl(RELATIONAL_OVERRIDE, Arrays.asList(R1(), R2()));
    TlaExp set =
        TlaSetFilter(
            TlaQuantOpHead(X(), R1()),
            TlaExists(
                TlaQuantOpHead(Y(), R2()),
                X().INDEX(TlaIntLiteral(1)).EQUALS(Y().INDEX(TlaIntLiteral(1)))));
    TlaExp body = R1().DIFF(set).UNION(R2());
    return new TlaDefn(decl, body, type);
  }

  private static TlaDefn transpose() {
    // Set(<<Str,Str>>) => Set(<<Str,Str>>);
    // _transpose(R) == {<<y,x>> : <<x,y>> \in R}
    SCType type = SnowCatTypes.OperatorT2T(binaryRelationType());
    TlaDecl decl = TlaDecl(TRANSPOSE, Arrays.asList(R()));
    TlaExp body =
        TlaSetMap(
            TlaQuantOpHeadTuple(Arrays.asList(X(), Y()), R()), TlaTuple(Arrays.asList(Y(), X())));
    return new TlaDefn(decl, body, type);
  }

  private static TlaDefn none() {
    // _none == {}
    return TlaDefn(NONE, TlaNullSet());
  }

  private static TlaDefn iden() {
    // _iden = {x \o x : x \in _univ}
    return TlaDefn(IDEN, TlaSetMap(TlaQuantOpHead(X(), _UNIV()), TlaConcatSeq(X(), X())));
  }

  private static TlaDefn univ(List<TlaVar> setVars) {
    // _univ == A \\union B... where A, B... are top-level sigs
    return TlaDefn(UNIV, repeatedUnion(setVars));
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
    // _no(S)
    return new TlaDefn(TlaDecl(NO, Arrays.asList(S())), S().EQUALS(TlaNullSet()));
  }
}
