package ca.uwaterloo.watform.alloyast.misc;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import antlr.generated.AlloyParser.NameContext;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParsVis;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import java.util.ArrayList;
import java.util.List;

public final class AlloyDeclParsVis extends AlloyBaseVisitor<AlloyDecl> {
	@Override
	public AlloyDecl visitDecl(AlloyParser.DeclContext ctx) {
		AlloyExprParsVis exprParsVis = new AlloyExprParsVis();
		final Boolean disj1 = null != ctx.DISJ(0) ? true : false;
		List<AlloyNameExpr> names = new ArrayList<>();
		for (NameContext nameCtx : ctx.names().name()) {
			names.add((AlloyNameExpr) exprParsVis.visit(nameCtx));
		}
		final Boolean disj2 = null != ctx.DISJ(1) ? true : false;
		AlloyDecl.Quant quant;
		if(null != ctx.LONE()) {
			quant = AlloyDecl.Quant.LONE;
		} else if(null != ctx.ONE()) {
			quant = AlloyDecl.Quant.ONE;
		} else if(null != ctx.SOME()) {
			quant = AlloyDecl.Quant.SOME;
		} else if (null != ctx.SET()) {
			quant = AlloyDecl.Quant.SET;
		} else {
			throw new AlloyUnexpTokenEx(ctx);
		}
		return new AlloyDecl(new Pos(ctx), disj1, names, disj2, quant, exprParsVis.visit(ctx.expr1()));
	}
}

