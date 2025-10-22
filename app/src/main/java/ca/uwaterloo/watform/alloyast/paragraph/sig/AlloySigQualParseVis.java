package ca.uwaterloo.watform.alloyast.paragraph.sig;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.*;

public final class AlloySigQualParseVis extends AlloyBaseVisitor<AlloySigPara.Qual> {
    @Override
    public AlloySigPara.Qual visitSigQualifier(AlloyParser.SigQualifierContext ctx) {
        if (null != ctx.VAR()) {
            return AlloySigPara.Qual.VAR;
        } else if (null != ctx.ABSTRACT()) {
            return AlloySigPara.Qual.ABSTRACT;
        } else if (null != ctx.PRIVATE()) {
            return AlloySigPara.Qual.PRIVATE;
        } else if (null != ctx.LONE()) {
            return AlloySigPara.Qual.LONE;
        } else if (null != ctx.ONE()) {
            return AlloySigPara.Qual.ONE;
        } else if (null != ctx.SOME()) {
            return AlloySigPara.Qual.SOME;
        } else {
            throw new AlloyUnexpTokenEx(ctx);
        }
    }
}
