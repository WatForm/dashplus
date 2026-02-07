package ca.uwaterloo.watform.dashast;

import antlr.generated.*;
import ca.uwaterloo.watform.dashast.dashNamedExpr.*;
import ca.uwaterloo.watform.dashast.dashref.DashExprParseVis;
import ca.uwaterloo.watform.utils.*;

public final class DashTransItemParseVis extends DashBaseVisitor<DashTransItem> {
    private final DashExprParseVis exprParseVis = new DashExprParseVis();

    @Override
    public DashOn visitDashOnRef(DashParser.DashOnRefContext ctx) {
        return new DashOn(new Pos(ctx), this.exprParseVis.visitEventDashRef(ctx.dashRef()));
    }

    @Override
    public DashSend visitDashSendRef(DashParser.DashSendRefContext ctx) {
        return new DashSend(new Pos(ctx), this.exprParseVis.visitEventDashRef(ctx.dashRef()));
    }

    @Override
    public DashWhen visitDashWhen(DashParser.DashWhenContext ctx) {
        return new DashWhen(new Pos(ctx), this.exprParseVis.visit(ctx.expr1()));
    }

    @Override
    public DashDo visitDashDo(DashParser.DashDoContext ctx) {
        return new DashDo(new Pos(ctx), this.exprParseVis.visit(ctx.expr1()));
    }

    @Override
    public DashFrom visitDashFromRef(DashParser.DashFromRefContext ctx) {
        return new DashFrom(new Pos(ctx), this.exprParseVis.visitStateDashRef(ctx.dashRef()));
    }

    @Override
    public DashGoto visitDashGotoRef(DashParser.DashGotoRefContext ctx) {
        return new DashGoto(new Pos(ctx), this.exprParseVis.visitStateDashRef(ctx.dashRef()));
    }
}
