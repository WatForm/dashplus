package ca.uwaterloo.watform.tlaast;

import ca.uwaterloo.watform.tlaast.tlabinops.TlaAnd;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaDiffSet;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaEquals;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaInSet;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaIntersectionSet;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaNotEq;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaNotInSet;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaOr;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaSubsetEq;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaUnionSet;
import ca.uwaterloo.watform.utils.*;
import java.util.List;

public abstract class TlaExp extends ASTNode {
    public abstract List<TlaExp> getChildren();

    public abstract String toTLAPlusSnippetCore();

    public String toTLAPlusSnippet(boolean brackets) {
        if (brackets)
            return TlaStrings.BRACKET_OPEN + toTLAPlusSnippetCore() + TlaStrings.BRACKET_CLOSE;
        return toTLAPlusSnippetCore();
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        sb.append(this.toTLAPlusSnippet(false));
        return;
    }

    @Override
    public void pp(PrintContext pCtx) {
        // I added this here so my change that makes pp a mandatory
        // method can build successfully. - Jack
    }

    // convenience in writing
    /*
    public Tla (TlaExp op)
    {
        return new Tla(this, op);
    }
    */

    public TlaAnd AND(TlaExp op) {
        return new TlaAnd(this, op);
    }

    public TlaOr OR(TlaExp op) {
        return new TlaOr(this, op);
    }

    public TlaEquals EQUALS(TlaExp op) {
        return new TlaEquals(this, op);
    }

    public TlaInSet IN(TlaExp op) {
        return new TlaInSet(this, op);
    }

    public TlaNotInSet NOTIN(TlaExp op) {
        return new TlaNotInSet(this, op);
    }

    public TlaIntersectionSet INTERSECTION(TlaExp op) {
        return new TlaIntersectionSet(this, op);
    }

    public TlaUnionSet UNION(TlaExp op) {
        return new TlaUnionSet(this, op);
    }

    public TlaDiffSet DIFF(TlaExp op) {
        return new TlaDiffSet(this, op);
    }

    public TlaSubsetEq SUBSETEQ(TlaExp op) {
        return new TlaSubsetEq(this, op);
    }

    public TlaNotEq NOT_EQUALS(TlaExp op) {
        return new TlaNotEq(this, op);
    }
}
