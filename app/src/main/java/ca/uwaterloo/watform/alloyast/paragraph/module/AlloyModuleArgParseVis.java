package ca.uwaterloo.watform.alloyast.paragraph.module;

import antlr.generated.*;
import antlr.generated.DashBaseVisitor;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParseVis;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara.AlloyModuleArg;
import ca.uwaterloo.watform.utils.Pos;

public final class AlloyModuleArgParseVis extends DashBaseVisitor<AlloyModuleArg> {
    @Override
    public AlloyModuleArg visitModuleArg(DashParser.ModuleArgContext ctx) {
        return new AlloyModuleArg(
                new Pos(ctx),
                null != ctx.EXACTLY(),
                (AlloyQnameExpr) new AlloyExprParseVis().visit(ctx.qname()));
    }
}
