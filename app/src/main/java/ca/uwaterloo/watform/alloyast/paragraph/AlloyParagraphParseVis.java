package ca.uwaterloo.watform.alloyast.paragraph;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParseVis;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.module.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara.AlloyModuleArg;
import ca.uwaterloo.watform.alloyast.paragraph.fact.*;
import ca.uwaterloo.watform.utils.*;
import java.util.List;

public final class AlloyParagraphParseVis extends AlloyBaseVisitor<AlloyParagraph> {
	private final AlloyExprParseVis exprParsVis = new AlloyExprParseVis();

	@Override
	public AlloyParagraph visitParagraph(AlloyParser.ParagraphContext ctx) {
		return this.visit(ctx.getChild(0));
	}

	// ====================================================================================
	// ModulePara
	// ====================================================================================
	@Override
	public AlloyModulePara visitModulePara(AlloyParser.ModuleParaContext ctx) {
		return new AlloyModulePara(
				new Pos(ctx),
				(AlloyQnameExpr)exprParsVis.visit(ctx.qname()),
				(List<AlloyModuleArg>) ParserUtil.visitAll(ctx.moduleArg(),
						new AlloyModuleArgParseVis()));
	}

	@Override
	public AlloyParagraph visitImportPara(AlloyParser.ImportParaContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public AlloyParagraph visitSigPara(AlloyParser.SigParaContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public AlloyParagraph visitEnumPara(AlloyParser.EnumParaContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public AlloyFactPara visitFactPara(AlloyParser.FactParaContext ctx) {
		String factName = "";
		if (null != ctx.name()) {
			factName = ctx.name().ID().getText();
		} else if (null != ctx.STRING_LITERAL()) {
			factName = ctx.STRING_LITERAL().getText();
		}
		return new AlloyFactPara(
				new Pos(ctx), factName, (AlloyBlock) this.exprParsVis.visit(ctx.block()));
	}

	@Override
	public AlloyParagraph visitPredPara(AlloyParser.PredParaContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public AlloyParagraph visitFunPara(AlloyParser.FunParaContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public AlloyParagraph visitAssertPara(AlloyParser.AssertParaContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public AlloyParagraph visitMacroPara(AlloyParser.MacroParaContext ctx) {
		return visitChildren(ctx);
	}
}
