package ca.uwaterloo.watform.alloyast.expr.var;

import static ca.uwaterloo.watform.parser.Parser.*;

import antlr.generated.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParseVis;
import java.util.List;

public final class AlloySigRefsParseVis extends DashBaseVisitor<List<AlloySigRefExpr>> {
    @Override
    public List<AlloySigRefExpr> visitSigRefs(DashParser.SigRefsContext ctx) {
        return visitAll(ctx.sigRef(), new AlloyExprParseVis(), AlloySigRefExpr.class);
    }
}
