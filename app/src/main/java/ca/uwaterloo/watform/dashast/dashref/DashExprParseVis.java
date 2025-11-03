package ca.uwaterloo.watform.dashast.dashref;

import antlr.generated.DashParser;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.utils.ParserUtil;
import ca.uwaterloo.watform.utils.Pos;

public final class DashExprParseVis extends AlloyExprParseVis {
    @Override
    public DashRef visitDashRefExpr(DashParser.DashRefExprContext ctx) {
        return (DashRef) this.visit(ctx.dashRef());
    }

    @Override
    public DashRef visitDashRef(DashParser.DashRefContext ctx) {
        return new DashRef(
                new Pos(ctx.LBRACK()), // following Dash.cup
                DashStrings.DashRefKind.VAR,
                ParserUtil.visitAll(ctx.qname(), this, AlloyQnameExpr.class),
                ParserUtil.visitAll(ctx.expr1(), this, AlloyExpr.class));
    }

    public DashRef visitDashRefWithKind(
            DashParser.DashRefContext ctx, DashStrings.DashRefKind kind) {
        return new DashRef(
                new Pos(ctx.LBRACK()), // following Dash.cup
                kind,
                ParserUtil.visitAll(ctx.qname(), this, AlloyQnameExpr.class),
                ParserUtil.visitAll(ctx.expr1(), this, AlloyExpr.class));
    }
}
