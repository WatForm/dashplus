package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import java.util.*;

public class PredFcnData {

    public class ExprInfo {
        public AlloyExpr expr;
        public Optional<Integer> arity;

        ExprInfo(AlloyExpr expr, Optional<Integer> arity) {
            this.expr = expr;
            this.arity = arity;
        }
    }

    public String name;
    protected List<ExprInfo> args = emptyList();
    protected Optional<ExprInfo> result;

    private PredFcnData(String name, List<AlloyExpr> argsExprList, AlloyExpr resultExpr) {
        this.name = name;
        for (AlloyExpr expr : argsExprList) {
            this.args.add(new ExprInfo(expr, Optional.empty()));
        }
        this.result = Optional.of(new ExprInfo(resultExpr, Optional.empty()));
    }

    public Integer numArgs() {
        return args.size();
    }

    public List<AlloyExpr> argExprs() {
        return mapBy(this.args, a -> a.expr);
    }

    public Boolean isPred() {
        return !this.result.isPresent();
    }

    public Boolean isFcn() {
        return this.result.isPresent();
    }

    public AlloyExpr resultExpr() {
        if (!this.isFcn()) throw AlloyModelImplError.fcnNotFound(this.name);
        else return this.result.get().expr;
    }

    public void setArgArity(Integer index, Integer arity) {
        this.args.get(index).arity = Optional.of(arity);
    }

    public void setResultArity(Integer arity) {
        this.result.get().arity = Optional.of(arity);
    }
}
