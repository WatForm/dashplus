/*
	all facts of an Alloy model (including ones from sigs and imports)
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.alloymodel.PredFunData.*;
import static ca.uwaterloo.watform.parser.Parser.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class SMConstraints extends SMPredFuns {

    // ignore the name of the fact
    // we just care about the namespace
    public record Constraint(String nameSpace, AlloyExpr expr, Boolean resolved) {
        @Override
        public String toString() {
            return String.format("[%s] %s (resolved=%s)", nameSpace, expr, resolved);
        }
    }

    List<Constraint> constraints = emptyList();

    protected SMConstraints() {}

    protected SMConstraints(SMConstraints other) {
        super(other);
        this.constraints = new ArrayList<Constraint>(other.constraints);
    }

    protected void resolveSMConstraints(
            TriFunction<AlloyExpr, String, List<AlloyDecl>, ResolveInfo> resolve2) {
        List<Constraint> newConstraints = emptyList();
        for (Constraint c : this.constraints) {
            if (!c.resolved) {
                ResolveInfo r = resolve2.apply(c.expr, c.nameSpace, emptyList());
                if (r.arity.isPresent()) {
                    newConstraints.add(new Constraint(c.nameSpace, r.exp, true));
                } else {
                    throw AlloyModelError.unknownArity(c.expr.pos, c.toString());
                }
            } else newConstraints.add(c);
        }
        this.constraints = newConstraints;
    }

    public void createConstraint(String nameSpace, AlloyExpr expr) {
        this.constraints.add(new Constraint(nameSpace, expr, false));
    }

    public List<AlloyExpr> allConstraints() {
        return mapBy(this.constraints, r -> r.expr);
    }

    public void debugSMConstraints() {
        StringBuilder sb = new StringBuilder("SMConstraints:\n");

        constraints.forEach(c -> sb.append("    ").append(c).append('\n'));

        System.out.println(sb.toString() + "\n");
    }
}
