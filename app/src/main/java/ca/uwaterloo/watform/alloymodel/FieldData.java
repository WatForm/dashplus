package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.alloymodel.ResolveInfo.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import java.util.*;

public class FieldData {

    // since these are changed after initialization
    // they have getters and setters
    protected AlloyExpr expr;
    // everything will get a fieldArity after resolve
    protected Optional<Integer> arity = UNKNOWN_ARITY;
    protected List<Qname> product = emptyList();

    // eventually we could add something about its basic type

    protected FieldData(AlloyExpr fieldExpr) {
        this.expr = fieldExpr;
    }

    @Override
    public String toString() {
        return expr
                + ","
                + "arity="
                + (arity.isPresent() ? Integer.toString(arity.get()) : "?")
                + ", "
                + product
                + '}';
    }

    // setters

    /*
    public void setFieldArity(Optional<Integer> a) {
        this.arity = a;
    }

    public void setFieldExpr(AlloyExpr exp) {
        this.expr = exp;
    }

    public void setIsResolved() {
        this.isResolved = true;
    }
    */

    /*
    public AlloyExpr fieldExpr() {
        return this.fieldExpr;
    }

    public Optional<Integer> fieldArity() {
        return this.fieldArity;
    }

    public List<Qname> fieldProductExpr() {
        // TODO: walk over arrows and o/w throw errors
        assert (this.isResolved);
        return this.productExpr;
    }

    public Boolean isResolved() {
        return this.isResolved;
    }
    */

}
