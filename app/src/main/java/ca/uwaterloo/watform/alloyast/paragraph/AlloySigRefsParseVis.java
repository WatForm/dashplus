package ca.uwaterloo.watform.alloyast.paragraph;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParseVis;
import ca.uwaterloo.watform.alloyast.expr.var.AlloySigRefExpr;
import ca.uwaterloo.watform.utils.ParserUtil;
import java.util.List;

public final class AlloySigRefsParseVis extends AlloyBaseVisitor<List<AlloySigRefExpr>> {
    @Override
    public List<AlloySigRefExpr> visitSigRefs(AlloyParser.SigRefsContext ctx) {
        return ParserUtil.visitAll(ctx.sigRef(), new AlloyExprParseVis(), AlloySigRefExpr.class);
    }
}
