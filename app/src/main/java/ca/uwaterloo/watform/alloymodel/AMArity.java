package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.Builtins.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import java.util.*;

public class AMArity extends AMPredTable {

    protected AMArity(AlloyFile alloyFile) {
        super(alloyFile);
    }

    protected AMArity(AMArity other) {
        super(other);
    }

    protected void resolve() {
        // arityAndSetMul is not available to AMs below this in the hierarchy
        super.resolve(this::arityAndSetMul);
    }

    // must go before arityAndMulCalcVis private attritube
    protected Optional<Integer> symbolArity(String symbolName) {
        // enums will be in allSigs
        // System.out.println("symbolArity " + symbolName);
        if (this.allSigs().contains(symbolName)) {
            // System.out.println("here1");
            return Optional.of(1);
        } else if (this.allFields().contains(symbolName)) {
            // System.out.println("here2");
            return this.fieldArity(symbolName);
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
    CalcAritySetMulDefaultsExprVis.Result arityAndSetMul(AlloyExpr e) {
        return arityAndMulCalcVis.visit(e);
    }

    // the following is useful for setting default multiplicity
    // in any expressions
    // i.e., we are not trying to calculate arity to be recorded
    // in the fieldTable
    AlloyExpr setMul(AlloyExpr e) {
        return arityAndMulCalcVis.visit(e).exp;
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
