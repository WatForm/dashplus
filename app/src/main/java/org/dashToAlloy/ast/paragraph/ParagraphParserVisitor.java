package org.dashToAlloy.ast.paragraph;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;

public class ParagraphParserVisitor extends AlloyBaseVisitor<Paragraph> {
	@Override
	public Paragraph visitParagraph(AlloyParser.ParagraphContext ctx) {
		return this.visit(ctx.getChild(0));
	}

	@Override
	public Paragraph visitModulePara(AlloyParser.ModuleParaContext ctx) { return visitChildren(ctx); }

	@Override 
	public Paragraph visitImportPara(AlloyParser.ImportParaContext ctx) { return visitChildren(ctx); }

	@Override
	public Paragraph visitSigPara(AlloyParser.SigParaContext ctx) { return visitChildren(ctx); }

	@Override
	public Paragraph visitEnumPara(AlloyParser.EnumParaContext ctx) { return visitChildren(ctx); }

	@Override
	public Paragraph visitFactPara(AlloyParser.FactParaContext ctx) {
		System.out.println("Visiting Fact. ");
		return new FactPara();
	}

	@Override
	public Paragraph visitPredPara(AlloyParser.PredParaContext ctx) { return visitChildren(ctx); }

	@Override
	public Paragraph visitFunPara(AlloyParser.FunParaContext ctx) { return visitChildren(ctx); }

	@Override
	public Paragraph visitAssertPara(AlloyParser.AssertParaContext ctx) { return visitChildren(ctx); }

	@Override
	public Paragraph visitMacroPara(AlloyParser.MacroParaContext ctx) { return visitChildren(ctx); }
}

