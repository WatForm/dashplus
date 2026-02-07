package ca.uwaterloo.watform.dashast.dashref;

import static ca.uwaterloo.watform.parser.Parser.*;

import antlr.generated.DashParser;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.utils.Pos;

public final class DashExprParseVis extends AlloyExprParseVis {
    @Override
    public DashRef visitDashRefExpr(DashParser.DashRefExprContext ctx) {
        return (DashRef) this.visit(ctx.dashRef());
    }

    @Override
    public DashRef visitDashRef(DashParser.DashRefContext ctx) {
        return visitVarDashRef(ctx);
    }

    public DashRef visitEventDashRef(DashParser.DashRefContext ctx) {
        return new EventDashRef(
                new Pos(ctx),
                visitAll(ctx.name(), this, AlloyNameExpr.class),
                visitAll(ctx.expr1(), this, AlloyExpr.class));
    }

    public DashRef visitStateDashRef(DashParser.DashRefContext ctx) {
        return new StateDashRef(
                new Pos(ctx),
                visitAll(ctx.name(), this, AlloyNameExpr.class),
                visitAll(ctx.expr1(), this, AlloyExpr.class));
    }

    public DashRef visitVarDashRef(DashParser.DashRefContext ctx) {
        return new VarDashRef(
                new Pos(ctx),
                visitAll(ctx.name(), this, AlloyNameExpr.class),
                visitAll(ctx.expr1(), this, AlloyExpr.class));
    }
}
