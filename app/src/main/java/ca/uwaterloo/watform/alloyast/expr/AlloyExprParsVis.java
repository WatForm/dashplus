package ca.uwaterloo.watform.alloyast.expr;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import antlr.generated.AlloyParser.AssignmentContext;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.join.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.expr.helper.*;

public final class AlloyExprParsVis extends AlloyBaseVisitor<AlloyExpr> {
	// @Override
	// public AlloyExpr visitAndFormula(AlloyParser.AndFormulaContext ctx) {
	// 	System.out.println("Visiting AndFormulaContext");
	// 	this.visit(ctx.expr(0));
	// 	this.visit(ctx.expr(1));
	// 	return new AlloyAndExpr();
	// }
	//
	// @Override
	// public AlloyExpr visitDotJoin(AlloyParser.DotJoinContext ctx) {
	// 	System.out.println("Visiting DotJoinContext");
	// 	this.visit(ctx.expr(0));
	// 	this.visit(ctx.expr(1));
	// 	return new AlloyDotJoinExpr();
	// }
	//
	// @Override
	// public AlloyExpr visitCardinalityValue(AlloyParser.CardinalityValueContext ctx) {
	// 	System.out.println("Visiting CardinalityValueContext");
	// 	this.visit(ctx.expr());
	// 	return new AlloyCardinalityExpr();
	// }
	//
	// @Override
	// public AlloyExpr visitPrimeValue(AlloyParser.PrimeValueContext ctx) {
	// 	System.out.println("Visiting PrimeValueContext");
	// 	this.visit(ctx.expr());
	// 	return new AlloyPrimeExpr();
	// }
	//
	// @Override
	// public AlloyExpr visitLet(AlloyParser.LetContext ctx) {
	// 	System.out.println("Visiting LetContext");
	//
	//
	// 	AlloyAsnExprHelperParsVis asnExprHelperParsVis = new AlloyAsnExprHelperParsVis();
	// 	for(AssignmentContext asn : ctx.assignment()) {
	// 		asnExprHelperParsVis.visit(asn);
	// 	}
	//
	// 	this.visit(ctx.body());
	//
	// 	return new AlloyLetExpr();
	// }
	//
	// // ============================
	// // Block
	// // ============================
	// @Override
	// public AlloyExpr visitBlock(AlloyParser.BlockContext ctx) {
	// 	System.out.println("Visiting BlockContext");
	// 	for(AlloyParser.ExprContext exprCtx : ctx.expr()) {
	// 		this.visit(exprCtx);
	// 	}
	// 	return new AlloyBlock();
	// }
	//
	//
	// 
	//
	// // ============================
	// // Qname
	// // ============================
	// @Override 
	// public AlloyExpr visitQnameValue(AlloyParser.QnameValueContext ctx) {
	// 	System.out.println("Visiting QnameValueContext");
	// 	return this.visit(ctx.qname());	
	// }
	//
	// @Override 
	// public AlloyExpr visitSimpleQname(AlloyParser.SimpleQnameContext ctx) {
	// 	System.out.println("Visiting SimpleQnameContext");
	// 	return new AlloyQnameExpr();
	// }
	//
	// @Override 
	// public AlloyExpr visitQualifiedQname(AlloyParser.QualifiedQnameContext ctx) {
	// 	System.out.println("Visiting QualifiedQnameContext");
	// 	return new AlloyQnameExpr();
	// }
	//
	//
	// // ============================
	// // Body
	// // ============================
	// 
	// @Override 
	// public AlloyExpr visitBlockBody(AlloyParser.BlockBodyContext ctx) {
	// 	System.out.println("Visiting BlockBodyContext");
	// 	return this.visit(ctx.block());
	// }
	//
	// @Override
	// public AlloyExpr visitBarBody(AlloyParser.BarBodyContext ctx) {
	// 	System.out.println("Visiting BarBodyContext");
	// 	return this.visit(ctx.expr());
	// }
	//



}
