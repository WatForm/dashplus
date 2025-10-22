package ca.uwaterloo.watform.alloyast.paragraph.sig;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParseVis;
import ca.uwaterloo.watform.alloyast.expr.var.AlloySigRefExpr;
import ca.uwaterloo.watform.utils.ParserUtil;
import ca.uwaterloo.watform.utils.Pos;

public final class AlloySigRelParseVis extends AlloyBaseVisitor<AlloySigPara.Rel> {
    private final AlloyExprParseVis exprParseVis = new AlloyExprParseVis();

    @Override
    public AlloySigPara.Extends visitExtendSigIn(AlloyParser.ExtendSigInContext ctx) {
        return new AlloySigPara.Extends(
                new Pos(ctx), (AlloySigRefExpr) exprParseVis.visit(ctx.sigRef()));
    }

    @Override
    public AlloySigPara.In visitInSigIn(AlloyParser.InSigInContext ctx) {
        return new AlloySigPara.In(
                new Pos(ctx),
                ParserUtil.visitAll(ctx.sigRef(), exprParseVis, AlloySigRefExpr.class));
    }

    @Override
    public AlloySigPara.Equal visitEqualSigIn(AlloyParser.EqualSigInContext ctx) {
        return new AlloySigPara.Equal(
                new Pos(ctx),
                ParserUtil.visitAll(ctx.sigRef(), exprParseVis, AlloySigRefExpr.class));
    }
}
