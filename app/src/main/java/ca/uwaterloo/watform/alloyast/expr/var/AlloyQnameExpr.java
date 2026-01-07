package ca.uwaterloo.watform.alloyast.expr.var;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.nullField;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.alloyast.*;

import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class AlloyQnameExpr extends AlloyVarExpr
        implements AlloySigRefExpr, AlloyScopableExpr {
    public final List<AlloyVarExpr> vars;

    public AlloyQnameExpr(Pos pos, List<AlloyVarExpr> vars) {
        super(pos, vars.stream().map(v -> v.label).collect(Collectors.joining(SLASH)));
        this.vars = Collections.unmodifiableList(vars);
        if (!vars.isEmpty()) {
            if (!(vars.getFirst() instanceof AlloyNameExpr)
                    && !(vars.getFirst() instanceof AlloySeqExpr)
                    && !(vars.getFirst() instanceof AlloyThisExpr)) {
                throw AlloyCtorError.qnameFirstMustBeNameThisOrSeq(pos);
            }
            for (int i = 1; i < vars.size(); i++) {
                if (!(vars.get(i) instanceof AlloyNameExpr)) {
                    throw AlloyCtorError.qnameTailIsAllName(pos);
                }
            }
        }

        reqNonNull(nullField(pos, this), this.vars);
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

    public AlloyQnameExpr(Pos pos, String label) {
        this(
                pos,
                Collections.unmodifiableList(Collections.singletonList(new AlloyNameExpr(label))));
    }

    public AlloyQnameExpr(String label) {
        this(
                Pos.UNKNOWN,
                Collections.unmodifiableList(Collections.singletonList(new AlloyNameExpr(label))));
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AlloyQnameExpr rebuild(String label) {
        return new AlloyQnameExpr(this.pos, label);
    }
}
