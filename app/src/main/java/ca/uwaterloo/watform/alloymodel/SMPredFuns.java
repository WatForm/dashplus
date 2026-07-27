/*
    Predicates and Functions are stored in the same table
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.alloymodel.PredFunData.*;
import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.alloymodel.ResolveInfo.*;
import static ca.uwaterloo.watform.parser.Parser.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;
import static ca.uwaterloo.watform.utils.ImplementationError.nullField;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.exprvisitor.CollectExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class SMPredFuns extends SMFields {

    private Map<Qname, List<PredFunData>> predFunTable = new LinkedHashMap<>();

    // init --------

    protected SMPredFuns() {}

    protected SMPredFuns(SMPredFuns other) {
        super(other);
        this.predFunTable = new LinkedHashMap<>(other.predFunTable);
    }

    public void createPred(Pos p, Qname predName, List<AlloyDecl> argDeclList, AlloyExpr body) {
        reqNonNull(nullField(p, this), predName, argDeclList, body);
        this.predFunTable
                .computeIfAbsent(predName, k -> new ArrayList())
                .add(PredData(p, argDeclList, body));
    }

    public void createFun(
            Pos p,
            Qname funName,
            List<AlloyDecl> argDeclList,
            AlloyExpr resultExpr,
            AlloyExpr body) {
        reqNonNull(nullField(p, this), funName, argDeclList, resultExpr, body);
        this.predFunTable
                .computeIfAbsent(funName, k -> new ArrayList())
                .add(FunData(p, argDeclList, resultExpr, body));
    }

    /*
    private void loadBuiltinPredFuns() {
        // adding in builtin preds/fun
        // will be only a few pred/totalOrder, fun/sum, etc


        // fun removeFirst [s: seq univ] : seq univ
        this.predFunTable.put(
                thisQname("removeFirst"),
                FunData("removeFirst", List.of(parseDecl("s: seq univ")), parseExpr("seq univ")));
        // fun firstElem [s: seq univ] : set univ
        this.predFunTable.put(
                thisQname("firstElem"),
                FunData("firstElem", List.of(parseDecl("s: seq univ")), parseExpr("seq univ")));
        // fun delete [s: seq univ, i: Int] : seq univ
        this.predFunTable.put(
                thisQname("delete"),
                FunData(
                        "delete",
                        List.of(parseDecl("s: seq univ"), parseDecl("i: one Int")),
                        parseExpr("seq univ")));
        // fun idxOf [s: Int -> univ, e: univ] : lone Int
        this.predFunTable.put(
                thisQname("idxOf"),
                FunData(
                        "idxOf",
                        List.of(parseDecl("s: set Int set->set univ"), parseDecl("e: one univ")),
                        parseExpr("lone Int")));

    }
    */

    // resolve ----------------------

    private Set<Qname> collectUsedPredFuns(AlloyExpr expr) {
        if (expr instanceof AlloyQnameExpr) {
            List<Qname> possibleMatches =
                    this.predFunQnameMatches(unknownQname(((AlloyQnameExpr) expr).getName()));
            return listToSet(possibleMatches);
        } else {
            return emptySet();
        }
    }

    private Set<Qname> resolved = new HashSet<>();
    private Set<Qname> visiting = new HashSet<>();
    private TriFunction<AlloyExpr, String, Optional<String>, ResolveInfo> resolve1;
    private TriFunction<AlloyExpr, String, List<AlloyDecl>, ResolveInfo> resolve2;

    // have to put them in def-use order
    private CollectExprVis<Qname> getDepsVis = new CollectExprVis<>(this::collectUsedPredFuns);

    protected void resolveSMPredFuns(
            TriFunction<AlloyExpr, String, Optional<String>, ResolveInfo> resolve1,
            TriFunction<AlloyExpr, String, List<AlloyDecl>, ResolveInfo> resolve2) {
        this.resolved = new HashSet<>();
        this.visiting = new HashSet<>();
        this.resolve1 = resolve1;
        this.resolve2 = resolve2;

        // KENG: I'm not sure how this working in def-use order will
        // work with dep analysis

        // do this loop first to make sure there is no overloading left
        for (Qname qname : this.predFunTable.keySet()) {
            // list of items for each qname

            if (this.predFunTable.get(qname).size() != 1) {
                PredFunData first = this.predFunTable.get(qname).get(0);
                throw AlloyModelError.overloadingPredFunNotSupported(first.pos);
            }
        }

        // now start the actual resolving
        for (Qname qname : this.predFunTable.keySet()) {
            this.resolvePredFunData(qname);
        }
    }

    protected void resolvePredFunData(Qname qname) {

        if (this.resolved.contains(qname)) return;
        if (visiting.contains(qname))
            throw AlloyModelError.recursivePredFunDependency(qname.toString());

        visiting.add(qname);

        PredFunData predFunData = this.predFunTable.get(qname).get(0);
        for (Qname dep : getDepsVis.visit(predFunData.body)) {
            // recursive call
            this.resolvePredFunData(dep);
        }

        // now to the resolution of the predFunData
        // determine the arites and set multiplicities
        // within args/returntype/body

        // 1) resolve the args by themselves and record their arities
        // these work with pointers
        for (PredFunData.ArgInfo argInfo : predFunData.argInfoList) {
            // have to check the whole decl
            // because "a: seq X" is ("a", SEQ, "X")
            // i.e. the mul of "SEQ" is in the decl not the expr
            // of the decl
            ResolveInfo argResolveInfo =
                    resolve1.apply(argInfo.decl, qname.nameSpace, Optional.empty());
            if (argResolveInfo.arity.isPresent()) {
                // put it back in the table
                argInfo.decl = (AlloyDecl) argResolveInfo.exp;
                argInfo.arity = argResolveInfo.arity;
            } else {
                throw AlloyModelError.unknownArity(
                        argResolveInfo.exp.pos, argResolveInfo.exp.toString());
            }
        }
        // System.out.println(this.predFunTable.get(qname));

        // 2) resolve the body of the pred/fun defn
        // args should have arity now
        ResolveInfo bodyResolveInfo =
                resolve2.apply(
                        predFunData.body,
                        qname.nameSpace,
                        // push the resolved args into the local context
                        mapBy(predFunData.argInfoList, x -> x.decl));
        if (bodyResolveInfo.arity.isPresent()) {
            predFunData.body = bodyResolveInfo.exp;
        } else {
            throw AlloyModelError.unknownArity(
                    bodyResolveInfo.exp.pos, bodyResolveInfo.exp.toString());
        }

        // 3) resolve the returnType by itself and record its arity
        Optional<PredFunData.ResultInfo> resultInfo = predFunData.resultInfo;

        if (resultInfo.isPresent()) {
            ResolveInfo resultResolveInfo =
                    resolve1.apply(resultInfo.get().expr, qname.nameSpace, Optional.empty());
            // put it back in the table
            if (resultResolveInfo.arity.isPresent()) {
                predFunData.resultInfo =
                        Optional.of(
                                new PredFunData.ResultInfo(
                                        resultResolveInfo.exp, resultResolveInfo.arity));
                // 4a) check body arity matches return type arity
                if (bodyResolveInfo.arity.get() != resultResolveInfo.arity.get()) {
                    throw AlloyModelError.arityMismatchReturnType(
                            predFunData.body.pos,
                            resultResolveInfo.arity.get(),
                            bodyResolveInfo.arity.get());
                }
            } else {
                throw AlloyModelError.unknownArity(
                        resultResolveInfo.exp.pos, resultResolveInfo.exp.toString());
            }

        } else {
            // it is a predicate
            // 4b) check body arity matches return type arity
            if (bodyResolveInfo.arity.get() != 1) {
                throw AlloyModelError.arityMismatchReturnType(
                        predFunData.body.pos, 1, bodyResolveInfo.arity.get());
            }
        }

        predFunData.isResolved = true;
        this.visiting.remove(qname);
        this.resolved.add(qname);
    }

    // lookups -------------------------------

    public List<Qname> predFunQnameMatches(Qname qname) {
        return this.predFunTable.keySet().stream()
                .filter(
                        q ->
                                q.name.equals(qname.name)
                                        & (q.nameSpace.equals(qname.nameSpace)
                                                || qname.nameSpace.equals(UNKNOWN_NAMESPACE)))
                .toList();
    }

    public List<Qname> funQnameMatches(Qname qname) {
        return filterBy(
                this.predFunQnameMatches(qname),
                q -> this.predFunTable.get(q).stream().anyMatch(j -> j.resultInfo.isPresent()));
    }

    public List<Qname> predQnameMatches(Qname qname) {
        return filterBy(
                this.predFunQnameMatches(qname),
                q -> this.predFunTable.get(q).stream().anyMatch(j -> !j.resultInfo.isPresent()));
    }

    // matches something, possibly more than one item
    public boolean isPredFun(Qname qname) {
        return !predFunQnameMatches(qname).isEmpty();
    }

    // matches something, possibly more than one item
    public boolean isPred(Qname qname) {
        return !predQnameMatches(qname).isEmpty();
    }

    // matches something, possibly more than one item
    public boolean isFun(Qname qname) {
        return !funQnameMatches(qname).isEmpty();
    }

    protected List<AlloyDecl> predFunArgDecls(Qname qname) {
        if (this.isPred(qname) || this.isFun(qname)) {
            // KENG TODO: I'm just returning the first match here
            // in both qname and in what matches qname
            // there are two get(0)'s below
            Qname chosen = this.predFunQnameMatches(qname).get(0);
            return mapBy(this.predFunTable.get(chosen).get(0).argInfoList, a -> a.decl);
        } else {
            // arity visitor determines if this is an error
            return emptyList();
        }
    }

    protected List<Optional<Integer>> predFunArgArities(Qname qname) {
        if (this.isPred(qname) || this.isFun(qname)) {
            // KENG TODO: I'm just returning the first match here
            // in both qname and in what matches qname
            // there are two get(0)'s below
            Qname chosen = this.predFunQnameMatches(qname).get(0);
            return mapBy(this.predFunTable.get(chosen).get(0).argInfoList, a -> a.arity);
        } else {
            // arity visitor determines if this is an error
            return emptyList();
        }
    }

    protected Optional<Integer> predFunReturnArity(Qname qname) {
        // KENG TODO: I'm just returning the first match here
        // in both qname and in what matches qname
        // there are two get(0)'s below
        if (this.isPred(qname)) {
            return ONE_ARITY;
        } else if (this.isFun(qname)) {
            Qname chosen = this.predFunQnameMatches(qname).get(0);
            return this.predFunTable.get(chosen).get(0).resultInfo.get().arity;
        } else {
            // arity visitor determines if this is an error
            return Optional.empty();
        }
    }

    private void exists(Qname qname) {
        if (predFunQnameMatches(qname).isEmpty())
            throw AlloyModelImplError.predFunNotFound(qname.toString());
    }

    // KENG: I'm not sure what info you want about args and return
    // types when overloading is possible
    // we can't just get args separately from return type
    // because they come as a pair

    // accessors ----------------------

    public List<Qname> allPreds() {
        return filterBy(setToList(this.predFunTable.keySet()), i -> this.isPred(i));
    }

    public List<Qname> allFuns() {
        return filterBy(setToList(this.predFunTable.keySet()), i -> this.isFun(i));
    }

    public void debugSMPredFuns() {
        StringBuilder sb = new StringBuilder("SMPredFuns:");

        predFunTable.forEach(
                (qname, list) -> sb.append("\n  ").append(qname).append(" -> ").append(list));

        System.out.println(sb.toString() + "\n");
    }

    /*
    public void removePredFromPredFunTable(Qname predName) {
        // this won't work well for overloading
        if (!this.allPreds().contains(predName)) {
            throw AlloyModelImplError.predNotFound(predName);
        } else {
            this.predFunTable.remove(predName);
        }
    }
    */

    /*
    // must be exact qname
    private void existsPredFun(Qname predFunName) {
        if (isUnknownNameSpace(predFunName) || (!this.isPred(predFunName) && !this.isFun(predFunName))) {
            throw AlloyModelImplError.tryingToAccessNonExistentPredFun(predFunName);
        }
    }

    public Integer numArgs(Qname predFunName) {
        existsPredFun(predFunName);
        return this.predFunTable.get(predFunName).argInfoList.size();
    }
    */

}
