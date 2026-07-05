package ca.uwaterloo.watform.paravisitor;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.dashast.DashState;
import ca.uwaterloo.watform.exprvisitor.ReplaceExprVis;
import ca.uwaterloo.watform.utils.ImplementationError;
import java.util.function.Function;

public class TestAndReplaceExprParaVis implements AlloyParaVis<AlloyPara> {

    private ReplaceExprVis testAndReplaceExprVis;

    public TestAndReplaceExprParaVis(
            Function<AlloyExpr, Boolean> test, Function<AlloyExpr, AlloyExpr> replace) {
        this.testAndReplaceExprVis = new ReplaceExprVis(test, replace);
    }

    public AlloyPara visit(AlloySigPara sigPara) {
        return sigPara.rebuild(
                mapBy(sigPara.fields, f -> ((AlloyDecl) testAndReplaceExprVis.visit(f))),
                ((AlloyBlock) sigPara.block.map(b -> testAndReplaceExprVis.visit(b)).orElse(null)));
    }

    public AlloyPara visit(AlloyAssertPara assertPara) {
        return assertPara.rebuild(((AlloyBlock) testAndReplaceExprVis.visit(assertPara.block)));
    }

    public AlloyPara visit(AlloyEnumPara enumPara) {
        // no expressions in this
        return enumPara;
    }

    public AlloyPara visit(AlloyFactPara factPara) {
        return factPara.rebuild(((AlloyBlock) testAndReplaceExprVis.visit(factPara.block)));
    }

    public AlloyPara visit(AlloyFunPara funPara) {
        // this.localEnvPush(funPara.arguments);
        // this testAndReplaceExprVis has context of arguments within it
        AlloyFunPara newFunPara =
                funPara.rebuild(
                        mapBy(funPara.arguments, a -> ((AlloyDecl) testAndReplaceExprVis.visit(a))),
                        testAndReplaceExprVis.visit(funPara.sub),
                        ((AlloyBlock) testAndReplaceExprVis.visit(funPara.block)));
        // this.localEnvPop(funPara.arguments);
        return newFunPara;
    }

    public AlloyPara visit(AlloyImportPara importPara) {
        // contains no expressions
        return importPara;
    }

    public AlloyPara visit(AlloyMacroPara macroPara) {
        return macroPara.rebuild(
                ((AlloyBlock)
                        (macroPara.block.map(b -> testAndReplaceExprVis.visit(b)).orElse(null))),
                (macroPara.sub.map(b -> testAndReplaceExprVis.visit(b)).orElse(null)));
    }

    public AlloyPara visit(AlloyPredPara predPara) {
        // this.localEnvPush(predPara.arguments);
        AlloyPredPara newPredPara =
                predPara.rebuild(
                        mapBy(
                                predPara.arguments,
                                a -> ((AlloyDecl) testAndReplaceExprVis.visit(a))),
                        ((AlloyBlock) testAndReplaceExprVis.visit(predPara.block)));
        // this.localEnvPop(p.arguments);
        return newPredPara;
    }

    public AlloyPara visit(AlloyCmdPara cmdPara) {
        return new AlloyCmdPara(
                cmdPara.pos,
                mapBy(
                        cmdPara.cmdDecls,
                        d ->
                                (d.rebuild(
                                        ((AlloyBlock)
                                                d.constrBlock
                                                        .map(x -> testAndReplaceExprVis.visit(x))
                                                        .orElse(null))))));
    }

    public AlloyPara visit(AlloyModulePara modPara) {
        return modPara;
    }

    public AlloyPara visit(DashState dashPara) {
        throw ImplementationError.notSupported();
    }
}
