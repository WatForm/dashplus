package ca.uwaterloo.watform.alloyast.paragraph.module;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParseVis;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara.AlloyModuleArg;
import ca.uwaterloo.watform.utils.Pos;

public final class AlloyModuleArgParseVis extends AlloyBaseVisitor<AlloyModuleArg> {
    @Override
    public AlloyModuleArg visitModuleArg(AlloyParser.ModuleArgContext ctx) {
        return new AlloyModuleArg(
                new Pos(ctx),
                null != ctx.EXACTLY(),
                (AlloyNameExpr) new AlloyExprParseVis().visit(ctx.name()));
    }
}
