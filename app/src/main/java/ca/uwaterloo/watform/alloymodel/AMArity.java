package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.Builtins.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPara;
import ca.uwaterloo.watform.paravisitor.TestAndReplaceExprParaVis;
import java.util.*;

public class AMArity extends AMPredFunTable {

    protected AMArity(AlloyFile alloyFile) {
        super(alloyFile);
    }

    protected AMArity(AMArity other) {
        super(other);
    }

    protected void resolve() {
        // arityAndSetMul is not available to AMs below this in the hierarchy
        super.resolve(this::fieldArityAndSetMul);
    }

    // must go before arityAndMulCalcVis private attritube
    // if sigParent has a value, we must be checking a field bounding expression
    // if the symbolName has a parent == sigParent, then that's allowed
    // but the arity of the symbol is reduced by 1
    protected Optional<Integer> sigFieldArity(String symbolName, Optional<String> sigParent) {
        // enums will be in allSigs
        // System.out.println("symbolArity " + symbolName);
        if (this.allSigs().contains(symbolName)) {
            return Optional.of(1);
        } else if (this.allFields().contains(symbolName)) {
            // if symbol has same parent sig as the field we are checking
            // it is allowed but the arity is not -1 the arity returned from the fieldTable
            if (sigParent.isPresent())
                // make sure the symbol has the same
                if (!this.fieldParent(symbolName).equals(sigParent.get())) {
                    throw AlloyModelError.cannotRefFieldInBoundingExprOutsideOfItsSig(
                            symbolName, sigParent.get());
                } else {
                    // implicitly the sigParent is already joined to the fieldName so we subtract 1
                    // if the field name has
                    // an arity
                    return this.fieldArity(symbolName).map(b -> b - 1);
                }
            else {
                return this.fieldArity(symbolName);
            }
        } else if (Builtins.isBuiltin(symbolName)) {
            // TODO: this may change once we can read imports
            return Optional.of(Builtins.builtinArity(symbolName));
        } else {
            // arity visitor determines if this is an error
            return Optional.empty();
        }
    }

    protected Optional<Integer> predFunReturnArity(String symbolName) {
        if (this.isPred(symbolName) || this.isFun(symbolName)) {
            return this.predFunTableReturnArity(symbolName);
        } else {
            // arity visitor determines if this is an error
            return Optional.empty();
        }
    }

    protected List<Optional<Integer>> predFunArgsArities(String symbolName) {
        if (this.isPred(symbolName) || this.isFun(symbolName)) {
            return this.predFunTableArgsArities(symbolName);
        } else {
            // arity visitor determines if this is an error
            return emptyList();
        }
    }

    // same visitor object used everywhere
    private CalcAritySetMulDefaultsExprVis arityAndMulCalcVis =
            new CalcAritySetMulDefaultsExprVis(
                    this::sigFieldArity, this::predFunReturnArity, this::predFunArgsArities);

    // this is used when we are getting back arity for the fieldTable
    CalcAritySetMulDefaultsExprVis.Result fieldArityAndSetMul(
            AlloyExpr e, Optional<String> sigParent) {
        return arityAndMulCalcVis.fieldArityAndSetMul(e, sigParent);
    }

    // the following is useful for setting default multiplicity
    // in any expressions
    // i.e., we are not trying to calculate arity to be recorded
    // in the fieldTable
    AlloyExpr setMul(AlloyExpr e) {
        return arityAndMulCalcVis.setMul(e).exp;
    }

    AlloyPara setMul(AlloyPara p) {
        return new TestAndReplaceExprParaVis(e -> true, e -> this.setMul(e)).visit(p);
    }

    AlloyPara setMul(List<AlloyDecl> decls, AlloyPara p) {
        CalcAritySetMulDefaultsExprVis vis =
                new CalcAritySetMulDefaultsExprVis(
                        this::sigFieldArity, this::predFunReturnArity, this::predFunArgsArities);
        List<AlloyDecl> expandedDecls = emptyList();
        for (AlloyDecl d : decls) {
            expandedDecls.addAll(d.expand());
        }
        vis.localEnvPush(expandedDecls);
        AlloyPara newPara =
                new TestAndReplaceExprParaVis(e -> true, e -> vis.visit(e).exp).visit(p);
        // vis is local so no need to pop
        /*
        List<AlloyDecl> expandedDecls = emptyList();
        for (AlloyDecl d : decls) {
            expandedDecls.addAll(d.expand());
        }
        vis.localEnvPop(expandedDecls);
        */
        return newPara;
    }

    /*
    public void localEnvPush(List<AlloyDecl> decls) {
        List<AlloyDecl> expandedDecls = emptyList();
        for (AlloyDecl d : decls) {
            expandedDecls.addAll(d.expand());
        }
        arityAndMulCalcVis.localEnvPush(expandedDecls);
    }

    public void localEnvPop(List<AlloyDecl> decls) {
        List<AlloyDecl> expandedDecls = emptyList();
        for (AlloyDecl d : decls) {
            expandedDecls.addAll(d.expand());
        }
        arityAndMulCalcVis.localEnvPop(expandedDecls);
    }
    */
}
