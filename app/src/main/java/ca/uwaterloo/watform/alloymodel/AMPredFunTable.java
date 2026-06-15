/*
    Predicates and Functions are stored in the same table
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.alloymodel.PredFunData.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;
import static ca.uwaterloo.watform.utils.ImplementationError.nullField;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyFunPara;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPredPara;
import ca.uwaterloo.watform.utils.*;
import java.util.*;
import java.util.function.BiFunction;

public class AMPredFunTable extends AMSigTable {

    private Map<String, PredFunData> predFunTable = new LinkedHashMap<>();

    private void loadBuiltinPredFuns() {
        // adding in builtin preds/fun
        // is a hack until imports work

        /*
        // fun removeFirst [s: seq univ] : seq univ
        this.predFunTable.put(
                "removeFirst",
                FunData(
                        "removeFirst",
                        List.of(parseDecl("s: seq univ")),
                        // AlloySeqDecl("s", new AlloyUnivExpr())),
                        parseExpr("seq univ")));
        // fun firstElem [s: seq univ] : set univ
        this.predFunTable.put(
                "firstElem",
                FunData(
                        "firstElem",
                        List.of(AlloySeqDecl("s", new AlloyUnivExpr())),
                        parseExpr("seq univ")));
        // fun delete [s: seq univ, i: Int] : seq univ
        this.predFunTable.put(
                "delete",
                FunData(
                        "delete",
                        List.of(
                                AlloySeqDecl("s", new AlloyUnivExpr()),
                                AlloyDecl("i", AlloyQtEnum.ONE, new AlloySigIntExpr())),
                        parseExpr("seq univ")));
        // fun idxOf [s: Int -> univ, e: univ] : lone Int
        this.predFunTable.put(
                "idxOf",
                FunData(
                        "idxOf",
                        List.of(
                                AlloySeqDecl(
                                        "s", AlloyArrow(new AlloyIntExpr(), new AlloyUnivExpr())),
                                AlloyDecl("e", AlloyQtEnum.ONE, new AlloyUnivExpr())),
                        new AlloyQtExpr(AlloyQtEnum.LONE, new AlloyIntExpr())));
        */
    }

    protected AMPredFunTable(AlloyFile alloyFile) {
        super(alloyFile);
        loadBuiltinPredFuns();
    }

    protected AMPredFunTable(AMPredFunTable other) {
        super(other);
        this.predFunTable = new LinkedHashMap<>(other.predFunTable);
        loadBuiltinPredFuns();
    }

    protected void resolve(
            BiFunction<AlloyExpr, Optional<String>, CalcAritySetMulDefaultsExprVis.Result>
                    arityAndSetMul) {
        super.resolve(arityAndSetMul);
        // determine the arites and set multiplicities
        // within args/results of predFunTable
        for (String name : this.predFunTable.keySet()) {
            for (PredFunData.ArgInfo argInfo : this.predFunTable.get(name).argInfoList) {
                // have to check the whole decl
                // because "a: seq X" is ("a", SEQ, "X")
                // i.e. the mul of "SEQ" is in the decl not the expr
                // of the decl
                CalcAritySetMulDefaultsExprVis.Result argResult =
                        arityAndSetMul.apply(argInfo.decl, Optional.empty());
                // System.out.println(argInfo.decl);
                // System.out.println(argResult.arity);
                // changes these values in place
                // argInfo.decl = argInfo.decl.rebuild(argResult.exp);
                if (argResult.arity.isPresent()) {
                    argInfo.arity = argResult.arity;
                } else {
                    throw AlloyModelError.unknownArity(argResult.exp.pos, argResult.exp.toString());
                }
            }
            Optional<PredFunData.ResultInfo> resultInfo = this.predFunTable.get(name).resultInfo;
            if (resultInfo.isPresent()) {
                CalcAritySetMulDefaultsExprVis.Result resultResult =
                        arityAndSetMul.apply(resultInfo.get().expr, Optional.empty());
                resultInfo.get().expr = resultResult.exp;
                resultInfo.get().arity = resultResult.arity;
                this.predFunTable.get(name).resultInfo = resultInfo;
            }
        }
        // TODO: checking if predicate calling is in a cycle
    }

    public List<String> allPreds() {
        return filterBy(
                setToList(this.predFunTable.keySet()), i -> this.predFunTable.get(i).isPred());
    }

    public List<String> allFuns() {
        return filterBy(
                setToList(this.predFunTable.keySet()), i -> this.predFunTable.get(i).isFun());
    }

    public void entryPred(Pos p, String predName, List<AlloyDecl> argDeclList) {
        // System.out.println(this.allPreds());
        // System.out.println(this.allSigs());
        if (this.allPreds().contains(predName)
                || this.allFields().contains(predName)
                || this.allSigs().contains(predName))
            throw AlloyModelError.duplicatePredName(p, predName);
        else this.predFunTable.put(predName, PredData(predName, argDeclList));
    }

    public void entryFun(Pos p, String funName, List<AlloyDecl> argDeclList, AlloyExpr resultExpr) {
        reqNonNull(nullField(p, this), resultExpr);

        // System.out.println(this.allPreds());
        // System.out.println(this.allSigs());
        if (this.allPreds().contains(funName)
                || this.allFields().contains(funName)
                || this.allSigs().contains(funName))
            throw AlloyModelError.duplicatePredName(p, funName);
        else this.predFunTable.put(funName, FunData(funName, argDeclList, resultExpr));
    }

    public boolean isPred(String predName) {
        return this.allPreds().contains(predName);
    }

    public boolean isFun(String funName) {
        return this.allFuns().contains(funName);
    }

    /*
    public Integer predArity(String predName) {
        if (this.allPreds().contains(predName)) {
            return this.predFunTable.get(predName).arity;
        } else throw AlloyModelImplError.predNotFound(predName);
    }
    */

    public void addPredToPredFunTable(AlloyPredPara predPara) {
        String predName = predPara.getName();
        // System.out.println("Adding to pred table: " + predName + Integer.toString(numArgs + 1));
        // one decl can be a,b,c:X
        entryPred(
                predPara.pos, predName, flatten(mapBy(predPara.arguments, decl -> decl.expand())));
    }

    public void addFunToPredFunTable(AlloyFunPara funPara) {
        String funName = funPara.getName();
        // System.out.println("Adding to pred table: " + predName + Integer.toString(numArgs + 1));
        entryPred(funPara.pos, funName, flatten(mapBy(funPara.arguments, decl -> decl.expand())));
    }

    public void removePredFromPredFunTable(String predName) {
        // this won't work well for overloading
        if (!this.allPreds().contains(predName)) {
            throw AlloyModelImplError.predNotFound(predName);
        } else {
            this.predFunTable.remove(predName);
        }
    }

    public Integer numArgs(String predFunName) {
        if (this.isPred(predFunName) || this.isFun(predFunName)) {
            return this.predFunTable.get(predFunName).argInfoList.size();
        } else {
            throw AlloyModelImplError.tryingToAccessNonExistentPredFun(predFunName);
        }
    }

    public Optional<Integer> predFunTableReturnArity(String predFunName) {
        if (this.isPred(predFunName)) {
            return Builtins.ONE_ARITY;
        } else if (this.isFun(predFunName)) {
            return this.predFunTable.get(predFunName).resultInfo.get().arity;
        } else {
            throw AlloyModelImplError.tryingToAccessNonExistentPredFun(predFunName);
        }
    }

    public List<Optional<Integer>> predFunTableArgsArities(String predFunName) {
        if (this.isPred(predFunName) || this.isFun(predFunName)) {
            return mapBy(this.predFunTable.get(predFunName).argInfoList, a -> a.arity);
        } else {
            throw AlloyModelImplError.tryingToAccessNonExistentPredFun(predFunName);
        }
    }
}
