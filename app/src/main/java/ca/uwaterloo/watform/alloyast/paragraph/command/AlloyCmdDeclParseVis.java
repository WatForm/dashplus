package ca.uwaterloo.watform.alloyast.paragraph.command;

import antlr.generated.AlloyBaseVisitor;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParseVis;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNumExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyScopableExpr;
import ca.uwaterloo.watform.utils.ParserUtil;
import ca.uwaterloo.watform.utils.Pos;

public final class AlloyCmdDeclParseVis extends AlloyBaseVisitor<AlloyCmdPara.CommandDecl> {
    AlloyExprParseVis exprParseVis = new AlloyExprParseVis();
    AlloyCmdDeclScopeParseVis typescopeParseVis = new AlloyCmdDeclScopeParseVis();

    @Override
    public AlloyCmdPara.CommandDecl visitCommandDecl(AlloyParser.CommandDeclContext ctx) {
        AlloyCmdPara.CommandDecl.CmdType cmdType = null;
        if (null != ctx.RUN()) {
            cmdType = AlloyCmdPara.CommandDecl.CmdType.RUN;
        } else if (null != ctx.CHECK()) {
            cmdType = AlloyCmdPara.CommandDecl.CmdType.CHECK;
        } else {
            throw new AlloyUnexpTokenEx(ctx);
        }

        AlloyQnameExpr declQname = null;
        AlloyQnameExpr invoQname = null;
        AlloyBlock constrBlock = null;
        if (ctx.qname().size() == 0) {
            if (null != ctx.block()) {
                constrBlock = (AlloyBlock) exprParseVis.visit(ctx.block());
            } else {
                throw new AlloyUnexpTokenEx(ctx);
            }
        } else if (ctx.qname().size() == 1) {
            if (null != ctx.block()) {
                declQname = (AlloyQnameExpr) exprParseVis.visit(ctx.qname(0));
                constrBlock = (AlloyBlock) exprParseVis.visit(ctx.block());
            } else {
                invoQname = (AlloyQnameExpr) exprParseVis.visit(ctx.qname(0));
            }
        } else if (ctx.qname().size() == 2) {
            if (null != ctx.block()) {
                throw new AlloyUnexpTokenEx(ctx);
            } else {
                declQname = (AlloyQnameExpr) exprParseVis.visit(ctx.qname(0));
                invoQname = (AlloyQnameExpr) exprParseVis.visit(ctx.qname(1));
            }
        } else {
            throw new AlloyUnexpTokenEx(ctx);
        }

        return new AlloyCmdPara.CommandDecl(
                new Pos(ctx),
                cmdType,
                declQname,
                invoQname,
                constrBlock,
                (null != ctx.scope()) ? this.typescopeParseVis.visit(ctx.scope()) : null,
                (null != ctx.number())
                        ? (AlloyNumExpr) this.exprParseVis.visit(ctx.number())
                        : null);
    }

    public static final class AlloyCmdDeclScopeParseVis
            extends AlloyBaseVisitor<AlloyCmdPara.CommandDecl.Scope> {
        AlloyExprParseVis exprParseVis = new AlloyExprParseVis();
        AlloyCmdDeclScopeTypescopeParseVis typescopeParseVis =
                new AlloyCmdDeclScopeTypescopeParseVis();

        @Override
        public AlloyCmdPara.CommandDecl.Scope visitScope(AlloyParser.ScopeContext ctx) {
            return new AlloyCmdPara.CommandDecl.Scope(
                    new Pos(ctx),
                    (null != ctx.number()) ? (AlloyNumExpr) exprParseVis.visit(ctx.number()) : null,
                    ParserUtil.visitAll(
                            ctx.typescope(),
                            typescopeParseVis,
                            AlloyCmdPara.CommandDecl.Scope.Typescope.class));
        }

        public static final class AlloyCmdDeclScopeTypescopeParseVis
                extends AlloyBaseVisitor<AlloyCmdPara.CommandDecl.Scope.Typescope> {
            AlloyExprParseVis exprParseVis = new AlloyExprParseVis();

            @Override
            public AlloyCmdPara.CommandDecl.Scope.Typescope visitTypescope(
                    AlloyParser.TypescopeContext ctx) {
                AlloyNumExpr start = (AlloyNumExpr) exprParseVis.visit(ctx.number(0));
                AlloyNumExpr end;
                if (null != ctx.number(1)) {
                    end = (AlloyNumExpr) exprParseVis.visit(ctx.number(1));
                } else {
                    if (!ctx.DOT().isEmpty()) {
                        end = new AlloyNumExpr(true, Integer.MAX_VALUE);
                    } else {
                        end = new AlloyNumExpr(start.isPositive, start.label);
                    }
                }
                AlloyNumExpr increment;
                if (null != ctx.number(2)) {
                    increment = (AlloyNumExpr) exprParseVis.visit(ctx.number(2));
                } else {
                    increment = new AlloyNumExpr(true, "1");
                }
                AlloyScopableExpr scopableExpr = null;
                if (ctx.qname() != null) {
                    scopableExpr = (AlloyScopableExpr) exprParseVis.visit(ctx.qname());
                } else if (ctx.SIGINT() != null) {
                    scopableExpr = (AlloyScopableExpr) exprParseVis.visit(ctx.SIGINT());
                } else if (ctx.INT() != null) {
                    scopableExpr = (AlloyScopableExpr) exprParseVis.visit(ctx.INT());
                } else if (ctx.SEQ() != null) {
                    scopableExpr = (AlloyScopableExpr) exprParseVis.visit(ctx.SEQ());
                } else if (ctx.STRING() != null) {
                    scopableExpr = (AlloyScopableExpr) exprParseVis.visit(ctx.STRING());
                } else if (ctx.STEPS() != null) {
                    scopableExpr = (AlloyScopableExpr) exprParseVis.visit(ctx.STEPS());
                } else {
                    throw new AlloyUnexpTokenEx(ctx);
                }
                return new AlloyCmdPara.CommandDecl.Scope.Typescope(
                        new Pos(ctx),
                        null != ctx.EXACTLY() || start == end,
                        start,
                        end,
                        increment,
                        scopableExpr);
            }
        }
    }
}
