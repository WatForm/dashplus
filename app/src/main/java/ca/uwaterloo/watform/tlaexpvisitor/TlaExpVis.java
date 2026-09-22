package ca.uwaterloo.watform.tlaexpvisitor;

import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaConst;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaIfThenElse;
import ca.uwaterloo.watform.tlaast.TlaLetBinding;
import ca.uwaterloo.watform.tlaast.TlaOperator;
import ca.uwaterloo.watform.tlaast.TlaRecord;
import ca.uwaterloo.watform.tlaast.TlaStdLibs;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlaast.tlabinops.*;
import ca.uwaterloo.watform.tlaast.tlanaryops.*;
import ca.uwaterloo.watform.tlaast.tlaquantops.*;
import ca.uwaterloo.watform.tlaast.tlaunops.*;

public interface TlaExpVis<T> {

  public default T visit(TlaExp exp) {
    return exp.accept(this);
  }

  // Abstract ones that need implementation in extensions


  public default T visit(TlaBinOp binOpExp) {
    return visit((TlaOperator) binOpExp);
  }

  public default T visit(TlaUnaryOp unaryOpExp) {
    return visit((TlaOperator) unaryOpExp);
  }

  public default T visit(TlaQuantOp quantOpExp) {
    return visit((TlaOperator) quantOpExp);
  }

  public default T visit(TlaNaryOp NaryOpExp) {
    return visit((TlaOperator) NaryOpExp);
  }


  // default implementations

  // binary
  public default T visit(TlaAdd exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaAnd exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaConcatSeq exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaDiffSet exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaDot exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaEquals exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaEquivalence exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaGreater exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaGreaterEq exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaImplies exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaIndexing exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaInSet exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaIntersectionSet exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaLesser exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaLesserEq exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaMult exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaNotEq exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaNotInSet exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaOr exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaProductSet exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaRange exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaSubsetEq exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaSubtract exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaUnionSet exp) {
    return visit((TlaBinOp) exp);
  }

  public default T visit(TlaFuncDomain exp) {
    return visit((TlaUnaryOp) exp);
  }

  public default T visit(TlaNot exp) {
    return visit((TlaUnaryOp) exp);
  }

  public default T visit(TlaPrime exp) {
    return visit((TlaUnaryOp) exp);
  }

  public default T visit(TlaSubsetUnary exp) {
    return visit((TlaUnaryOp) exp);
  }

  public default T visit(TlaUnionUnary exp) {
    return visit((TlaUnaryOp) exp);
  }

  public default T visit(TlaExists exp) {
    return visit((TlaQuantOp) exp);
  }

  public default T visit(TlaForAll exp) {
    return visit((TlaQuantOp) exp);
  }

  public default T visit(TlaFuncMapConstr exp) {
    return visit((TlaQuantOp) exp);
  }

  public default T visit(TlaSetFilter exp) {
    return visit((TlaQuantOp) exp);
  }

  public default T visit(TlaSetMap exp) {
    return visit((TlaQuantOp) exp);
  }

  public default T visit(TlaSet exp) {
    return visit((TlaNaryOp) exp);
  }

  public default T visit(TlaSeq exp) {
    return visit((TlaNaryOp) exp);
  }

  public default T visit(TlaTuple exp) {
    return visit((TlaNaryOp) exp);
  }

  public default T visit(TlaUnchanged exp) {
    return visit((TlaNaryOp) exp);
  }

  T visit(TlaOperator OperatorExp);

  T visit(TlaAppl ApplExp);

  T visit(TlaConst ConstExp);

  T visit(TlaDefn DefnExp);

  T visit(TlaIfThenElse ifThenElseExp);

  T visit(TlaLetBinding letBindingExp);

  T visit(TlaRecord recordExp);

  T visit(TlaStdLibs stdLibExp);

  T visit(TlaVar varExp);
}
