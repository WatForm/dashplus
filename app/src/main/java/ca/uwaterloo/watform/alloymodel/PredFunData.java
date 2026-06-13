package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;
import static ca.uwaterloo.watform.utils.ImplementationError.nullField;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.utils.Pos;
import java.util.*;

public class PredFunData {

    public class ArgInfo {
        public AlloyDecl decl;
        public Optional<Integer> arity;

        ArgInfo(AlloyDecl decl, Optional<Integer> arity) {
            this.decl = decl;
            this.arity = arity;
        }
    }

    public class ResultInfo {
        public AlloyExpr expr;
        public Optional<Integer> arity;

        ResultInfo(AlloyExpr expr, Optional<Integer> arity) {
            this.expr = expr;
            this.arity = arity;
        }
    }

    public String name;
    protected List<ArgInfo> argInfoList = emptyList();
    protected Optional<ResultInfo> resultInfo;

    private PredFunData(
            String name, List<AlloyDecl> argsDeclList, Optional<AlloyExpr> optionalResultExpr) {
        this.name = name;
        for (AlloyDecl decl : argsDeclList) {
            this.argInfoList.add(new ArgInfo(decl, Builtins.UNKNOWN_ARITY));
        }
        if (optionalResultExpr.isPresent())
            this.resultInfo =
                    Optional.of(new ResultInfo(optionalResultExpr.get(), Builtins.UNKNOWN_ARITY));
        else this.resultInfo = Optional.empty();
    }

    public static PredFunData PredData(String name, List<AlloyDecl> argsDeclList) {
        // Boolean return value
        return new PredFunData(name, argsDeclList, Optional.empty());
    }

    public static PredFunData FunData(
            String name, List<AlloyDecl> argsDeclList, AlloyExpr resultExpr) {
        reqNonNull(nullField(Pos.UNKNOWN, resultExpr), resultExpr);
        return new PredFunData(name, argsDeclList, Optional.of(resultExpr));
    }

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

    /*
    public void setArgArity(Integer index, Integer arity) {
        this.args.get(index).arity = Optional.of(arity);
    }

    public void setResultArity(Integer arity) {
        this.result.get().arity = Optional.of(arity);
    }
    */
}
