package ca.uwaterloo.watform.dashast;

import static ca.uwaterloo.watform.parser.Parser.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.emptyList;
import static ca.uwaterloo.watform.utils.GeneralUtil.extractItemsOfClass;
import static ca.uwaterloo.watform.utils.GeneralUtil.extractOneFromList;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import antlr.generated.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyArrowExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyUnionExpr;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.dashNamedExpr.*;
import ca.uwaterloo.watform.dashast.dashref.DashExprParseVis;
import ca.uwaterloo.watform.utils.*;
import java.util.List;
import java.util.stream.Collectors;

public final class DashStateItemParseVis extends DashBaseVisitor<DashStateItem> {
    private final DashExprParseVis exprParseVis = new DashExprParseVis();

    private List<String> extractNames(DashParser.NamesContext ctx) {
        if (null != ctx) {
            return visitAll(ctx.name(), exprParseVis, AlloyNameExpr.class).stream()
                    .map(AlloyNameExpr::toString)
                    .collect(Collectors.toList());
        }
        return emptyList();
    }

    private List<String> extractNames(DashParser.QnamesContext ctx) {
        if (null != ctx) {
            return visitAll(ctx.qname(), exprParseVis, AlloyQnameExpr.class).stream()
                    .map(AlloyQnameExpr::toString)
                    .collect(Collectors.toList());
        }
        return emptyList();
    }

    private String extractName(DashParser.QnameContext ctx) {
        if (null == ctx) return "";
        return this.exprParseVis.visit(ctx).toString();
    }

    private String extractName(DashParser.NameContext ctx) {
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
        DashVarDecls.Quant quant = DashVarDecls.Quant.ONE;
        if (null != ctx.LONE()) {
            quant = DashVarDecls.Quant.LONE;
        } else if (null != ctx.SOME()) {
            quant = DashVarDecls.Quant.SOME;
        } else if (null != ctx.SET()) {
            quant = DashVarDecls.Quant.SET;
        }
        AlloyExpr expr = exprParseVis.visit(ctx.expr1());
        if (!(expr instanceof AlloyArrowExpr
                || expr instanceof AlloyQnameExpr
                || expr instanceof AlloyUnionExpr))
            notSupported(
                    " var decl " + expr.toString() + " is of class " + expr.getClass().getName());
        if (quant != null && expr instanceof AlloyArrowExpr) quant = DashVarDecls.Quant.SET;
        return new DashVarDecls(
                new Pos(ctx),
                this.extractNames(ctx.names()),
                quant,
                exprParseVis.visit(ctx.expr1()),
                null != ctx.ENV() ? DashStrings.IntEnvKind.ENV : DashStrings.IntEnvKind.INT);
    }

    @Override
    public DashBufferDecls visitDashBufferDecls(DashParser.DashBufferDeclsContext ctx) {
        return new DashBufferDecls(
                new Pos(ctx),
                this.extractNames(ctx.names()),
                (null != ctx.qname())
                        ? this.extractName(ctx.qname())
                        : new AlloySigIntExpr().toString(),
                DashStrings.IntEnvKind.INT);
    }

    @Override
    public DashTrans visitDashTrans(DashParser.DashTransContext ctx) {
        DashTransItemParseVis transItemParseVis = new DashTransItemParseVis();
        List<DashTransItem> transItems =
                visitAll(ctx.transItem(), transItemParseVis, DashTransItem.class);
        return new DashTrans(
                new Pos(ctx),
                this.extractName(ctx.name()),
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
        if (null != ctx.qname()) {
            return new DashInv(
                    new Pos(ctx),
                    this.extractName(ctx.qname()),
                    this.exprParseVis.visit(ctx.block()));
        } else {
            return new DashInv(new Pos(ctx), this.exprParseVis.visit(ctx.block()));
        }
    }

    @Override
    public DashPred visitDashPred(DashParser.DashPredContext ctx) {
        return new DashPred(
                new Pos(ctx), this.extractName(ctx.name()), this.exprParseVis.visit(ctx.expr1()));
    }

    @Override
    public DashState visitDashState(DashParser.DashStateContext ctx) {
        return new DashState(
                new Pos(ctx),
                this.extractName(ctx.name()),
                DashState.noParam(),
                DashStrings.StateKind.OR,
                null != ctx.DEF() ? DashStrings.DefKind.DEFAULT : DashStrings.DefKind.NOTDEFAULT,
                visitAll(ctx.stateItem(), new DashStateItemParseVis(), Object.class));
    }

    @Override
    public DashState visitDashConcState(DashParser.DashConcStateContext ctx) {
        return new DashState(
                new Pos(ctx),
                this.extractName(ctx.name()),
                null != ctx.qname() ? this.extractName(ctx.qname()) : DashState.noParam(),
                DashStrings.StateKind.AND,
                null != ctx.DEF() ? DashStrings.DefKind.DEFAULT : DashStrings.DefKind.NOTDEFAULT,
                visitAll(ctx.stateItem(), new DashStateItemParseVis(), Object.class));
    }
}
