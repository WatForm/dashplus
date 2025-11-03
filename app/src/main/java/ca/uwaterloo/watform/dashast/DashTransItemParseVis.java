package ca.uwaterloo.watform.dashast;

import antlr.generated.*;
import ca.uwaterloo.watform.dashast.dashNamedExpr.*;
import ca.uwaterloo.watform.dashast.dashref.DashExprParseVis;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.utils.*;

public final class DashTransItemParseVis extends DashBaseVisitor<DashTransItem> {
    private final DashExprParseVis exprParseVis = new DashExprParseVis();

    @Override
    public DashOn visitDashOnQname(DashParser.DashOnQnameContext ctx) {
        return new DashOn(
                new Pos(ctx),
                new DashRef(
                        new Pos(ctx),
                        DashStrings.DashRefKind.EVENT,
                        this.exprParseVis.visit(ctx.qname()).toString(),
                        DashRef.emptyParamValuesList()));
    }

    @Override
    public DashOn visitDashOnRef(DashParser.DashOnRefContext ctx) {
        return new DashOn(
                new Pos(ctx),
                this.exprParseVis.visitDashRefWithKind(
                        ctx.dashRef(), DashStrings.DashRefKind.EVENT));
    }

    @Override
    public DashSend visitDashSendQname(DashParser.DashSendQnameContext ctx) {
        return new DashSend(
                new Pos(ctx),
                new DashRef(
                        new Pos(ctx),
                        DashStrings.DashRefKind.EVENT,
                        this.exprParseVis.visit(ctx.qname()).toString(),
                        DashRef.emptyParamValuesList()));
    }

    @Override
    public DashSend visitDashSendRef(DashParser.DashSendRefContext ctx) {
        return new DashSend(
                new Pos(ctx),
                this.exprParseVis.visitDashRefWithKind(
                        ctx.dashRef(), DashStrings.DashRefKind.EVENT));
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
    public DashFrom visitDashFromQname(DashParser.DashFromQnameContext ctx) {
        return new DashFrom(
                new Pos(ctx),
                new DashRef(
                        new Pos(ctx),
                        DashStrings.DashRefKind.STATE,
                        this.exprParseVis.visit(ctx.qname()).toString(),
                        DashRef.emptyParamValuesList()));
    }

    @Override
    public DashFrom visitDashFromRef(DashParser.DashFromRefContext ctx) {
        return new DashFrom(
                new Pos(ctx),
                this.exprParseVis.visitDashRefWithKind(
                        ctx.dashRef(), DashStrings.DashRefKind.STATE));
    }

    @Override
    public DashGoto visitDashGotoQname(DashParser.DashGotoQnameContext ctx) {
        return new DashGoto(
                new Pos(ctx),
                new DashRef(
                        new Pos(ctx),
                        DashStrings.DashRefKind.STATE,
                        this.exprParseVis.visit(ctx.qname()).toString(),
                        DashRef.emptyParamValuesList()));
    }

    @Override
    public DashGoto visitDashGotoRef(DashParser.DashGotoRefContext ctx) {
        return new DashGoto(
                new Pos(ctx),
                this.exprParseVis.visitDashRefWithKind(
                        ctx.dashRef(), DashStrings.DashRefKind.STATE));
    }
}
