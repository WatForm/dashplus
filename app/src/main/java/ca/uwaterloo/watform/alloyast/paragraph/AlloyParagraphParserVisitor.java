package ca.uwaterloo.watform.alloyast.paragraph;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.expr.*;

public final class AlloyParagraphParserVisitor extends AlloyBaseVisitor<AlloyParagraph> {
	private final AlloyBlockParserVisitor bpv;

	public AlloyParagraphParserVisitor() {
		super();
		this.bpv = new AlloyBlockParserVisitor();
	}

	@Override
	public AlloyParagraph visitParagraph(AlloyParser.ParagraphContext ctx) {
		System.out.println("Visiting Paragraph");
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
		System.out.println("Visiting Fact");
		AlloyBlock b = this.bpv.visit(ctx.block());
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

