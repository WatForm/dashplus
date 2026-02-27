package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.Boilerplate.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.utils.ImplementationError;

public class AlloyArityVis implements AlloyExprVis<Integer> {

    @Override
    public Integer visit(DashRef dashRef) {

        throw ImplementationError.notSupported("dashref inside pure AlloyVis");
    }

    @Override
    public Integer visit(DashParam dashParam) {

        throw ImplementationError.notSupported("dashParam inside pure AlloyVis");
    }

    @Override
    public Integer visit(AlloyBinaryExpr binExpr) {

        Integer el = this.visit(binExpr.left);
        Integer er = this.visit(binExpr.right);

        Integer answer =
                switch (binExpr) {
                    case AlloyAndExpr _ -> el; //Integer.valueOf(0);
                    case AlloyArrowExpr _ -> el+er;
                    case AlloyCmpExpr exp -> Integer.valueOf(0);
                    case AlloyDiffExpr _ -> el;
                    case AlloyDomRestrExpr _ -> el; //_DOMAIN_RESTRICTION(el, er);
                    case AlloyDotExpr _ -> el; //Integer.valueOf(el.v);
                    case AlloyEqualsExpr _ -> Integer.valueOf(0);
                    case AlloyIffExpr _ -> Integer.valueOf(0);
                    case AlloyImpliesExpr _ -> Integer.valueOf(0);
                    case AlloyIntersExpr _ -> el;
                    case AlloyNotEqualsExpr _ -> Integer.valueOf(0);
                    case AlloyOrExpr _ -> Integer.valueOf(0);
                    case AlloyRelOvrdExpr _ -> el; //_RELATIONAL_OVERRIDE(el, er);
                    case AlloyRngRestrExpr _ -> el; //_RANGE_RESTRICTION(el, er);
                    case AlloyUnionExpr _ -> el;
                    default -> null; /* case  _ -> (el, er); */
                };

		return Integer.valueOf(0);

        // if (answer != null) return answer;

        // throw ImplementationError.notSupported(
        //        "non-translatable expression: " + binExpr.toString());
    }

    @Override
    public Integer visit(AlloyUnaryExpr unaryExpr) {

        Integer e = visit(unaryExpr.sub);

		/*
        TlaExp answer =
                switch (unaryExpr) {
                    case AlloyCardExpr _ -> TlaStdLibs.Cardinality(e);
                    case AlloyNegExpr _ -> TlaNot(e);
                    case AlloyQtExpr exp ->
                            switch (exp.qt) {
                                case AlloyQtEnum.SOME -> _SOME(e);
                                case AlloyQtEnum.LONE -> _LONE(e);
                                case AlloyQtEnum.NO -> _NO(e);
                                case AlloyQtEnum.ONE -> _ONE(e);
                                case AlloyQtEnum.ALL -> TlaTrue();
                                default -> null;
                            };
                    case AlloyTransExpr _ -> _TRANSPOSE(e);
                    default -> null;
                };
		*/

        //if (answer != null) 
		// 
		return Integer.valueOf(0);

        // throw ImplementationError.notSupported(
                // "non-translatable expression: " + unaryExpr.toString());
    }

    @Override
    public Integer visit(AlloyVarExpr varExpr) {

        TlaExp answer =
                switch (varExpr) {
                    case AlloyUnivExpr _ -> _UNIV();
                    case AlloyIdenExpr _ -> _IDEN();
                    case AlloyNoneExpr _ -> _NONE();
                    default -> null;
                };

        if (answer != null) return Integer.valueOf(0);

        throw ImplementationError.notSupported(
                "non-translatable expression: " + varExpr.toString());
    }

    @Override
    public Integer visit(AlloyBlock block) {

        return Integer.valueOf(0);
    }

    @Override
    public Integer visit(AlloyBracketExpr bracketExpr) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for bracket");
    }

    @Override
    public Integer visit(AlloyCphExpr comprehensionExpr) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for cph");
    }

    @Override
    public Integer visit(AlloyIteExpr iteExpr) {

        return this.visit(iteExpr.conseq);
    }

    @Override
    public Integer visit(AlloyLetExpr letExpr) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for let");
    }

    @Override
    public Integer visit(AlloyQuantificationExpr quantificationExpr) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for quantification");
    }

    @Override
    public Integer visit(AlloyDecl decl) {

        throw ImplementationError.notSupported("Unimplemented method 'visit' for decl");
    }
}
