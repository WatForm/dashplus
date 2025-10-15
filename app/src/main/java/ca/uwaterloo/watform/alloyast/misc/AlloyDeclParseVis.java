package ca.uwaterloo.watform.alloyast.misc;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import antlr.generated.AlloyParser.NameContext;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParseVis;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class AlloyDeclParseVis extends AlloyBaseVisitor<AlloyDecl> {
	@Override
	public AlloyDecl visitDecl(AlloyParser.DeclContext ctx) {
		AlloyExprParseVis exprParsVis = new AlloyExprParseVis();
		final Boolean disj1 = null != ctx.DISJ(0) ? true : false;
		List<AlloyNameExpr> names = new ArrayList<>();
		for (NameContext nameCtx : ctx.names().name()) {
			names.add((AlloyNameExpr) exprParsVis.visit(nameCtx));
		}
		final Boolean disj2 = null != ctx.DISJ(1) ? true : false;
		Optional<AlloyDecl.Quant> quant;
		if(null != ctx.LONE()) {
			quant = Optional.of(AlloyDecl.Quant.LONE);
		} else if(null != ctx.ONE()) {
			quant = Optional.of(AlloyDecl.Quant.ONE);
		} else if(null != ctx.SOME()) {
			quant = Optional.of(AlloyDecl.Quant.SOME);
		} else if (null != ctx.SET()) {
			quant = Optional.of(AlloyDecl.Quant.SET);
		} else {
			quant = Optional.empty();
		}
		return new AlloyDecl(new Pos(ctx), disj1, names, disj2, quant, exprParsVis.visit(ctx.expr1()));
	}
}

