package ca.uwaterloo.watform.alloyast.paragraph.sig;

import antlr.generated.*;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.utils.Pos;

public final class AlloySigQualParseVis extends DashBaseVisitor<AlloySigPara.Qual> {
    @Override
    public AlloySigPara.Qual visitSigQualifier(DashParser.SigQualifierContext ctx) {
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
            throw AlloyASTImplError.invalidCase(new Pos(ctx));
        }
    }
}
