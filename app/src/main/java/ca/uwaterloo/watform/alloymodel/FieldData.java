package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyThisExpr;
import ca.uwaterloo.watform.exprvisitor.ReplaceExprVis;
import java.util.*;

public class FieldData {

    // probably we don't need these two because fieldName is the key to the table
    // and the parent of a field is not used anywhere currently
    public final String fieldName;
    public final String sigParent;

    // since these are changed after initialization
    // they have getters and setters
    protected AlloyExpr fieldExpr;
    protected Optional<Integer> fieldArity;

    // eventually we could add something about its basic type

    FieldData(String fieldName, AlloyExpr fieldExpr, String sigParent) {
        this.fieldName = fieldName;
        // replace any occurrences of "this" with AlloyQnameVar(sigParent)
        this.fieldExpr =
                new ReplaceExprVis(e -> e instanceof AlloyThisExpr, e -> AlloyVar(sigParent))
                        .visit(fieldExpr);
        this.sigParent = sigParent;
        // everything will get a fieldArity eventually
        this.fieldArity = Optional.empty();
    }

    // setters

    public void setFieldArity(Optional<Integer> a) {
        this.fieldArity = a;
    }

    public void setFieldExpr(AlloyExpr exp) {
        this.fieldExpr = exp;
    }

    // getters

    public AlloyExpr fieldExpr() {
        return this.fieldExpr;
    }

    public Optional<Integer> fieldArity() {
        return this.fieldArity;
    }
}
