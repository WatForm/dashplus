package ca.uwaterloo.watform.dashast;

import static ca.uwaterloo.watform.utils.GeneralUtil.emptyList;
import static ca.uwaterloo.watform.utils.GeneralUtil.extractItemsOfClass;
import static ca.uwaterloo.watform.utils.GeneralUtil.extractOneFromList;

import antlr.generated.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.dashNamedExpr.*;
import ca.uwaterloo.watform.dashast.dashref.DashExprParseVis;
import ca.uwaterloo.watform.utils.*;
import java.util.List;
import java.util.stream.Collectors;

public final class DashStateItemParseVis extends DashBaseVisitor<DashStateItem> {
    private final DashExprParseVis exprParseVis = new DashExprParseVis();

    private List<String> extractNames(DashParser.QnamesContext ctx) {
        if (null != ctx) {
            return ParserUtil.visitAll(ctx.qname(), exprParseVis, AlloyQnameExpr.class).stream()
                    .map(AlloyQnameExpr::toString)
                    .collect(Collectors.toList());
        }
        return emptyList();
    }

    private String extractName(DashParser.QnameContext ctx) {
        if (null == ctx) return "";
        return this.exprParseVis.visit(ctx).toString();
    }

    @Override
    public DashEventDecls visitDashEventDecls(DashParser.DashEventDeclsContext ctx) {
        return new DashEventDecls(
                new Pos(ctx),
                this.extractNames(ctx.qnames()),
                null != ctx.ENV() ? DashStrings.IntEnvKind.ENV : DashStrings.IntEnvKind.INT);
    }

    @Override
    public DashVarDecls visitDashVarDecls(DashParser.DashVarDeclsContext ctx) {
        return new DashVarDecls(
                new Pos(ctx),
                this.extractNames(ctx.qnames()),
                exprParseVis.visit(ctx.expr1()),
                null != ctx.ENV() ? DashStrings.IntEnvKind.ENV : DashStrings.IntEnvKind.INT);
    }

    @Override
    public DashBufferDecls visitDashBufferDecls(DashParser.DashBufferDeclsContext ctx) {
        return new DashBufferDecls(
                new Pos(ctx),
                this.extractNames(ctx.qnames()),
                (null != ctx.qname())
                        ? this.extractName(ctx.qname())
                        : new AlloySigIntExpr().toString(),
                DashStrings.IntEnvKind.INT);
    }

    @Override
    public DashTrans visitDashTrans(DashParser.DashTransContext ctx) {
        DashTransItemParseVis transItemParseVis = new DashTransItemParseVis();
        List<DashTransItem> transItems =
                ParserUtil.visitAll(ctx.transItem(), transItemParseVis, DashTransItem.class);
        return new DashTrans(
                new Pos(ctx),
                this.extractName(ctx.qname()),
                (DashFrom)
                        extractOneFromList(
                                extractItemsOfClass(transItems, DashFrom.class), "DashFrom"),
                (DashGoto)
                        extractOneFromList(
                                extractItemsOfClass(transItems, DashGoto.class), "DashGoto"),
                (DashOn)
                        extractOneFromList(extractItemsOfClass(transItems, DashOn.class), "DashOn"),
                (DashSend)
                        extractOneFromList(
                                extractItemsOfClass(transItems, DashSend.class), "DashSend"),
                (DashWhen)
                        extractOneFromList(
                                extractItemsOfClass(transItems, DashWhen.class), "DashWhen"),
                (DashDo)
                        extractOneFromList(
                                extractItemsOfClass(transItems, DashDo.class), "DashDo"));
    }

    @Override
    public DashInit visitDashInit(DashParser.DashInitContext ctx) {
        return new DashInit(new Pos(ctx), this.exprParseVis.visit(ctx.block()));
    }

    @Override
    public DashInv visitDashInv(DashParser.DashInvContext ctx) {
        return new DashInv(
                new Pos(ctx), this.extractName(ctx.qname()), this.exprParseVis.visit(ctx.block()));
    }

    @Override
    public DashEntered visitDashEntered(DashParser.DashEnteredContext ctx) {
        return new DashEntered(new Pos(ctx), this.exprParseVis.visit(ctx.block()));
    }

    @Override
    public DashExited visitDashExited(DashParser.DashExitedContext ctx) {
        return new DashExited(new Pos(ctx), this.exprParseVis.visit(ctx.block()));
    }

    @Override
    public DashPred visitDashPred(DashParser.DashPredContext ctx) {
        return new DashPred(
                new Pos(ctx), this.extractName(ctx.qname()), this.exprParseVis.visit(ctx.expr1()));
    }

    @Override
    public DashState visitDashState(DashParser.DashStateContext ctx) {
        return new DashState(
                new Pos(ctx),
                this.extractName(ctx.qname()),
                DashState.noParam(),
                DashStrings.StateKind.OR,
                null != ctx.DEF() ? DashStrings.DefKind.DEFAULT : DashStrings.DefKind.NOTDEFAULT,
                ParserUtil.visitAll(ctx.stateItem(), new DashStateItemParseVis(), Object.class));
    }

    @Override
    public DashState visitDashConcState(DashParser.DashConcStateContext ctx) {
        return new DashState(
                new Pos(ctx),
                this.extractName(ctx.qname(0)),
                null != ctx.qname(1) ? this.extractName(ctx.qname(1)) : DashState.noParam(),
                DashStrings.StateKind.AND,
                null != ctx.DEF() ? DashStrings.DefKind.DEFAULT : DashStrings.DefKind.NOTDEFAULT,
                ParserUtil.visitAll(ctx.stateItem(), new DashStateItemParseVis(), Object.class));
    }
}
