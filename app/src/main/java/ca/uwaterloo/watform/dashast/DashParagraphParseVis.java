package ca.uwaterloo.watform.dashast;

import antlr.generated.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.*;

public final class DashParagraphParseVis extends AlloyParagraphParseVis {
    @Override
    public DashState visitStateRoot(DashParser.StateRootContext ctx) {
        return new DashState(
                new Pos(ctx),
                this.exprParseVis.visit(ctx.qname()).toString(),
                DashState.noParam(),
                DashStrings.StateKind.OR,
                DashStrings.DefKind.NOTDEFAULT,
                ParserUtil.visitAll(ctx.stateItem(), new DashStateItemParseVis(), Object.class));
    }
}
