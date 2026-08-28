package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.AlloyStrings.Kind;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyexprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.alloymodel.Qname;
import ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection;
import ca.uwaterloo.watform.utils.*;
import java.util.List;

public class SetEvaluator implements AlloyExprVis<TupleSet> {
  private final EvaluationTable evaluationTable;
  private final FormulaEvaluator formulaEvaluator;
  private final EvalLogger logger;

  public SetEvaluator(
      EvaluationTable evaluationTable, boolean debug, FormulaEvaluator formulaEvaluator) {
    this.evaluationTable = evaluationTable;
    this.logger = EvalLoggerFactory.make("evaluation", debug);
    this.formulaEvaluator = formulaEvaluator;
  }

  private static Qname qnameOf(AlloyQnameExpr expr) {
    return switch (expr.kind) {
      case SIG, FIELD, PREDFUN -> Qname.alloyQnameExprToQname(expr);
      case UNKNOWN_KIND -> Qname.unknownQname(expr.getName());
    };
  }

  // Unimplemented — error message carries all needed detail
  public TupleSet visit(AlloyBinaryExpr binExpr) {
    throw AlloyEvaluatorImplError.missingVisitCase(
        "SetEvaluator",
        binExpr.pos,
        "AlloyBinaryExpr: " + binExpr + " " + binExpr.getClass().getName());
  }

  public TupleSet visit(AlloyUnaryExpr unaryExpr) {
    throw AlloyEvaluatorImplError.missingVisitCase(
        "SetEvaluator",
        unaryExpr.pos,
        "AlloyUnaryExpr: " + unaryExpr + " " + unaryExpr.getClass().getName());
  }

  public TupleSet visit(AlloyVarExpr varExpr) {
    throw AlloyEvaluatorImplError.missingVisitCase(
        "SetEvaluator",
        varExpr.pos,
        "AlloyVarExpr: " + varExpr + " " + varExpr.getClass().getName());
  }

  public TupleSet visit(AlloyBlock block) {
    throw AlloyEvaluatorImplError.missingVisitCase(
        "SetEvaluator", block.pos, "AlloyBlock: " + block + " " + block.getClass().getName());
  }

  public TupleSet visit(AlloyCphExpr comprehensionExpr) {
    logger.enter("ComprehensionExpr " + comprehensionExpr);
    validateDeclarations(comprehensionExpr.decls);
    var valList = mapBy(comprehensionExpr.decls, d -> d.expr.accept(this));
    if (containsMatch(valList, TupleSet::isUnspecified)) {
      logger.exit("ComprehensionExpr " + TupleSet.unspecified());
      return TupleSet.unspecified();
    }
    var result = TupleSet.of(collectTuples(comprehensionExpr, valList, 0, 0, emptyList()));
    logger.exit("ComprehensionExpr " + result);
    return result;
  }

  // TODO: review
  private List<AtomTuple> collectTuples(
      AlloyCphExpr expr, List<TupleSet> sets, int idx1, int idx2, List<AtomTuple> current) {
    if (idx1 == expr.decls.size() - 1 && idx2 == expr.decls.get(idx1).qnames.size() - 1) {
      List<AtomTuple> res = emptyList();
      for (var tuple : sets.get(idx1)) {
        evaluationTable.addRelation(
            qnameOf(expr.decls.get(idx1).qnames.get(idx2)), TupleSet.of(List.of(tuple)));
        current.add(tuple);
        var formEval = expr.body.isEmpty() ? TRUE : expr.body.get().accept(formulaEvaluator);
        evaluationTable.removeRelation(qnameOf(expr.decls.get(idx1).qnames.get(idx2)));
        if (formEval == TRUE) {
          res.add(AtomTuple.concat(current));
        }
        current.removeLast();
      }
      return res;
    } else {
      List<AtomTuple> res = emptyList();
      for (var tuple : sets.get(idx1)) {
        evaluationTable.addRelation(
            qnameOf(expr.decls.get(idx1).qnames.get(idx2)), TupleSet.of(List.of(tuple)));
        current.add(tuple);
        res.addAll(
            collectTuples(
                expr,
                sets,
                idx1 + ((idx2 + 1) / expr.decls.get(idx1).qnames.size()),
                (idx2 + 1) % expr.decls.get(idx1).qnames.size(),
                current));
        evaluationTable.removeRelation(qnameOf(expr.decls.get(idx1).qnames.get(idx2)));
        current.removeLast();
      }
      return res;
    }
  }

  public TupleSet visit(AlloyIteExpr iteExpr) {
    throw AlloyEvaluatorImplError.missingVisitCase(
        "SetEvaluator",
        iteExpr.pos,
        "AlloyIteExpr: " + iteExpr + " " + iteExpr.getClass().getName());
  }

  // TODO: potentially review, may need changing
  public TupleSet visit(AlloyLetExpr letExpr) {
    logger.enter("LetExpr " + letExpr);
    evaluationTable.addStackFrame();
    for (var asn : letExpr.asns) {
      evaluationTable.addRelation(qnameOf(asn.qname), asn.expr.accept(this));
    }
    var result = letExpr.body.accept(this);
    evaluationTable.popStackFrame();
    logger.enter("LetExpr " + result);
    return result;
  }

  public TupleSet visit(AlloyQuantificationExpr quantificationExpr) {
    if (quantificationExpr.quant != AlloyQuantificationExpr.Quant.SUM) {
      throw AlloyEvaluatorImplError.missingVisitCase(
          "SetEvaluator",
          quantificationExpr.pos,
          "AlloyQuantificationExpr: "
              + quantificationExpr
              + " "
              + quantificationExpr.getClass().getName());
    }

    logger.enter("SumQuantificationExpr " + quantificationExpr);
    validateDeclarations(quantificationExpr.decls);
    var values = mapBy(quantificationExpr.decls, declaration -> declaration.expr.accept(this));
    if (containsMatch(values, TupleSet::isUnspecified)) {
      logger.exit("SumQuantificationExpr " + TupleSet.unspecified());
      return TupleSet.unspecified();
    }
    TupleSet result = sumQuantification(quantificationExpr, values, 0, 0);
    logger.exit("SumQuantificationExpr " + result);
    return result;
  }

  private TupleSet sumQuantification(
      AlloyQuantificationExpr expr, List<TupleSet> values, int declIndex, int nameIndex) {
    TupleSet result = evaluationTable.getIntScalar(0, expr.pos);
    boolean last =
        declIndex == expr.decls.size() - 1
            && nameIndex == expr.decls.get(declIndex).qnames.size() - 1;
    Qname name = qnameOf(expr.decls.get(declIndex).qnames.get(nameIndex));
    for (AtomTuple tuple : values.get(declIndex)) {
      evaluationTable.addRelation(name, TupleSet.of(List.of(tuple)));
      TupleSet value;
      if (last) {
        value = expr.body.accept(this);
      } else {
        int nextDecl = declIndex + ((nameIndex + 1) / expr.decls.get(declIndex).qnames.size());
        int nextName = (nameIndex + 1) % expr.decls.get(declIndex).qnames.size();
        value = sumQuantification(expr, values, nextDecl, nextName);
      }
      evaluationTable.removeRelation(name);
      if (value.isUnspecified()) return TupleSet.unspecified();
      result = processPlus(result.getScalar(), value.getScalar(), expr.pos);
    }
    return result;
  }

  static void validateDeclarations(List<AlloyDecl> declarations) {
    for (AlloyDecl declaration : declarations) {
      if (declaration.isDisj2) {
        throw AlloyEvaluatorImplError.unsupportedDisjOnDomain(
            declaration.pos, declaration.toString());
      }
      if (declaration.mul.isPresent() && declaration.mul.get() != AlloyQtEnum.ONE) {
        throw AlloyEvaluatorImplError.unsupportedDeclarationMultiplicity(
            declaration.pos, declaration.toString(), declaration.mul.get());
      }
    }
  }

  public TupleSet visit(AlloyDecl decl) {
    throw AlloyEvaluatorImplError.missingVisitCase(
        "SetEvaluator", decl.pos, "AlloyDecl: " + decl + " " + decl.getClass().getName());
  }

  // TODO: review
  public TupleSet visit(AlloyQnameExpr qName) {
    logger.enter("QName: " + qName);
    if (qName.vars.isEmpty()) {
      throw AlloyEvaluatorImplError.unresolvedQname(qName.pos, qName.toString());
    }
    if (qName.kind == Kind.PREDFUN) {
      var result = createPredFunCall(qName);
      logger.exit("QName = " + result);
      return result;
    }
    Qname relation = qnameOf(qName);
    var result = evaluationTable.get(relation);
    if (result.isEmpty()) {
      throw AlloyEvaluatorImplError.relationNotInInstance(qName.pos, relation);
    }
    logger.exit("QName = " + result.get());
    return result.get();
  }

  public TupleSet visit(AlloyNoneExpr expr) {
    logger.enter("None");
    logger.exit("None = {}");
    return TupleSet.emptySet();
  }

  public TupleSet visit(AlloyIdenExpr expr) {
    logger.enter("Iden");
    var result = evaluationTable.getIden();
    logger.exit("Iden = " + result);
    return result;
  }

  public TupleSet visit(AlloyUnivExpr expr) {
    logger.enter("Univ");
    var result = evaluationTable.getUniv();
    logger.exit("Univ = " + result);
    return result;
  }

  public TupleSet visit(AlloyUnionExpr expr) {
    logger.enter("Union: " + expr);
    var result = TupleSet.union(expr.left.accept(this), expr.right.accept(this));
    logger.exit("Union = " + result);
    return result;
  }

  public TupleSet visit(AlloyIntersExpr expr) {
    logger.enter("Intersect: " + expr);
    var result = TupleSet.intersect(expr.left.accept(this), expr.right.accept(this));
    logger.exit("Intersect = " + result);
    return result;
  }

  public TupleSet visit(AlloyDiffExpr expr) {
    logger.enter("Diff: " + expr);
    var result = TupleSet.diff(expr.left.accept(this), expr.right.accept(this));
    logger.exit("Diff = " + result);
    return result;
  }

  public TupleSet visit(AlloyFunAddExpr expr) {
    logger.enter("Integer addition: " + expr);
    TupleSet left = expr.left.accept(this);
    TupleSet right = expr.right.accept(this);
    var result =
        left.isUnspecified() || right.isUnspecified()
            ? TupleSet.unspecified()
            : processPlus(left.getScalar(), right.getScalar(), expr.pos);
    logger.exit("Integer addition = " + result);
    return result;
  }

  public TupleSet visit(AlloyFunSubExpr expr) {
    logger.enter("Integer subtraction: " + expr);
    TupleSet left = expr.left.accept(this);
    TupleSet right = expr.right.accept(this);
    var result =
        left.isUnspecified() || right.isUnspecified()
            ? TupleSet.unspecified()
            : processMinus(left.getScalar(), right.getScalar(), expr.pos);
    logger.exit("Integer subtraction = " + result);
    return result;
  }

  public TupleSet visit(AlloyFunMulExpr expr) {
    logger.enter("Integer multiplication: " + expr);
    TupleSet left = expr.left.accept(this);
    TupleSet right = expr.right.accept(this);
    var result =
        left.isUnspecified() || right.isUnspecified()
            ? TupleSet.unspecified()
            : processMul(left.getScalar(), right.getScalar(), expr.pos);
    logger.exit("Integer multiplication = " + result);
    return result;
  }

  public TupleSet visit(AlloyFunDivExpr expr) {
    logger.enter("Integer division: " + expr);
    TupleSet left = expr.left.accept(this);
    TupleSet right = expr.right.accept(this);
    var result =
        left.isUnspecified() || right.isUnspecified()
            ? TupleSet.unspecified()
            : processDiv(left.getScalar(), right.getScalar(), expr.pos);
    logger.exit("Integer division = " + result);
    return result;
  }

  public TupleSet visit(AlloyFunRemExpr expr) {
    logger.enter("Integer remainder: " + expr);
    TupleSet left = expr.left.accept(this);
    TupleSet right = expr.right.accept(this);
    var result =
        left.isUnspecified() || right.isUnspecified()
            ? TupleSet.unspecified()
            : processRem(left.getScalar(), right.getScalar(), expr.pos);
    logger.exit("Integer remainder = " + result);
    return result;
  }

  public TupleSet visit(AlloyArrowExpr expr) {
    logger.enter("ArrowProduct: " + expr);
    var result = TupleSet.crossProduct(expr.left.accept(this), expr.right.accept(this));
    logger.exit("ArrowProduct = " + result);
    return result;
  }

  public TupleSet visit(AlloyDotExpr expr) {
    logger.enter("Dot: " + expr);
    var result = TupleSet.join(expr.left.accept(this), expr.right.accept(this));
    logger.exit("Dot = " + result);
    return result;
  }

  public TupleSet visit(AlloyBracketExpr bracketExpr) {
    logger.enter("BoxJoin: " + bracketExpr);
    TupleSet result = bracketExpr.expr.accept(this);
    for (var argument : bracketExpr.exprs) {
      result = TupleSet.join(argument.accept(this), result);
    }
    logger.exit("BoxJoin = " + result);
    return result;
  }

  private TupleSet createPredFunCall(AlloyQnameExpr callableExpr) {
    Qname callable = qnameOf(callableExpr);
    Pos pos = callableExpr.pos;

    var body = evaluationTable.getCallableBody(callable);
    var argumentDecls = evaluationTable.getCallableArguments(callable);
    if (body.isEmpty() || argumentDecls.isEmpty()) {
      throw AlloyEvaluatorImplError.callableNotInEvaluationTable(pos, callable);
    }
    int expectedArguments = flatten(mapBy(argumentDecls.get(), argument -> argument.qnames)).size();

    if (evaluationTable.isPredicate(callable)) {
      return TupleSet.predicateCall(
          callable,
          expectedArguments,
          args -> processGenericPredicate(callable, argumentDecls.get(), body.get(), args));
    }
    return TupleSet.partialFunction(
        callable,
        expectedArguments,
        args -> processGenericFun(callable, argumentDecls.get(), body.get(), args));
  }

  // TODO: review
  private TupleSet processGenericFun(
      Qname function, List<AlloyDecl> argumentDecls, AlloyBlock body, List<TupleSet> args) {
    var argNames = flatten(mapBy(argumentDecls, argument -> argument.qnames));

    if (args.size() != argNames.size()) {
      throw AlloyEvaluatorImplError.callableArgumentCount(
          body.pos, "Function", function, argNames.size(), args.size());
    }

    evaluationTable.addStackFrame();
    try {
      for (int i = 0; i < args.size(); i++) {
        evaluationTable.addRelation(qnameOf(argNames.get(i)), args.get(i));
      }
      return body.exprs.getFirst().accept(this);
    } finally {
      evaluationTable.popStackFrame();
    }
  }

  private ThreeVal processGenericPredicate(
      Qname predicate, List<AlloyDecl> argumentDecls, AlloyBlock body, List<TupleSet> args) {
    var argNames = flatten(mapBy(argumentDecls, argument -> argument.qnames));

    if (args.size() != argNames.size()) {
      throw AlloyEvaluatorImplError.callableArgumentCount(
          body.pos, "Predicate", predicate, argNames.size(), args.size());
    }

    evaluationTable.addStackFrame();
    try {
      for (int i = 0; i < args.size(); i++) {
        evaluationTable.addRelation(qnameOf(argNames.get(i)), args.get(i));
      }
      return body.accept(formulaEvaluator);
    } finally {
      evaluationTable.popStackFrame();
    }
  }

  // TODO: cleanup
  private TupleSet processPlus(Atom first, Atom second, Pos pos) {

    if (first instanceof LabelAtom || second instanceof LabelAtom)
      throw AlloyEvaluatorImplError.arithmeticOperandsNotIntegers(pos, "plus", first, second);

    if (first instanceof IntegerAtom fi && second instanceof IntegerAtom si) {
      return evaluationTable.getIntScalar(fi.value() + si.value(), pos);
    }

    // at least one side has overflowed, so no concrete sum is available
    OverflowDirection da = Atom.directionOf(first);
    OverflowDirection db = Atom.directionOf(second);

    if (da == OverflowDirection.OVERFLOW_UNKNOWN || db == OverflowDirection.OVERFLOW_UNKNOWN) {
      return evaluationTable.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, pos);
    }

    if (da != null && db != null) {
      // both sides overflowed with a known direction
      OverflowDirection result = (da == db) ? da : OverflowDirection.OVERFLOW_UNKNOWN;
      return evaluationTable.getOverflowScalar(result, pos);
    }

    // exactly one side overflowed with a known direction; the other is a concrete integer
    OverflowAtom overflowed = (OverflowAtom) (da != null ? first : second);
    int concreteVal = ((IntegerAtom) (da != null ? second : first)).value();
    OverflowDirection dir = overflowed.direction();

    boolean sameDirection =
        (dir == OverflowDirection.OVERFLOW_UP && concreteVal >= 0)
            || (dir == OverflowDirection.OVERFLOW_DOWN && concreteVal <= 0);

    return evaluationTable.getOverflowScalar(
        sameDirection ? dir : OverflowDirection.OVERFLOW_UNKNOWN, pos);
  }

  private static OverflowDirection flip(OverflowDirection d) {
    return switch (d) {
      case OVERFLOW_UP -> OverflowDirection.OVERFLOW_DOWN;
      case OVERFLOW_DOWN -> OverflowDirection.OVERFLOW_UP;
      case OVERFLOW_UNKNOWN -> OverflowDirection.OVERFLOW_UNKNOWN;
    };
  }

  private TupleSet processMinus(Atom first, Atom second, Pos pos) {

    if (first instanceof LabelAtom || second instanceof LabelAtom)
      throw AlloyEvaluatorImplError.arithmeticOperandsNotIntegers(pos, "minus", first, second);

    if (first instanceof IntegerAtom fi && second instanceof IntegerAtom si) {
      return evaluationTable.getIntScalar(fi.value() - si.value(), pos);
    }

    OverflowDirection da = Atom.directionOf(first);
    OverflowDirection dbRaw = Atom.directionOf(second);

    if (da == OverflowDirection.OVERFLOW_UNKNOWN || dbRaw == OverflowDirection.OVERFLOW_UNKNOWN) {
      return evaluationTable.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, pos);
    }

    if (da != null && dbRaw != null) {
      // both overflowed. Similar to addition, if direction is the same (after negation) - we
      // know the result overflows in that direction
      OverflowDirection db = flip(dbRaw);
      OverflowDirection result = (da == db) ? da : OverflowDirection.OVERFLOW_UNKNOWN;
      return evaluationTable.getOverflowScalar(result, pos);
    }

    if (da != null) {
      // a overflowed, b concrete: negating a concrete, finite-magnitude value is
      // exact, so this is just plus's single-overflow rule with b's sign flipped.
      int negSecond = -((IntegerAtom) second).value();
      boolean sameDirection =
          (da == OverflowDirection.OVERFLOW_UP && negSecond >= 0)
              || (da == OverflowDirection.OVERFLOW_DOWN && negSecond <= 0);
      return evaluationTable.getOverflowScalar(
          sameDirection ? da : OverflowDirection.OVERFLOW_UNKNOWN, pos);
    } else {
      // b overflowed, a concrete: a - b == a + (-b), and b's magnitude is unbounded
      // here -- this is where the asymmetric two's-complement range actually bites.
      int firstVal = ((IntegerAtom) first).value();

      if (dbRaw == OverflowDirection.OVERFLOW_DOWN) {
        // -b > maxInt unconditionally: minimal-magnitude down clears maxInt by a
        // full unit of cushion (|minInt| = |maxInt| + 1) -- no boundary risk.
        boolean sameDirection = firstVal >= 0;
        return evaluationTable.getOverflowScalar(
            sameDirection ? OverflowDirection.OVERFLOW_UP : OverflowDirection.OVERFLOW_UNKNOWN,
            pos);
      } else {
        // dbRaw == OVERFLOW_UP: -b <= minInt, and the minimal-magnitude up value
        // negates to EXACTLY minInt -- in range, not overflow. Needs a strictly
        // negative concrete addend to guarantee clearing that boundary.
        boolean sameDirection = firstVal < 0;
        return evaluationTable.getOverflowScalar(
            sameDirection ? OverflowDirection.OVERFLOW_DOWN : OverflowDirection.OVERFLOW_UNKNOWN,
            pos);
      }
    }
  }

  // TODO: cleanup
  private TupleSet processMul(Atom first, Atom second, Pos pos) {

    if (first instanceof LabelAtom || second instanceof LabelAtom)
      throw AlloyEvaluatorImplError.arithmeticOperandsNotIntegers(pos, "mul", first, second);

    if (first instanceof IntegerAtom fi && second instanceof IntegerAtom si) {
      return evaluationTable.getIntScalar(fi.value() * si.value(), pos);
    }

    // 0 * anything is exactly 0, even if "anything" is out of representable range --
    // this holds regardless of the other side's direction, even OVERFLOW_UNKNOWN
    if (first instanceof IntegerAtom fi0 && fi0.value() == 0)
      return evaluationTable.getIntScalar(0, pos);
    if (second instanceof IntegerAtom si0 && si0.value() == 0)
      return evaluationTable.getIntScalar(0, pos);

    OverflowDirection da = Atom.directionOf(first);
    OverflowDirection db = Atom.directionOf(second);

    if (da == OverflowDirection.OVERFLOW_UNKNOWN || db == OverflowDirection.OVERFLOW_UNKNOWN) {
      return evaluationTable.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, pos);
    }

    // unlike plus, a product's sign is fully determined by the two factors' signs alone,
    // regardless of unknown magnitude -- so once neither factor is zero and both signs
    // are known, the result direction is always definite (never UNKNOWN)
    boolean firstPositive =
        (da != null) ? da == OverflowDirection.OVERFLOW_UP : ((IntegerAtom) first).value() > 0;
    boolean secondPositive =
        (db != null) ? db == OverflowDirection.OVERFLOW_UP : ((IntegerAtom) second).value() > 0;

    if (firstPositive == secondPositive) {
      // negative * negative, or positive * positive: magnitude only grows,
      // always lands strictly past maxInt -- never ambiguous, due to the
      // asymmetric two's-complement range (|minInt| = |maxInt| + 1)
      return evaluationTable.getOverflowScalar(OverflowDirection.OVERFLOW_UP, pos);
    } else {
      // one positive, one negative: the minimal-magnitude overflow-up value,
      // negated, lands exactly on minInt -- in range, not overflow -- so a
      // "down" result can never be asserted from direction alone
      return evaluationTable.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, pos);
    }
  }

  // TODO: cleanup
  private TupleSet processDiv(Atom first, Atom second, Pos pos) {

    if (first instanceof LabelAtom || second instanceof LabelAtom)
      throw AlloyEvaluatorImplError.arithmeticOperandsNotIntegers(pos, "div", first, second);

    if (first instanceof IntegerAtom fi && second instanceof IntegerAtom si) {
      if (si.value() == 0) {
        throw AlloyEvaluatorImplError.arithmeticDivisionByZero(pos, "div");
      }
      return evaluationTable.getIntScalar(fi.value() / si.value(), pos);
    }

    // Unless first in minInt, the resulting value must be 0
    if (first instanceof IntegerAtom fi) {
      OverflowDirection secondDir = Atom.directionOf(second);
      if (secondDir == OverflowDirection.OVERFLOW_UP
          || secondDir == OverflowDirection.OVERFLOW_DOWN) {
        // boundary collision: |minInt| == minimal OVERFLOW_UP magnitude (maxInt + 1),
        // so minInt / (an UP value) could be exactly -1, not 0 -- unresolvable without
        // knowing the divisor's exact magnitude. OVERFLOW_DOWN's minimal magnitude
        // (maxInt + 2) never collides, so it's unaffected.
        boolean boundaryRisk =
            secondDir == OverflowDirection.OVERFLOW_UP && fi.value() == evaluationTable.minInt();
        if (boundaryRisk) {
          return evaluationTable.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, pos);
        }
        return evaluationTable.getIntScalar(0, pos);
      }
    }

    OverflowDirection da = Atom.directionOf(first);
    OverflowDirection db = Atom.directionOf(second);

    if (da == OverflowDirection.OVERFLOW_UNKNOWN || db == OverflowDirection.OVERFLOW_UNKNOWN) {
      return evaluationTable.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, pos);
    }

    if (da != null && db != null) {
      // both out of range: the ratio of two unbounded magnitudes is unconstrained --
      // could land back in representable range -- so not even a direction is safe to assert
      return evaluationTable.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, pos);
    }

    // numerator overflowed, divisor concrete
    if (second instanceof IntegerAtom si) {
      if (si.value() == 1) {
        // identity: same direction, unchanged
        return evaluationTable.getOverflowScalar(da, pos);
      }
      if (si.value() == -1) {
        // negation -- same boundary issue as mul: negating the minimal OVERFLOW_UP
        // value can land exactly on minInt (in range), so UP is never safe to assert;
        // negating OVERFLOW_DOWN always exceeds maxInt, so DOWN -> UP is always safe
        return evaluationTable.getOverflowScalar(
            da == OverflowDirection.OVERFLOW_DOWN
                ? OverflowDirection.OVERFLOW_UP
                : OverflowDirection.OVERFLOW_UNKNOWN,
            pos);
      }

      // may or may not end up within the range
      return evaluationTable.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, pos);
    }

    // numerator overflowed, divisor concrete nonzero: sign is determined, but magnitude
    // depends on the numerator's unknown exact value, so it may or may not still overflow
    return evaluationTable.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, pos);
  }

  // TODO: cleanup
  private TupleSet processRem(Atom first, Atom second, Pos pos) {

    if (first instanceof LabelAtom || second instanceof LabelAtom)
      throw AlloyEvaluatorImplError.arithmeticOperandsNotIntegers(pos, "rem", first, second);

    if (first instanceof IntegerAtom fi && second instanceof IntegerAtom si) {
      if (si.value() == 0) {
        throw AlloyEvaluatorImplError.arithmeticDivisionByZero(pos, "rem");
      }
      return evaluationTable.getIntScalar(fi.value() % si.value(), pos);
    }

    if (first instanceof IntegerAtom fi) {
      OverflowDirection secondDir = Atom.directionOf(second);
      if (secondDir == OverflowDirection.OVERFLOW_UP
          || secondDir == OverflowDirection.OVERFLOW_DOWN) {
        // same boundary collision as div: at minInt / (minimal UP value), quotient is
        // -1 and remainder is 0, not minInt -- unresolvable, same reasoning as above.
        boolean boundaryRisk =
            secondDir == OverflowDirection.OVERFLOW_UP && fi.value() == evaluationTable.minInt();
        if (boundaryRisk) {
          return evaluationTable.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, pos);
        }
        return evaluationTable.getIntScalar(fi.value(), pos);
      }
      return evaluationTable.getOverflowScalar(
          OverflowDirection.OVERFLOW_UNKNOWN, pos); // second overflow is unknown
    }

    if (second instanceof IntegerAtom si) {
      // a % 1 == 0 and a % -1 == 0 for every integer a, regardless of first's
      // magnitude or direction -- must be checked ahead of any direction dispatch
      if (si.value() == 1 || si.value() == -1) {
        return evaluationTable.getIntScalar(0, pos);
      }
      // numerator overflowed, |divisor| > 1: |remainder| < |divisor| so the true
      // exact value depends on the numerator's unresolved true value
      return evaluationTable.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, pos);
    }

    // both overflowed. Cannot determine the exact value or if concretely overflows
    return evaluationTable.getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, pos);
  }

  public TupleSet visit(AlloyTransExpr expr) {
    logger.enter("Transpose: " + expr);
    var result = TupleSet.mapBy(expr.sub.accept(this), t -> AtomTuple.transpose(t));
    logger.exit("Transpose = " + result);
    return result;
  }

  public TupleSet visit(AlloyDomRestrExpr expr) {
    logger.enter("DomainRestrict: " + expr);
    var domain = expr.left.accept(this);
    var relation = expr.right.accept(this);
    TupleSet result =
        TupleSet.filterByThree(relation, t -> domain.contains(AtomTuple.tupleOfFirst(t)));
    logger.exit("DomainRestrict = " + result);
    return result;
  }

  public TupleSet visit(AlloyRngRestrExpr expr) {
    logger.enter("RangeRestrict: " + expr);
    var relation = expr.left.accept(this);
    var range = expr.right.accept(this);
    TupleSet result =
        TupleSet.filterByThree(relation, t -> range.contains(AtomTuple.tupleOfLast(t)));
    logger.exit("RangeRestrict = " + result);
    return result;
  }

  public TupleSet visit(AlloyRelOvrdExpr expr) {
    logger.enter("RelOverride: " + expr);
    var left = expr.left.accept(this);
    var right = expr.right.accept(this);

    var domRight = TupleSet.mapBy(right, e -> AtomTuple.tupleOfFirst(e));
    TupleSet result =
        TupleSet.union(
            TupleSet.filterByThree(left, t -> domRight.contains(AtomTuple.tupleOfFirst(t)).not()),
            right);
    logger.exit("RelOverride = " + result);
    return result;
  }

  private TupleSet evalTransClosure(TupleSet base) {
    if (base.isUnspecified()) return TupleSet.unspecified();
    var collect = base;
    var current = TupleSet.join(base, base);

    while (true) {
      if (current.isUnspecified()) return current;
      if (current.isEmpty()) break;
      collect = TupleSet.union(collect, current);
      current = TupleSet.join(current, base);
    }

    return collect;
  }

  public TupleSet visit(AlloyTransClosExpr expr) {
    logger.enter("TransClosure: " + expr);
    TupleSet result = evalTransClosure(expr.sub.accept(this));
    logger.exit("TransClosure = " + result);
    return result;
  }

  public TupleSet visit(AlloyReflTransClosExpr expr) {
    logger.enter("TransClosure: " + expr);
    TupleSet result = evalTransClosure(expr.sub.accept(this));
    result = TupleSet.union(result, evaluationTable.getIden());
    logger.exit("TransClosure = " + result);
    return result;
  }

  // TODO: Need to check for overflow in future
  public TupleSet visit(AlloyNumExpr expr) {
    logger.enter("NumExpr: " + expr);
    TupleSet result = evaluationTable.getIntScalar(expr.value, expr.pos);
    logger.exit("NumExpr = " + result);
    return result;
  }

  public TupleSet visit(AlloyCardExpr expr) {
    logger.enter("Cardinality: " + expr);
    TupleSet result = evaluationTable.getCardinality(expr.sub.accept(this), expr.pos);
    logger.exit("Cardinality = " + result);
    return result;
  }

  public TupleSet visit(AlloySigIntExpr expr) {
    logger.enter("Int set: " + expr);
    var result = evaluationTable.getIntSet();
    logger.exit("Int set = " + result);
    return result;
  }
}
