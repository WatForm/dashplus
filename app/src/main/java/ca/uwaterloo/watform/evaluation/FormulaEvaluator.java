package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.AlloyStrings.Kind;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyQuantificationExpr.Quant;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyexprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.alloymodel.Qname;
import ca.uwaterloo.watform.utils.*;
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

  // These visit cases are unimplemented; just note the type and let the error carry the detail
  public ThreeVal visit(AlloyBinaryExpr binExpr) {
    throw AlloyEvaluatorImplError.missingVisitCase(
        "FormulaEvaluator",
        binExpr.pos,
        "AlloyBinaryExpr: " + binExpr + " " + binExpr.getClass().getName());
  }

  public ThreeVal visit(AlloyUnaryExpr unaryExpr) {
    throw AlloyEvaluatorImplError.missingVisitCase(
        "FormulaEvaluator",
        unaryExpr.pos,
        "AlloyUnaryExpr: " + unaryExpr + " " + unaryExpr.getClass().getName());
  }

  public ThreeVal visit(AlloyVarExpr varExpr) {
    throw AlloyEvaluatorImplError.missingVisitCase(
        "FormulaEvaluator",
        varExpr.pos,
        "AlloyVarExpr: " + varExpr + " " + varExpr.getClass().getName());
  }

  public ThreeVal visit(AlloyBracketExpr bracketExpr) {
    if (!(bracketExpr.expr instanceof AlloyQnameExpr qnameExpr) || qnameExpr.kind != Kind.PREDFUN) {
      throw AlloyEvaluatorImplError.missingVisitCase(
          "FormulaEvaluator",
          bracketExpr.pos,
          "AlloyBracketExpr: " + bracketExpr + " " + bracketExpr.getClass().getName());
    }
    return evaluatePredicate(
        qnameExpr, mapBy(bracketExpr.exprs, expr -> expr.accept(setEvaluator)));
  }

  public ThreeVal visit(AlloyQnameExpr qnameExpr) {
    if (qnameExpr.kind != Kind.PREDFUN) {
      return visit((AlloyVarExpr) qnameExpr);
    }
    return evaluatePredicate(qnameExpr, emptyList());
  }

  private ThreeVal evaluatePredicate(AlloyQnameExpr predicateExpr, List<TupleSet> arguments) {
    Qname predicateName = qnameOf(predicateExpr);
    var predicateBody = evaluationTable.getCallableBody(predicateName);
    var predicateArguments = evaluationTable.getCallableArguments(predicateName);
    if (predicateBody.isEmpty() || predicateArguments.isEmpty()) {
      throw AlloyEvaluatorImplError.predicateNotInEvaluationTable(predicateExpr.pos, predicateName);
    }

    var argumentNames = flatten(mapBy(predicateArguments.get(), argument -> argument.qnames));
    if (arguments.size() != argumentNames.size()) {
      throw AlloyEvaluatorImplError.callableArgumentCount(
          predicateExpr.pos, "Predicate", predicateName, argumentNames.size(), arguments.size());
    }

    evaluationTable.addStackFrame();
    try {
      for (int i = 0; i < arguments.size(); i++) {
        evaluationTable.addRelation(qnameOf(argumentNames.get(i)), arguments.get(i));
      }
      return predicateBody.get().accept(this);
    } finally {
      evaluationTable.popStackFrame();
    }
  }

  public ThreeVal visit(AlloyCphExpr comprehensionExpr) {
    throw AlloyEvaluatorImplError.missingVisitCase(
        "FormulaEvaluator",
        comprehensionExpr.pos,
        "AlloyCphExpr: " + comprehensionExpr + " " + comprehensionExpr.getClass().getName());
  }

  public ThreeVal visit(AlloyIteExpr iteExpr) {
    throw AlloyEvaluatorImplError.missingVisitCase(
        "FormulaEvaluator",
        iteExpr.pos,
        "AlloyIteExpr: " + iteExpr + " " + iteExpr.getClass().getName());
  }

  // TODO: potentially review, may need changing
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

  // TODO: cleanup, potentially add var handling or other edge cases
  public ThreeVal visit(AlloyQuantificationExpr quantificationExpr) {
    logger.enter("QuantificationExpr " + quantificationExpr);
    var valList = mapBy(quantificationExpr.decls, d -> d.expr.accept(setEvaluator));
    if (containsMatch(valList, TupleSet::isUnspecified)) {
      logger.exit("QuantificationExpr " + UNKNOWN);
      return UNKNOWN;
    }
    var resCount = countQuant(quantificationExpr, valList, 0, 0);
    var result =
        switch (quantificationExpr.quant) {
          case Quant.ALL -> resCount.falseCnt > 0 ? FALSE : (resCount.unkCnt > 0 ? UNKNOWN : TRUE);
          case Quant.NO -> resCount.trueCnt > 0 ? FALSE : (resCount.unkCnt > 0 ? UNKNOWN : TRUE);
          case Quant.LONE -> resCount.trueCnt > 1 ? FALSE : (resCount.unkCnt > 0 ? UNKNOWN : TRUE);
          case Quant.ONE ->
              resCount.trueCnt > 1
                  ? FALSE
                  : (resCount.unkCnt > 0 ? UNKNOWN : convertThree(resCount.trueCnt == 1));
          case Quant.SOME -> resCount.trueCnt > 0 ? TRUE : (resCount.unkCnt > 0 ? UNKNOWN : FALSE);
          default ->
              throw AlloyEvaluatorImplError.sumQuantifierInBooleanContext(quantificationExpr.pos);
        };
    logger.exit("QuantificationExpr " + result);
    return result;
  }

  private static record ResCount(int trueCnt, int falseCnt, int unkCnt) {}

  // TODO: review
  private ResCount countQuant(
      AlloyQuantificationExpr expr, List<TupleSet> sets, int idx1, int idx2) {
    if (idx1 == expr.decls.size() - 1 && idx2 == expr.decls.get(idx1).qnames.size() - 1) {
      int cntTrue = 0, cntFalse = 0, cntUnk = 0;
      for (var tuple : sets.get(idx1)) {
        evaluationTable.addRelation(
            qnameOf(expr.decls.get(idx1).qnames.get(idx2)), TupleSet.of(List.of(tuple)));
        var res = expr.body.accept(this);
        evaluationTable.removeRelation(qnameOf(expr.decls.get(idx1).qnames.get(idx2)));
        switch (res) {
          case TRUE:
            cntTrue++;
            break;
          case FALSE:
            cntFalse++;
            break;
          default:
            cntUnk++;
            break;
        }
      }
      return new ResCount(cntTrue, cntFalse, cntUnk);
    } else {
      int cntTrue = 0, cntFalse = 0, cntUnk = 0;
      for (var tuple : sets.get(idx1)) {
        evaluationTable.addRelation(
            qnameOf(expr.decls.get(idx1).qnames.get(idx2)), TupleSet.of(List.of(tuple)));
        var res =
            countQuant(
                expr,
                sets,
                idx1 + ((idx2 + 1) / expr.decls.get(idx1).qnames.size()),
                (idx2 + 1) % expr.decls.get(idx1).qnames.size());
        evaluationTable.removeRelation(qnameOf(expr.decls.get(idx1).qnames.get(idx2)));
        cntTrue += res.trueCnt;
        cntFalse += res.falseCnt;
        cntUnk += res.unkCnt;
      }
      return new ResCount(cntTrue, cntFalse, cntUnk);
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
    var result = expr.left.accept(this).iff(expr.right.accept(this));
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
    var result =
        left.isUnspecified() || right.isUnspecified()
            ? UNKNOWN
            : switch (expr.comp) {
              case AlloyCmpExpr.Comp.IN -> TupleSet.threeSubset(left, right);
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
    if (expr.neg) result = result.not();
    logger.exit("CMP " + expr.comp + " = " + result);
    return result;
  }
}
