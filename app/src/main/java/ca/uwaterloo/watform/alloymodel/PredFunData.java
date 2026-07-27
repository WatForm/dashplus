package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.alloymodel.ResolveInfo.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;
import static ca.uwaterloo.watform.utils.ImplementationError.nullField;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.utils.Pos;
import java.util.*;

public class PredFunData {

    // arity is not initialized as UNKNOWN on creation
    // b/c we may be creating these in resolve
    public static class ArgInfo {
        public AlloyDecl decl;
        public Optional<Integer> arity;

        ArgInfo(AlloyDecl decl, Optional<Integer> arity) {
            this.decl = decl;
            this.arity = arity;
        }

        @Override
        public String toString() {
            return "{" + decl + "(" + arity.map(a -> Integer.toString(a)).orElse("?") + ")}";
        }
    }

    public static class ResultInfo {
        public AlloyExpr expr;
        public Optional<Integer> arity;

        ResultInfo(AlloyExpr expr, Optional<Integer> arity) {
            this.expr = expr;
            this.arity = arity;
        }

        @Override
        public String toString() {
            return "{" + expr + "(" + arity.map(a -> Integer.toString(a)).orElse("?") + ")}";
        }
    }

    protected Pos pos;
    protected List<ArgInfo> argInfoList = emptyList();
    protected Optional<ResultInfo> resultInfo;
    public AlloyExpr body;
    public Boolean isResolved = false;

    private PredFunData(
            Pos p,
            List<AlloyDecl> argsDeclList,
            Optional<AlloyExpr> optionalResultExpr,
            AlloyExpr body) {

        for (AlloyDecl decl : argsDeclList) {
            this.argInfoList.add(new ArgInfo(decl, UNKNOWN_ARITY));
        }
        if (optionalResultExpr.isPresent())
            this.resultInfo = Optional.of(new ResultInfo(optionalResultExpr.get(), UNKNOWN_ARITY));
        else this.resultInfo = Optional.empty();
        this.body = body;
        this.pos = p;
    }

    public static PredFunData PredData(Pos p, List<AlloyDecl> argsDeclList, AlloyExpr body) {
        // Boolean return value
        return new PredFunData(p, argsDeclList, Optional.empty(), body);
    }

    public static PredFunData FunData(
            Pos p, List<AlloyDecl> argsDeclList, AlloyExpr resultExpr, AlloyExpr body) {
        reqNonNull(nullField(Pos.UNKNOWN, resultExpr), resultExpr);
        return new PredFunData(p, argsDeclList, Optional.of(resultExpr), body);
    }

    @Override
    public String toString() {
        return argInfoList + ", " + resultInfo + ", " + body + " (resolved=" + isResolved + ")";
    }

    /*
    public Integer numArgs() {
        return argInfoList.size();
    }

    public List<AlloyDecl> argDecls() {
        return mapBy(this.argInfoList, a -> a.decl);
    }

    public Boolean isPred() {
        return !this.resultInfo.isPresent();
    }

    public Boolean isFun() {
        return this.resultInfo.isPresent();
    }

    public AlloyExpr resultExpr() {
        if (!this.isFun()) throw AlloyModelImplError.fcnNotFound(this.name);
        else return this.resultInfo.get().expr;
    }

    public AlloyExpr body() {
        return this.body;
    }

    /*
    public void setArgArity(Integer index, Integer arity) {
        this.args.get(index).arity = Optional.of(arity);
    }

    public void setResultArity(Integer arity) {
        this.result.get().arity = Optional.of(arity);
    }
    */
}
