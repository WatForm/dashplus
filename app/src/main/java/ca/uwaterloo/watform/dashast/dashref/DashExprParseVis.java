package ca.uwaterloo.watform.dashast.dashref;

import static ca.uwaterloo.watform.parser.Parser.*;

import antlr.generated.DashParser;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.dashast.DashStrings.DashRefKind;
import ca.uwaterloo.watform.utils.Pos;

public final class DashExprParseVis extends AlloyExprParseVis {
    @Override
    public DashRef visitDashRefExpr(DashParser.DashRefExprContext ctx) {
        return (DashRef) this.visit(ctx.dashRef());
    }

    @Override
    public DashRef visitDashRef(DashParser.DashRefContext ctx) {
        return visitDashRefWithKind(ctx, DashRefKind.VAR);
    }

    public DashRef visitDashRefWithKind(
            DashParser.DashRefContext ctx, DashStrings.DashRefKind kind) {
        return new DashRef(
                new Pos(ctx),
                kind,
                visitAll(ctx.name(), this, AlloyNameExpr.class),
                visitAll(ctx.expr1(), this, AlloyExpr.class));
    }
}
