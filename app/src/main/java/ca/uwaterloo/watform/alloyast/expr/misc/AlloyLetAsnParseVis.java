package ca.uwaterloo.watform.alloyast.expr.misc;

import antlr.generated.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyLetExpr.AlloyLetAsn;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.utils.*;

public final class AlloyLetAsnParseVis extends DashBaseVisitor<AlloyLetAsn> {
    @Override
    public AlloyLetAsn visitAssignment(DashParser.AssignmentContext ctx) {
        AlloyExprParseVis exprParseVis = new AlloyExprParseVis();

        return new AlloyLetAsn(
                new Pos(ctx),
                (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
                exprParseVis.visit(ctx.expr1()));
    }
}
