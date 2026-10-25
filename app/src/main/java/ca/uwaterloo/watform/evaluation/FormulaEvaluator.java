package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyQuantificationExpr.Quant;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyexprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.alloymodel.Qname;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.Arrays;
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
    SetEvaluator.validateDeclarations(quantificationExpr.decls);
    var usedNames = usedDeclarationNames(quantificationExpr);
    var resCount = countQuant(quantificationExpr, usedNames, 0, 0, List.of(), null);
    boolean uncertain = resCount.unkCnt > 0 || resCount.uncertainBindings;
    var result =
        switch (quantificationExpr.quant) {
          case Quant.ALL -> resCount.falseCnt > 0 ? FALSE : (uncertain ? UNKNOWN : TRUE);
          case Quant.NO -> resCount.trueCnt > 0 ? FALSE : (uncertain ? UNKNOWN : TRUE);
          case Quant.LONE -> resCount.trueCnt > 1 ? FALSE : (uncertain ? UNKNOWN : TRUE);
          case Quant.ONE ->
              resCount.trueCnt > 1
                  ? FALSE
                  : (uncertain ? UNKNOWN : convertThree(resCount.trueCnt == 1));
          case Quant.SOME -> resCount.trueCnt > 0 ? TRUE : (uncertain ? UNKNOWN : FALSE);
          default ->
              throw AlloyEvaluatorImplError.sumQuantifierInBooleanContext(quantificationExpr.pos);
        };
    logger.exit("QuantificationExpr " + result);
    return result;
  }

  private static record ResCount(int trueCnt, int falseCnt, int unkCnt, boolean uncertainBindings) {
    private static ResCount of(ThreeVal value) {
      return switch (value) {
        case TRUE -> new ResCount(1, 0, 0, false);
        case FALSE -> new ResCount(0, 1, 0, false);
        case UNKNOWN -> new ResCount(0, 0, 1, false);
      };
    }

    private static ResCount withUncertainBindings() {
      return new ResCount(0, 0, 0, true);
    }

    private ResCount plus(ResCount other) {
      return new ResCount(
          cappedAdd(trueCnt, other.trueCnt),
          cappedAdd(falseCnt, other.falseCnt),
          cappedAdd(unkCnt, other.unkCnt),
          uncertainBindings || other.uncertainBindings);
    }

    private ResCount times(int count) {
      if (count == 0) return new ResCount(0, 0, 0, false);
      return new ResCount(
          cappedMultiply(trueCnt, count),
          cappedMultiply(falseCnt, count),
          cappedMultiply(unkCnt, count),
          uncertainBindings);
    }

    private static int cappedAdd(int first, int second) {
      return Math.min(2, first + second);
    }

    private static int cappedMultiply(int value, int multiplier) {
      if (value == 0 || multiplier == 0) return 0;
      return value == 1 && multiplier == 1 ? 1 : 2;
    }
  }

  private ResCount countQuant(
      AlloyQuantificationExpr expr,
      List<boolean[]> usedNames,
      int declIndex,
      int nameIndex,
      List<AtomTuple> pickedInDecl,
      TupleSet values) {
    if (declIndex == expr.decls.size()) return ResCount.of(expr.body.accept(this));

    AlloyDecl declaration = expr.decls.get(declIndex);
    if (nameIndex == 0) {
      values = declaration.expr.accept(setEvaluator);
    }

    boolean[] declarationUsage = usedNames.get(declIndex);
    boolean wholeDisjointBlockUnused = declaration.isDisj1 && !containsTrue(declarationUsage);
    if (wholeDisjointBlockUnused) {
      if (values.isConcrete()
          && !values.containsOverflow()
          && disjointBindingCount(values.size(), declaration.qnames.size()) == 0) {
        return new ResCount(0, 0, 0, false);
      }
      ResCount branch = countQuant(expr, usedNames, declIndex + 1, 0, List.of(), null);
      if (values.isUnspecified() || (declaration.qnames.size() > 1 && values.containsOverflow())) {
        return repeatUnknownNumberOfTimes(expr.quant, branch);
      }
      return branch.times(disjointBindingCount(values.size(), declaration.qnames.size()));
    }

    if (!declarationUsage[nameIndex]) {
      if (values.isConcrete() && values.isEmpty()) return new ResCount(0, 0, 0, false);
      boolean blockComplete = nameIndex == declaration.qnames.size() - 1;
      ResCount branch =
          blockComplete
              ? countQuant(expr, usedNames, declIndex + 1, 0, List.of(), null)
              : countQuant(expr, usedNames, declIndex, nameIndex + 1, pickedInDecl, values);
      if (values.isUnspecified()) return repeatUnknownNumberOfTimes(expr.quant, branch);
      return branch.times(values.size());
    }

    if (values.isUnspecified()) return ResCount.withUncertainBindings();
    Qname name = qnameOf(declaration.qnames.get(nameIndex));
    ResCount result = new ResCount(0, 0, 0, false);
    for (AtomTuple tuple : values) {
      ThreeVal disjointness = SetEvaluator.disjointnessOf(declaration, pickedInDecl, tuple);
      if (disjointness == FALSE) continue;
      if (disjointness == UNKNOWN) {
        result = result.plus(ResCount.withUncertainBindings());
        continue;
      }

      evaluationTable.addStackFrame();
      evaluationTable.addRelation(name, TupleSet.of(List.of(tuple)));
      ResCount branch;
      try {
        boolean blockComplete = nameIndex == declaration.qnames.size() - 1;
        if (blockComplete) {
          branch = countQuant(expr, usedNames, declIndex + 1, 0, List.of(), null);
        } else {
          List<AtomTuple> nextPicked = new ArrayList<>(pickedInDecl);
          nextPicked.add(tuple);
          branch = countQuant(expr, usedNames, declIndex, nameIndex + 1, nextPicked, values);
        }
      } finally {
        evaluationTable.popStackFrame();
      }
      result = result.plus(branch);
    }
    return result;
  }

  private static ResCount repeatUnknownNumberOfTimes(Quant quant, ResCount branch) {
    // An unspecified domain may have any cardinality, including zero. Collapse the repetition
    // only when every possible repetition count has the same quantified result. In particular,
    // a constant false body does not make `all` false, and a constant true body does not make
    // `some` true, because the domain may be empty.
    boolean repetitionCannotAffectResult =
        switch (quant) {
          case ALL -> branch.falseCnt == 0 && branch.unkCnt == 0 && !branch.uncertainBindings;
          case SOME, NO, ONE, LONE ->
              branch.trueCnt == 0 && branch.unkCnt == 0 && !branch.uncertainBindings;
          case SUM -> false;
        };
    return repetitionCannotAffectResult
        ? new ResCount(0, 0, 0, false)
        : ResCount.withUncertainBindings();
  }

  private static int disjointBindingCount(int domainSize, int nameCount) {
    int result = 1;
    for (int index = 0; index < nameCount; index++) {
      int choices = domainSize - index;
      if (choices <= 0) return 0;
      result = ResCount.cappedMultiply(result, choices);
    }
    return result;
  }

  static List<boolean[]> usedDeclarationNames(AlloyQuantificationExpr expr) {
    List<boolean[]> result = new ArrayList<>();
    for (int declIndex = 0; declIndex < expr.decls.size(); declIndex++) {
      AlloyDecl declaration = expr.decls.get(declIndex);
      boolean[] used = new boolean[declaration.qnames.size()];
      for (int nameIndex = 0; nameIndex < declaration.qnames.size(); nameIndex++) {
        Qname target = qnameOf(declaration.qnames.get(nameIndex));
        used[nameIndex] = VariableUsage.usedAfterDeclaration(expr, declIndex, target);
      }
      if (declaration.isDisj1 && containsTrue(used)) {
        Arrays.fill(used, true);
      }
      result.add(used);
    }
    return result;
  }

  private static boolean containsTrue(boolean[] values) {
    for (boolean value : values) {
      if (value) return true;
    }
    return false;
  }

  private static final class VariableUsage implements AlloyExprVis<Boolean> {
    private final Qname target;

    private VariableUsage(Qname target) {
      this.target = target;
    }

    private static boolean usedAfterDeclaration(
        AlloyQuantificationExpr expression, int declarationIndex, Qname target) {
      VariableUsage usage = new VariableUsage(target);
      for (int index = declarationIndex + 1; index < expression.decls.size(); index++) {
        AlloyDecl declaration = expression.decls.get(index);
        if (declaration.expr.accept(usage)) return true;
        if (usage.bindsTarget(declaration.qnames)) return false;
      }
      return expression.body.accept(usage);
    }

    @Override
    public Boolean visit(AlloyBinaryExpr expression) {
      return expression.left.accept(this) || expression.right.accept(this);
    }

    @Override
    public Boolean visit(AlloyUnaryExpr expression) {
      return expression.sub.accept(this);
    }

    @Override
    public Boolean visit(AlloyVarExpr expression) {
      return false;
    }

    @Override
    public Boolean visit(AlloyQnameExpr expression) {
      return qnameOf(expression).equals(target);
    }

    @Override
    public Boolean visit(AlloyBlock block) {
      return block.exprs.stream().anyMatch(expression -> expression.accept(this));
    }

    @Override
    public Boolean visit(AlloyBracketExpr expression) {
      return expression.expr.accept(this)
          || expression.exprs.stream().anyMatch(argument -> argument.accept(this));
    }

    @Override
    public Boolean visit(AlloyCphExpr expression) {
      return usesBoundExpression(expression.decls, expression.body.orElse(null));
    }

    @Override
    public Boolean visit(AlloyIteExpr expression) {
      return expression.cond.accept(this)
          || expression.conseq.accept(this)
          || expression.alt.accept(this);
    }

    @Override
    public Boolean visit(AlloyLetExpr expression) {
      for (var assignment : expression.asns) {
        if (assignment.expr.accept(this)) return true;
        if (qnameOf(assignment.qname).equals(target)) return false;
      }
      return expression.body.accept(this);
    }

    @Override
    public Boolean visit(AlloyQuantificationExpr expression) {
      return usesBoundExpression(expression.decls, expression.body);
    }

    @Override
    public Boolean visit(AlloyDecl declaration) {
      return declaration.expr.accept(this);
    }

    private boolean usesBoundExpression(List<AlloyDecl> declarations, AlloyExpr body) {
      for (AlloyDecl declaration : declarations) {
        if (declaration.expr.accept(this)) return true;
        if (bindsTarget(declaration.qnames)) return false;
      }
      return body != null && body.accept(this);
    }

    private boolean bindsTarget(List<AlloyQnameExpr> names) {
      return containsMatch(names, name -> qnameOf(name).equals(target));
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
