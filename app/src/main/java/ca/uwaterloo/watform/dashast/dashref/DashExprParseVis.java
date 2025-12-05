package ca.uwaterloo.watform.dashast.dashref;

import static ca.uwaterloo.watform.utils.ParserUtil.*;

import antlr.generated.DashParser;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.utils.Pos;

public final class DashExprParseVis extends AlloyExprParseVis {
    @Override
    public DashRef visitDashRef1Expr(DashParser.DashRef1ExprContext ctx) {
        return (DashRef) this.visit(ctx.dashRef1());
    }

    @Override
    public DashRef visitDashRef1(DashParser.DashRef1Context ctx) {
        return new DashRef(
                new Pos(ctx),
                DashStrings.DashRefKind.VAR,
                visitAll(ctx.name(), this, AlloyNameExpr.class),
                visitAll(ctx.expr1(), this, AlloyExpr.class));
    }

    public DashRef visitDashRef1WithKind(
            DashParser.DashRef1Context ctx, DashStrings.DashRefKind kind) {
        return new DashRef(
                new Pos(ctx),
                kind,
                visitAll(ctx.name(), this, AlloyNameExpr.class),
                visitAll(ctx.expr1(), this, AlloyExpr.class));
    }

    public DashRef visitDashRef2WithKind(
            DashParser.DashRef2Context ctx, DashStrings.DashRefKind kind) {
        return new DashRef(
                new Pos(ctx),
                kind,
                visitAll(ctx.name(), this, AlloyNameExpr.class),
                DashRef.emptyParamValuesList());
    }
}
