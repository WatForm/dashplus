package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.Builtins.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import java.util.*;

public class AMArity extends AMPredFcnTable {

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
    protected Optional<Integer> symbolArity(String symbolName, Optional<String> sigParent) {
        // enums will be in allSigs
        // System.out.println("symbolArity " + symbolName);
        if (this.allSigs().contains(symbolName)) {
            // System.out.println("here1");
            return Optional.of(1);
        } else if (this.allFields().contains(symbolName)) {
            // System.out.println("here2");
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
            // System.out.println("here3");
            return Optional.of(Builtins.builtinArity(symbolName));
        } else if (this.isPred(symbolName)) {
            // System.out.println("here4");
            return Optional.of(this.predArity(symbolName));
        } else {
            // unknown arity
            return UNKNOWN_ARITY;
            // throw ImplementationError.missingCase("symbol " + symbolName + " not found
            // (symbolArity) " + symbolNam);
        }
    }

    // same visitor object used everywhere
    private CalcAritySetMulDefaultsExprVis arityAndMulCalcVis =
            new CalcAritySetMulDefaultsExprVis(this::symbolArity);

    // this is used when we are getting back arity for the fieldTable
    CalcAritySetMulDefaultsExprVis.Result fieldArityAndSetMul(AlloyExpr e, String sigParent) {
        return arityAndMulCalcVis.fieldArityAndSetMul(e, sigParent);
    }

    // the following is useful for setting default multiplicity
    // in any expressions
    // i.e., we are not trying to calculate arity to be recorded
    // in the fieldTable
    AlloyExpr setMul(AlloyExpr e) {
        return arityAndMulCalcVis.setMul(e).exp;
    }

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
}
