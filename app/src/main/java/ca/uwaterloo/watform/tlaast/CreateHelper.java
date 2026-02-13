package ca.uwaterloo.watform.tlaast;

import static ca.uwaterloo.watform.utils.GeneralUtil.foldLeft;

import ca.uwaterloo.watform.tlaast.tlabinops.*;
import ca.uwaterloo.watform.tlaast.tlaliterals.*;
import ca.uwaterloo.watform.tlaast.tlanaryops.*;
import ca.uwaterloo.watform.tlaast.tlaquantops.*;
import ca.uwaterloo.watform.tlaast.tlaquantops.TlaQuantOp.TlaQuantOpHead;
import ca.uwaterloo.watform.tlaast.tlaunops.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateHelper {

    // custom
    public static final TlaSet TlaNullSet() {
        return TlaSet(new ArrayList<>());
    }

    public static final TlaIntLiteral TlaZero() {
        return TlaIntLiteral(0);
    }

    public static TlaExp repeatedUnion(List<? extends TlaExp> operands) {
        int n = operands.size();
        if (n == 0) return TlaNullSet();
        return foldLeft(operands.subList(1, n), TlaUnionSet::new, operands.get(0));
    }

    public static TlaExp repeatedAnd(List<? extends TlaExp> operands) {
        if (operands.size() == 0) return TlaTrue();
        return TlaAndList(operands);
    }

    public static TlaExp repeatedAnd(TlaExp... operands) {
        return TlaAndList(Arrays.asList(operands));
    }

    public static TlaExp repeatedOr(TlaExp... operands) {
        return TlaOrList(Arrays.asList(operands));
    }

    public static TlaExp repeatedOr(List<? extends TlaExp> operands) {
        if (operands.size() == 0) return TlaFalse();
        return TlaOrList(operands);
    }

    // generic
    /*
    public static Tla Tla()
    {
    	return new Tla()
    }
    */
    public static TlaDefn TlaDefn(TlaDecl decl, TlaExp body) {
        return new TlaDefn(decl, body);
    }

    public static TlaDefn TlaDefn(String name, TlaExp body) {
        return new TlaDefn(new TlaDecl(name), body);
    }

    public static TlaDecl TlaDecl(String name) {
        return new TlaDecl(name);
    }

    public static TlaDecl TlaDecl(String name, List<TlaVar> params) {
        return new TlaDecl(name, params);
    }

    public static TlaAppl TlaAppl(String name) {
        return new TlaAppl(name);
    }

    public static TlaAppl TlaAppl(String name, List<? extends TlaExp> args) {
        return new TlaAppl(name, args);
    }

    // simple expressions
    /*
    public static Tla Tla(String name)
    {
    	return new Tla(name);
    }
    */
    public static TlaVar TlaVar(String name) {
        return new TlaVar(name);
    }

    public static TlaConst TlaConst(String name) {
        return new TlaConst(name);
    }

    // literals
    /*
    public static Tla Tla()
    {
    	return new Tla()
    }
    */
    public static TlaTrue TlaTrue() {
        return new TlaTrue();
    }

    public static TlaFalse TlaFalse() {
        return new TlaFalse();
    }

    public static TlaBoolean TlaBoolean() {
        return new TlaBoolean();
    }

    public static TlaStringLiteral TlaStringLiteral(String s) {
        return new TlaStringLiteral(s);
    }

    public static TlaIntLiteral TlaIntLiteral(int n) {
        return new TlaIntLiteral(n);
    }

    public static TlaIntSet TlaIntSet() {
        return new TlaIntSet();
    }

    public static TlaStringSet TlaStringSet() {
        return new TlaStringSet();
    }

    // unops
    /*
    public static Tla Tla(TlaExp op)
    {
    	return new Tla(op);
    }
    */
    public static TlaSubsetUnary TlaSubsetUnary(TlaExp op) {
        return new TlaSubsetUnary(op);
    }

    public static TlaUnionUnary TlaUnionUnary(TlaExp op) {
        return new TlaUnionUnary(op);
    }

    public static TlaFuncDomain TlaFuncDomain(TlaExp op) {
        return new TlaFuncDomain(op);
    }

    public static TlaNot TlaNot(TlaExp op) {
        return new TlaNot(op);
    }

    public static TlaPrime TlaPrime(TlaVar var) {
        return new TlaPrime(var);
    }

    // quantops
    /*
    public static Tla Tla(TlaVar var, TlaExp set, TlaExp exp)
    {
    	return new Tla(var,set,exp);
    }
    */

    public static TlaQuantOpHead TlaQuantOpHead(TlaVar var, TlaExp set) {
        return new TlaQuantOpHead(TlaQuantOpHead.Type.FLAT, Arrays.asList(var), set);
    }

    public static TlaQuantOpHead TlaQuantOpHeadFlat(List<TlaVar> vars, TlaExp set) {
        return new TlaQuantOpHead(TlaQuantOpHead.Type.FLAT, vars, set);
    }

    public static TlaQuantOpHead TlaQuantOpHeadTuple(List<TlaVar> vars, TlaExp set) {
        return new TlaQuantOpHead(TlaQuantOpHead.Type.TUPLE, vars, set);
    }

    public static TlaExists TlaExists(List<TlaQuantOpHead> heads, TlaExp exp) {
        return new TlaExists(heads, exp);
    }

    public static TlaExists TlaExists(TlaQuantOpHead head, TlaExp exp) {
        return new TlaExists(Arrays.asList(head), exp);
    }

    public static TlaForAll TlaForAll(List<TlaQuantOpHead> heads, TlaExp exp) {
        return new TlaForAll(heads, exp);
    }

    public static TlaForAll TlaForAll(TlaQuantOpHead head, TlaExp exp) {
        return new TlaForAll(Arrays.asList(head), exp);
    }

    public static TlaFuncMapConstr TlaFuncMapConstr(List<TlaQuantOpHead> heads, TlaExp exp) {
        return new TlaFuncMapConstr(heads, exp);
    }

    public static TlaFuncMapConstr TlaFuncMapConstr(TlaQuantOpHead head, TlaExp exp) {
        return new TlaFuncMapConstr(Arrays.asList(head), exp);
    }

    public static TlaSetFilter TlaSetFilter(List<TlaQuantOpHead> heads, TlaExp exp) {
        return new TlaSetFilter(heads, exp);
    }

    public static TlaSetFilter TlaSetFilter(TlaQuantOpHead head, TlaExp exp) {
        return new TlaSetFilter(Arrays.asList(head), exp);
    }

    public static TlaSetMap TlaSetMap(List<TlaQuantOpHead> heads, TlaExp exp) {
        return new TlaSetMap(heads, exp);
    }

    public static TlaSetMap TlaSetMap(TlaQuantOpHead head, TlaExp exp) {
        return new TlaSetMap(Arrays.asList(head), exp);
    }

    // binops
    /*
    public static Tla Tla(TlaExp op1, TlaExp op2)
    {
    	return new Tla(op1, op2);
    }
    */
    public static TlaAdd TlaAdd(TlaExp op1, TlaExp op2) {
        return new TlaAdd(op1, op2);
    }

    public static TlaAnd TlaAnd(TlaExp op1, TlaExp op2) {
        return new TlaAnd(op1, op2);
    }

    public static TlaConcatSeq TlaConcatSeq(TlaExp op1, TlaExp op2) {
        return new TlaConcatSeq(op1, op2);
    }

    public static TlaDiffSet TlaDiffSet(TlaExp op1, TlaExp op2) {
        return new TlaDiffSet(op1, op2);
    }

    public static TlaEquals TlaEquals(TlaExp op1, TlaExp op2) {
        return new TlaEquals(op1, op2);
    }

    public static TlaEquivalence TlaEquivalence(TlaExp op1, TlaExp op2) {
        return new TlaEquivalence(op1, op2);
    }

    public static TlaGreater TlaGreater(TlaExp op1, TlaExp op2) {
        return new TlaGreater(op1, op2);
    }

    public static TlaGreaterEq TlaGreaterEq(TlaExp op1, TlaExp op2) {
        return new TlaGreaterEq(op1, op2);
    }

    public static TlaImplies TlaImplies(TlaExp op1, TlaExp op2) {
        return new TlaImplies(op1, op2);
    }

    public static TlaIndexing TlaIndexing(TlaExp op1, TlaExp op2) {
        return new TlaIndexing(op1, op2);
    }

    public static TlaInSet TlaInSet(TlaExp op1, TlaExp op2) {
        return new TlaInSet(op1, op2);
    }

    public static TlaNotInSet TlaNotInSet(TlaExp op1, TlaExp op2) {
        return new TlaNotInSet(op1, op2);
    }

    public static TlaIntersectionSet TlaIntersectionSet(TlaExp op1, TlaExp op2) {
        return new TlaIntersectionSet(op1, op2);
    }

    public static TlaLesser TlaLesser(TlaExp op1, TlaExp op2) {
        return new TlaLesser(op1, op2);
    }

    public static TlaLesserEq TlaLesserEq(TlaExp op1, TlaExp op2) {
        return new TlaLesserEq(op1, op2);
    }

    public static TlaMult TlaMult(TlaExp op1, TlaExp op2) {
        return new TlaMult(op1, op2);
    }

    public static TlaNotEq TlaNotEq(TlaExp op1, TlaExp op2) {
        return new TlaNotEq(op1, op2);
    }

    public static TlaOr TlaOr(TlaExp op1, TlaExp op2) {
        return new TlaOr(op1, op2);
    }

    public static TlaProductSet TlaProductSet(TlaExp op1, TlaExp op2) {
        return new TlaProductSet(op1, op2);
    }

    public static TlaRange TlaRange(TlaExp op1, TlaExp op2) {
        return new TlaRange(op1, op2);
    }

    public static TlaSubsetEq TlaSubsetEq(TlaExp op1, TlaExp op2) {
        return new TlaSubsetEq(op1, op2);
    }

    public static TlaSubtract TlaSubtract(TlaExp op1, TlaExp op2) {
        return new TlaSubtract(op1, op2);
    }

    public static TlaUnionSet TlaUnionSet(TlaExp op1, TlaExp op2) {
        return new TlaUnionSet(op1, op2);
    }

    // n-ary ops
    /*
    public static Tla Tla(List<? extends TlaExp> children)
    {
    	return new Tla(children);
    }
    */
    public static TlaAndList TlaAndList(List<? extends TlaExp> children) {
        return new TlaAndList(children);
    }

    public static TlaOrList TlaOrList(List<? extends TlaExp> children) {
        return new TlaOrList(children);
    }

    public static TlaSeq TlaSeq(List<? extends TlaExp> children) {
        return new TlaSeq(children);
    }

    public static TlaSeq TlaSeq(TlaExp... children) {
        return new TlaSeq(Arrays.asList(children));
    }

    public static TlaSet TlaSet(List<? extends TlaExp> children) {
        return new TlaSet(children);
    }

    public static TlaSet TlaSet(TlaExp... children) {
        return new TlaSet(Arrays.asList(children));
    }

    public static TlaTuple TlaTuple(List<? extends TlaExp> children) {
        return new TlaTuple(children);
    }

    public static TlaTuple TlaTuple(TlaExp... children) {
        return new TlaTuple(Arrays.asList(children));
    }

    public static TlaUnchanged TlaUnchanged(List<? extends TlaVar> children) {
        return new TlaUnchanged(children);
    }

    public static TlaUnchanged TlaUnchanged(TlaVar... children) {
        return new TlaUnchanged(Arrays.asList(children));
    }

    // misc
}
