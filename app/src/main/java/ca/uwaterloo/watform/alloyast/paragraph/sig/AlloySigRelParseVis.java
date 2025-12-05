package ca.uwaterloo.watform.alloyast.paragraph.sig;

import static ca.uwaterloo.watform.utils.ParserUtil.*;

import antlr.generated.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParseVis;
import ca.uwaterloo.watform.alloyast.expr.var.AlloySigRefExpr;
import ca.uwaterloo.watform.utils.Pos;

public final class AlloySigRelParseVis extends DashBaseVisitor<AlloySigPara.Rel> {
    private final AlloyExprParseVis exprParseVis = new AlloyExprParseVis();

    @Override
    public AlloySigPara.Extends visitExtendSigIn(DashParser.ExtendSigInContext ctx) {
        return new AlloySigPara.Extends(
                new Pos(ctx), (AlloySigRefExpr) exprParseVis.visit(ctx.sigRef()));
    }

    @Override
    public AlloySigPara.In visitInSigIn(DashParser.InSigInContext ctx) {
        return new AlloySigPara.In(
                new Pos(ctx), visitAll(ctx.sigRef(), exprParseVis, AlloySigRefExpr.class));
    }

    @Override
    public AlloySigPara.Equal visitEqualSigIn(DashParser.EqualSigInContext ctx) {
        return new AlloySigPara.Equal(
                new Pos(ctx), visitAll(ctx.sigRef(), exprParseVis, AlloySigRefExpr.class));
    }
}
