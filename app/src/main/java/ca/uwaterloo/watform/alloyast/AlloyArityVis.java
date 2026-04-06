package ca.uwaterloo.watform.alloyast;

import static ca.uwaterloo.watform.alloytotla.BoilerplateA2T.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloymodel.AlloyModelError;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.utils.ImplementationError;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/*
 * Plan
 * 1) create declTable: Qname of a field -> its Decl
 * 2) create arityTale: Qname of a field or sig-> Arity
 * 3) check for cycles using declTable
 * 		- @Nancy Is there a visitor that returns a list of Qnames used in an
 * AlloyExpr? We didn't find one.
 * 		- need new exprVis that returns a list Qnames used in an AlloyExpr
 * 		- DFS with recursion stack can do this in linear time
 * 4) fill in arityTable by iterating through declTable,
 * 		- at each field, recursively find all the arity it needs to know via
 * AlloyArityVis
 * 			- visit(AlloyQname) has access to arityTable b/c it's a field
 * 		- this is linear time
 *
 * Integration into pipeline:
 * 		- We need arity to fill in defaults for the AST, like Decl
 * 		- But we also need the AST to calculate arity
 * 		- The AST is immutable
 * 		- We thought we could do this:
 * 			- ANTLR -> our AST -> Calculate Arity
 * 				-> run another visitor on the origial AST with arity info to
 * produce a new AST -> new AST with defaults filled
 * 			- We need copy constructors for more AlloyAST
 */

public class AlloyArityVis implements AlloyExprVis<Integer> {

    // this is half-finished - the other half, integration with field table and
    // dynamic lookups, is yet to be completed. This is shelved for now

    public static final Integer BOOLEAN_ARITY = Integer.valueOf(0);
    public static final Integer UNKNOWN_ARITY = Integer.valueOf(-1);

    protected final HashMap<AlloyQnameExpr, AlloyDecl> declTable;
    // hold the entire Decl
    protected final HashMap<AlloyQnameExpr, Integer> arityTable;

    protected static HashMap<AlloyQnameExpr, AlloyDecl> buildExprTable(AlloyFile alloyFile) {
        HashMap<AlloyQnameExpr, AlloyDecl> declTable = new HashMap<>();
        for (AlloyPara para : alloyFile.paras) {
            if (!(para instanceof AlloySigPara)) continue;
            AlloySigPara sig = (AlloySigPara) para;
            for (AlloyDecl field : sig.fields) {
                List<AlloyDecl> expandedFields = field.expand();
                for (AlloyDecl expandedField : expandedFields) {
                    AlloyQnameExpr qname = expandedField.qnames.getFirst();
                    if (declTable.containsKey(qname)) {
                        AlloyModelError.duplicateName(declTable.get(qname).pos, qname.pos);
                    }
                    declTable.put(qname, expandedField);
                }
            }
        }
        return declTable;
    }

    protected static HashMap<AlloyQnameExpr, Integer> buildArityTable(AlloyFile alloyFile) {
        HashMap<AlloyQnameExpr, Integer> arityTable = new HashMap<>();
        for (AlloyPara para : alloyFile.paras) {
            if (!(para instanceof AlloySigPara)) continue;
            AlloySigPara sig = (AlloySigPara) para;
            for (AlloyQnameExpr sigName : sig.qnames) {
                arityTable.put(sigName, Integer.valueOf(1));
            }
            for (AlloyDecl field : sig.fields) {
                List<AlloyDecl> expandedFields = field.expand();
                for (AlloyDecl expandedField : expandedFields) {
                    AlloyQnameExpr qname = expandedField.qnames.getFirst();
                    // don't check for duplication again here; already checked in buildExprTable
                    arityTable.put(qname, Integer.valueOf(-1));
                }
            }
        }
        return arityTable;
    }

    public AlloyArityVis(AlloyFile alloyFile) {
        this.declTable = buildExprTable(alloyFile);
        this.arityTable = buildArityTable(alloyFile);
    }

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

        filterBy(
                        Arrays.asList(
                                AlloyUnionExpr.class,
                                AlloyDiffExpr.class,
                                AlloyRelOvrdExpr.class,
                                AlloyIntersExpr.class),
                        c -> c.isInstance(binExpr))
                .forEach(c -> equalityCheck(binExpr.left, binExpr.right));

        Integer answer =
                switch (binExpr) {
                    case AlloyAndExpr _ -> BOOLEAN_ARITY;
                    case AlloyArrowExpr _ -> el + er;
                    case AlloyCmpExpr exp -> BOOLEAN_ARITY;
                    case AlloyDiffExpr _ -> el;
                    case AlloyDomRestrExpr _ -> er - 1;
                    case AlloyDotExpr _ -> el + er - 2;
                    case AlloyEqualsExpr _ -> BOOLEAN_ARITY;
                    case AlloyIffExpr _ -> BOOLEAN_ARITY;
                    case AlloyImpliesExpr _ -> BOOLEAN_ARITY;
                    case AlloyIntersExpr _ -> el;
                    case AlloyNotEqualsExpr _ -> BOOLEAN_ARITY;
                    case AlloyOrExpr _ -> BOOLEAN_ARITY;
                    case AlloyRelOvrdExpr _ -> el;
                    case AlloyRngRestrExpr _ -> el - 1;
                    case AlloyUnionExpr _ -> el;
                    default -> null;
                };

        if (binExpr.getClass() == AlloyAndExpr.class)
            if (el != er) if (answer != null) return answer;

        throw ImplementationError.notSupported(
                "non-translatable expression: " + binExpr.toString());
    }

    @Override
    public Integer visit(AlloyUnaryExpr unaryExpr) {

        Integer e = visit(unaryExpr.sub);

        Integer answer =
                switch (unaryExpr) {
                    case AlloyCardExpr _ -> BOOLEAN_ARITY;
                    case AlloyNegExpr _ -> BOOLEAN_ARITY;
                    case AlloyQtExpr exp ->
                            switch (exp.qt) {
                                case AlloyQtEnum.SOME -> BOOLEAN_ARITY;
                                case AlloyQtEnum.LONE -> BOOLEAN_ARITY;
                                case AlloyQtEnum.NO -> BOOLEAN_ARITY;
                                case AlloyQtEnum.ONE -> BOOLEAN_ARITY;
                                case AlloyQtEnum.ALL -> BOOLEAN_ARITY;
                                default -> null;
                            };
                    case AlloyTransExpr _ -> e;
                    default -> null;
                };

        if (answer != null) return Integer.valueOf(0);

        throw ImplementationError.notSupported(
                "non-translatable expression: " + unaryExpr.toString());
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

    private void equalityCheck(AlloyExpr el, AlloyExpr er) {
        if (visit(el) != visit(er))
            throw new ImplementationError(
                    "Arities are not equal in left: "
                            + el.toString()
                            + " and right: "
                            + er.toString());
    }
}
