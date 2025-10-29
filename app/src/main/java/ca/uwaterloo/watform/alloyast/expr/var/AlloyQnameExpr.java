package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class AlloyQnameExpr extends AlloyVarExpr
        implements AlloySigRefExpr, AlloyScopableExpr {
    public final List<AlloyVarExpr> vars;

    public AlloyQnameExpr(Pos pos, List<AlloyVarExpr> vars) {
        super(
                pos,
                vars.stream()
                        .map(AlloyVarExpr::getLabel)
                        .collect(Collectors.joining(AlloyStrings.SLASH)));
        this.vars = Collections.unmodifiableList(vars);
        if (!vars.isEmpty()) {
            if (!(vars.getFirst() instanceof AlloyNameExpr)
                    && !(vars.getFirst() instanceof AlloySeqExpr)
                    && !(vars.getFirst() instanceof AlloyThisExpr)) {
                throw new ImplementationError(
                        pos,
                        "First var of AlloyQnameExpr must be either AlloyNameExpr, AlloySeqExpr or AlloyThisExpr. ");
            }
            for (int i = 1; i < vars.size(); i++) {
                if (!(vars.get(i) instanceof AlloyNameExpr)) {
                    throw new ImplementationError(
                            pos,
                            "Everything after the head of AlloyQnameExpr must be AlloyNameExpr. ");
                }
            }
        }
    }

    public AlloyQnameExpr(List<AlloyVarExpr> vars) {
        this(Pos.UNKNOWN, vars);
    }

    public AlloyQnameExpr(Pos pos, AlloyVarExpr var) {
        this(pos, Collections.unmodifiableList(Collections.singletonList(var)));
    }

    public AlloyQnameExpr(AlloyVarExpr var) {
        this(Pos.UNKNOWN, Collections.unmodifiableList(Collections.singletonList(var)));
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
