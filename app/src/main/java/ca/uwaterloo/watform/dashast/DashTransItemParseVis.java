package ca.uwaterloo.watform.dashast;

import antlr.generated.*;
import ca.uwaterloo.watform.dashast.dashNamedExpr.*;
import ca.uwaterloo.watform.dashast.dashref.DashExprParseVis;
import ca.uwaterloo.watform.utils.*;

public final class DashTransItemParseVis extends DashBaseVisitor<DashTransItem> {
    private final DashExprParseVis exprParseVis = new DashExprParseVis();

    // @Override
    // public DashOn visitDashOnQname(DashParser.DashOnQnameContext ctx) {
    //     return new DashOn(
    //             new Pos(ctx),
    //             new DashRef(
    //                     new Pos(ctx),
    //                     DashStrings.DashRefKind.EVENT,
    //                     this.exprParseVis.visit(ctx.qname()).toString(),
    //                     DashRef.emptyParamValuesList()));
    // }

    @Override
    public DashOn visitDashOnRef1(DashParser.DashOnRef1Context ctx) {
        return new DashOn(
                new Pos(ctx),
                this.exprParseVis.visitDashRef1WithKind(
                        ctx.dashRef1(), DashStrings.DashRefKind.EVENT));
    }

    @Override
    public DashOn visitDashOnRef2(DashParser.DashOnRef2Context ctx) {
        return new DashOn(
                new Pos(ctx),
                this.exprParseVis.visitDashRef2WithKind(
                        ctx.dashRef2(), DashStrings.DashRefKind.EVENT));
    }

    // @Override
    // public DashSend visitDashSendQname(DashParser.DashSendQnameContext ctx) {
    //     return new DashSend(
    //             new Pos(ctx),
    //             new DashRef(
    //                     new Pos(ctx),
    //                     DashStrings.DashRefKind.EVENT,
    //                     this.exprParseVis.visit(ctx.qname()).toString(),
    //                     DashRef.emptyParamValuesList()));
    // }

    @Override
    public DashSend visitDashSendRef1(DashParser.DashSendRef1Context ctx) {
        return new DashSend(
                new Pos(ctx),
                this.exprParseVis.visitDashRef1WithKind(
                        ctx.dashRef1(), DashStrings.DashRefKind.EVENT));
    }

    @Override
    public DashSend visitDashSendRef2(DashParser.DashSendRef2Context ctx) {
        return new DashSend(
                new Pos(ctx),
                this.exprParseVis.visitDashRef2WithKind(
                        ctx.dashRef2(), DashStrings.DashRefKind.EVENT));
    }

    @Override
    public DashWhen visitDashWhen(DashParser.DashWhenContext ctx) {
        return new DashWhen(new Pos(ctx), this.exprParseVis.visit(ctx.expr1()));
    }

    @Override
    public DashDo visitDashDo(DashParser.DashDoContext ctx) {
        return new DashDo(new Pos(ctx), this.exprParseVis.visit(ctx.expr1()));
    }

    // @Override
    // public DashFrom visitDashFromQname(DashParser.DashFromQnameContext ctx) {
    //     return new DashFrom(
    //             new Pos(ctx),
    //             new DashRef(
    //                     new Pos(ctx),
    //                     DashStrings.DashRefKind.STATE,
    //                     this.exprParseVis.visit(ctx.qname()).toString(),
    //                     DashRef.emptyParamValuesList()));
    // }

    @Override
    public DashFrom visitDashFromRef1(DashParser.DashFromRef1Context ctx) {
        return new DashFrom(
                new Pos(ctx),
                this.exprParseVis.visitDashRef1WithKind(
                        ctx.dashRef1(), DashStrings.DashRefKind.STATE));
    }

    @Override
    public DashFrom visitDashFromRef2(DashParser.DashFromRef2Context ctx) {
        return new DashFrom(
                new Pos(ctx),
                this.exprParseVis.visitDashRef2WithKind(
                        ctx.dashRef2(), DashStrings.DashRefKind.STATE));
    }

    // @Override
    // public DashGoto visitDashGotoQname(DashParser.DashGotoQnameContext ctx) {
    //     return new DashGoto(
    //             new Pos(ctx),
    //             new DashRef(
    //                     new Pos(ctx),
    //                     DashStrings.DashRefKind.STATE,
    //                     this.exprParseVis.visit(ctx.qname()).toString(),
    //                     DashRef.emptyParamValuesList()));
    // }

    @Override
    public DashGoto visitDashGotoRef1(DashParser.DashGotoRef1Context ctx) {
        return new DashGoto(
                new Pos(ctx),
                this.exprParseVis.visitDashRef1WithKind(
                        ctx.dashRef1(), DashStrings.DashRefKind.STATE));
    }

    @Override
    public DashGoto visitDashGotoRef2(DashParser.DashGotoRef2Context ctx) {
        return new DashGoto(
                new Pos(ctx),
                this.exprParseVis.visitDashRef2WithKind(
                        ctx.dashRef2(), DashStrings.DashRefKind.STATE));
    }
}
