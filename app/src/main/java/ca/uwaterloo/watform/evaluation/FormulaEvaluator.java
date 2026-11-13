package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.*;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyQuantificationExpr.Quant;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyexprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.alloymodel.Qname;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

public class FormulaEvaluator implements AlloyExprVis<ThreeVal> {
  private final EvaluationTable evaluationTable;
  private final AlloyExprVis<TupleSet> setEvaluator;
  private final EvalLogger logger;

  public FormulaEvaluator(EvaluationTable evaluationTable, boolean debug) {
    logger = EvalLoggerFactory.make("evaluation", debug);
    setEvaluator = new SetEvaluator(evaluationTable, debug, this);
    this.evaluationTable = evaluationTable;
  }

  private static Qname qnameOf(AlloyQnameExpr expr) {
    return switch (expr.kind) {
      case SIG, FIELD, PREDFUN -> Qname.alloyQnameExprToQname(expr);
      case UNKNOWN_KIND -> Qname.unknownQname(expr.getName());
    };
  }

  // Concrete AST nodes without a visitor below are deliberately unsupported by this evaluator.
  public ThreeVal visit(AlloyBinaryExpr binExpr) {
    throw AlloyEvaluatorError.unsupportedExpression("FormulaEvaluator", binExpr.pos, binExpr);
  }

  public ThreeVal visit(AlloyUnaryExpr unaryExpr) {
    throw AlloyEvaluatorError.unsupportedExpression("FormulaEvaluator", unaryExpr.pos, unaryExpr);
  }

  public ThreeVal visit(AlloyVarExpr varExpr) {
    throw AlloyEvaluatorError.unsupportedExpression("FormulaEvaluator", varExpr.pos, varExpr);
  }

  public ThreeVal visit(AlloyBracketExpr bracketExpr) {
    return bracketExpr.accept(setEvaluator).evaluatePredicate();
  }

  public ThreeVal visit(AlloyQnameExpr qnameExpr) {
    return qnameExpr.accept(setEvaluator).evaluatePredicate();
  }

  public ThreeVal visit(AlloyPredTotOrdExpr totalOrderExpr) {
    return totalOrderExpr.accept(setEvaluator).evaluatePredicate();
  }

  public ThreeVal visit(AlloyDotExpr dotExpr) {
    return dotExpr.accept(setEvaluator).evaluatePredicate();
  }

  public ThreeVal visit(AlloyCphExpr comprehensionExpr) {
    throw AlloyEvaluatorError.unsupportedExpression(
        "FormulaEvaluator", comprehensionExpr.pos, comprehensionExpr);
  }

  public ThreeVal visit(AlloyIteExpr iteExpr) {
    logger.enter("IfThenElse: " + iteExpr);
    ThreeVal condition = iteExpr.cond.accept(this);
    ThreeVal result =
        switch (condition) {
          case TRUE -> iteExpr.conseq.accept(this);
          case FALSE -> iteExpr.alt.accept(this);
          case UNKNOWN -> UNKNOWN;
        };
    logger.exit("IfThenElse = " + result);
    return result;
  }

  public ThreeVal visit(AlloyLetExpr letExpr) {
    logger.enter("LetExpr " + letExpr);
    evaluationTable.addStackFrame();
    for (var asn : letExpr.asns) {
      evaluationTable.addRelation(qnameOf(asn.qname), asn.expr.accept(setEvaluator));
    }
    var result = letExpr.body.accept(this);
    evaluationTable.popStackFrame();
    logger.exit("LetExpr " + result);
    return result;
  }

  public ThreeVal visit(AlloyQuantificationExpr quantificationExpr) {
    logger.enter("QuantificationExpr " + quantificationExpr);
    if (quantificationExpr.quant == Quant.SUM) {
      throw AlloyEvaluatorImplError.sumQuantifierInBooleanContext(quantificationExpr.pos);
    }
    SetEvaluator.validateDeclarations(quantificationExpr.decls);
    var accumulator = new QuantifierAccumulator(quantificationExpr.quant);
    countQuant(quantificationExpr, 0, 0, List.of(), null, false, accumulator);
    ThreeVal result = accumulator.result();
    logger.exit("QuantificationExpr " + result);
    return result;
  }

  private static final class QuantifierAccumulator {
    private final Quant quant;
    private int definiteTrueCount;
    private boolean foundFalse;
    private boolean uncertain;

    private QuantifierAccumulator(Quant quant) {
      this.quant = quant;
    }

    private void record(ThreeVal value, boolean assignmentContainsOverflow) {
      switch (value) {
        case FALSE -> foundFalse = true;
        case UNKNOWN -> uncertain = true;
        case TRUE -> {
          // Overflow representatives establish existence, but cannot establish an exact count.
          if (assignmentContainsOverflow && (quant == Quant.ONE || quant == Quant.LONE)) {
            uncertain = true;
          } else {
            definiteTrueCount = Math.min(2, definiteTrueCount + 1);
          }
        }
      }
    }

    private boolean settled() {
      return switch (quant) {
        case ALL -> foundFalse;
        case SOME, NO -> definiteTrueCount > 0;
        case ONE, LONE -> definiteTrueCount > 1;
        case SUM -> throw AlloyEvaluatorImplError.sumQuantifierInBooleanContext(Pos.UNKNOWN);
      };
    }

    private ThreeVal result() {
      return switch (quant) {
        case ALL -> foundFalse ? FALSE : uncertain ? UNKNOWN : TRUE;
        case SOME -> definiteTrueCount > 0 ? TRUE : uncertain ? UNKNOWN : FALSE;
        case NO -> definiteTrueCount > 0 ? FALSE : uncertain ? UNKNOWN : TRUE;
        case ONE ->
            definiteTrueCount > 1
                ? FALSE
                : uncertain ? UNKNOWN : convertThree(definiteTrueCount == 1);
        case LONE -> definiteTrueCount > 1 ? FALSE : uncertain ? UNKNOWN : TRUE;
        case SUM -> throw AlloyEvaluatorImplError.sumQuantifierInBooleanContext(Pos.UNKNOWN);
      };
    }
  }

  private void countQuant(
      AlloyQuantificationExpr expr,
      int declIndex,
      int nameIndex,
      List<TupleSet> pickedInDecl,
      QuantifierBindings bindings,
      boolean assignmentContainsOverflow,
      QuantifierAccumulator accumulator) {
    if (declIndex == expr.decls.size()) {
      accumulator.record(expr.body.accept(this), assignmentContainsOverflow);
      return;
    }

    AlloyDecl declaration = expr.decls.get(declIndex);
    if (nameIndex == 0) {
      bindings = QuantifierBindings.of(declaration, declaration.expr.accept(setEvaluator));
      if (bindings.uncertain()) accumulator.uncertain = true;
    }
    Qname name = qnameOf(declaration.qnames.get(nameIndex));
    for (TupleSet binding : bindings.values()) {
      ThreeVal disjointness = QuantifierBindings.disjointnessOf(declaration, pickedInDecl, binding);
      if (disjointness == FALSE) continue;
      if (disjointness == UNKNOWN) {
        accumulator.uncertain = true;
        continue;
      }

      evaluationTable.addStackFrame();
      evaluationTable.addRelation(name, binding);
      try {
        boolean containsOverflow = assignmentContainsOverflow || binding.containsOverflow();
        if (nameIndex == declaration.qnames.size() - 1) {
          countQuant(expr, declIndex + 1, 0, List.of(), null, containsOverflow, accumulator);
        } else {
          List<TupleSet> nextPicked = new ArrayList<>(pickedInDecl);
          nextPicked.add(binding);
          countQuant(
              expr, declIndex, nameIndex + 1, nextPicked, bindings, containsOverflow, accumulator);
        }
      } finally {
        evaluationTable.popStackFrame();
      }
      if (accumulator.settled()) return;
    }
  }

  public ThreeVal visit(AlloyDecl decl) {
    throw AlloyEvaluatorImplError.missingVisitCase(
        "FormulaEvaluator", decl.pos, "AlloyDecl: " + decl + " " + decl.getClass().getName());
  }

  public ThreeVal visit(AlloyBlock block) {
    logger.enter("Block (" + block.exprs.size() + " exprs)");
    var returnVal = TRUE;
    for (var expr : block.exprs) {
      returnVal = returnVal.and(expr.accept(this));
      if (returnVal.shortCircuitsAnd()) {
        logger.exit("Expr evaluated to: " + returnVal);
        return returnVal;
      }
    }
    logger.exit("Block = " + returnVal);
    return returnVal;
  }

  private ThreeVal isOne(TupleSet set) {
    if (set.isUnspecified()) return UNKNOWN;
    if (set.size() != 1) return FALSE;
    return set.containsOverflow() ? UNKNOWN : TRUE;
  }

  public ThreeVal visit(AlloyQtExpr qtExpr) {
    logger.enter("Multiplicity " + qtExpr.qt + ": " + qtExpr.sub);
    var set = qtExpr.sub.accept(setEvaluator);
    if (set.isUnspecified()) {
      logger.exit("Multiplicity " + qtExpr.qt + " = " + UNKNOWN);
      return UNKNOWN;
    }
    var result =
        switch (qtExpr.qt) {
          case AlloyQtEnum.NO -> convertThree(set.isEmpty());
          case AlloyQtEnum.SOME -> convertThree(!set.isEmpty());
          case AlloyQtEnum.ONE -> isOne(set);
          case AlloyQtEnum.LONE -> convertThree(set.isEmpty()).or(isOne(set));
          default ->
              throw AlloyEvaluatorImplError.missingVisitCase(
                  "FormulaEvaluator", qtExpr.pos, "AlloyQtEnum multiplicity: " + qtExpr.qt);
        };
    logger.exit("Multiplicity " + qtExpr.qt + " = " + result);
    return result;
  }

  public ThreeVal visit(AlloyEqualsExpr expr) {
    logger.enter("EQ: " + expr);
    var result =
        TupleSet.threeEquals(expr.left.accept(setEvaluator), (expr.right.accept(setEvaluator)));
    logger.exit("EQ = " + result);
    return result;
  }

  public ThreeVal visit(AlloyNotEqualsExpr expr) {
    logger.enter("NEQ: " + expr);
    var result =
        TupleSet.threeEquals(expr.left.accept(setEvaluator), (expr.right.accept(setEvaluator)))
            .not();
    logger.exit("NEQ = " + result);
    return result;
  }

  public ThreeVal visit(AlloyAndExpr expr) {
    logger.enter("AND: " + expr);
    var leftRes = expr.left.accept(this);
    if (leftRes.shortCircuitsAnd()) {
      logger.exit("AND = " + shortCircuitAndResult() + " (short-circuit)");
      return shortCircuitAndResult();
    }
    var result = leftRes.and(expr.right.accept(this));
    logger.exit("AND = " + result);
    return result;
  }

  public ThreeVal visit(AlloyOrExpr expr) {
    logger.enter("OR: " + expr);
    var leftRes = expr.left.accept(this);
    if (leftRes.shortCircuitsOr()) {
      logger.exit("OR = " + shortCircuitOrResult() + " (short-circuit)");
      return shortCircuitOrResult();
    }
    var result = leftRes.or(expr.right.accept(this));
    logger.exit("OR = " + result);
    return result;
  }

  public ThreeVal visit(AlloyImpliesExpr expr) {
    logger.enter("IMPL: " + expr);
    var leftRes = expr.left.accept(this);
    if (leftRes.shortCircuitImpl()) {
      logger.exit("IMPL = " + shortCircuitImplResult() + " (short-circuit)");
      return shortCircuitImplResult();
    }
    var result = leftRes.impl(expr.right.accept(this));
    logger.exit("IMPL = " + result);
    return result;
  }

  public ThreeVal visit(AlloyIffExpr expr) {
    logger.enter("IFF: " + expr);
    var leftResult = expr.left.accept(this);
    if (leftResult == UNKNOWN) {
      logger.exit("IFF = " + UNKNOWN + " (short-circuit)");
      return UNKNOWN;
    }
    var result = leftResult.iff(expr.right.accept(this));
    logger.exit("IFF = " + result);
    return result;
  }

  public ThreeVal visit(AlloyNegExpr expr) {
    logger.enter("NOT: " + expr);
    var result = expr.sub.accept(this).not();
    logger.exit("NOT = " + result);
    return result;
  }

  public ThreeVal visit(AlloyCmpExpr expr) {
    logger.enter("CMP " + expr.comp + ": " + expr);
    var left = expr.left.accept(setEvaluator);
    var right = expr.right.accept(setEvaluator);
    ThreeVal result;
    if (expr.comp == AlloyCmpExpr.Comp.IN) {
      result = TupleSet.threeSubset(left, right);
    } else {
      result =
          left.isUnspecified() || right.isUnspecified()
              ? UNKNOWN
              : switch (expr.comp) {
                case AlloyCmpExpr.Comp.LESS_THAN ->
                    Atom.threeLessThan(left.getScalar(), right.getScalar());
                case AlloyCmpExpr.Comp.GREATER_THAN ->
                    Atom.threeGreater(left.getScalar(), right.getScalar());
                case AlloyCmpExpr.Comp.LESS_EQUAL ->
                    Atom.threeLessEqual(left.getScalar(), right.getScalar());
                case AlloyCmpExpr.Comp.EQUAL_LESS ->
                    Atom.threeLessEqual(left.getScalar(), right.getScalar());
                case AlloyCmpExpr.Comp.GREATER_EQUAL ->
                    Atom.threeGreaterEqual(left.getScalar(), right.getScalar());
                default ->
                    throw AlloyEvaluatorImplError.missingVisitCase(
                        "FormulaEvaluator", expr.pos, "AlloyCmp comp: " + expr.comp);
              };
    }
    if (expr.neg) result = result.not();
    logger.exit("CMP " + expr.comp + " = " + result);
    return result;
  }
}
