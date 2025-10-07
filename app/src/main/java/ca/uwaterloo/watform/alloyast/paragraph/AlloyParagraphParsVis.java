package ca.uwaterloo.watform.alloyast.paragraph;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParsVis;
import ca.uwaterloo.watform.alloyast.expr.misc.*;

public final class AlloyParagraphParsVis extends AlloyBaseVisitor<AlloyParagraph> {
	private final AlloyExprParsVis exprParsVis;

	public AlloyParagraphParsVis() {
		super();
		this.exprParsVis = new AlloyExprParsVis();
	}

	@Override
	public AlloyParagraph visitParagraph(AlloyParser.ParagraphContext ctx) {
		System.out.println("Visiting ParagraphContext");
		return this.visit(ctx.getChild(0));
	}

	@Override
	public AlloyParagraph visitModulePara(AlloyParser.ModuleParaContext ctx) { return visitChildren(ctx); }

	@Override 
	public AlloyParagraph visitImportPara(AlloyParser.ImportParaContext ctx) { return visitChildren(ctx); }

	@Override
	public AlloyParagraph visitSigPara(AlloyParser.SigParaContext ctx) { return visitChildren(ctx); }

	@Override
	public AlloyParagraph visitEnumPara(AlloyParser.EnumParaContext ctx) { return visitChildren(ctx); }

	@Override
	public AlloyParagraph visitFactPara(AlloyParser.FactParaContext ctx) {
		System.out.println("Visiting FactParaContext");
		AlloyExpr b = this.exprParsVis.visit(ctx.block());
		return new AlloyFactPara();
	}

	@Override
	public AlloyParagraph visitPredPara(AlloyParser.PredParaContext ctx) { return visitChildren(ctx); }

	@Override
	public AlloyParagraph visitFunPara(AlloyParser.FunParaContext ctx) { return visitChildren(ctx); }

	@Override
	public AlloyParagraph visitAssertPara(AlloyParser.AssertParaContext ctx) { return visitChildren(ctx); }

	@Override
	public AlloyParagraph visitMacroPara(AlloyParser.MacroParaContext ctx) { return visitChildren(ctx); }
}

