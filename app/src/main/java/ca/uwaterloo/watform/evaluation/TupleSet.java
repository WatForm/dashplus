package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.FALSE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.UNKNOWN;
import static ca.uwaterloo.watform.utils.GeneralUtil.concat;
import static ca.uwaterloo.watform.utils.GeneralUtil.containsMatch;

import ca.uwaterloo.watform.alloymodel.Qname;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/** A value produced while evaluating an Alloy set expression. */
public abstract class TupleSet implements Iterable<AtomTuple> {
  public enum Kind {
    CONCRETE,
    UNSPECIFIED,
    PARTIAL_FUNCTION,
    PREDICATE_CALL
  }

  private static final TupleSet UNSPECIFIED = new UnspecifiedTupleSet();

  public abstract Kind kind();

  protected abstract List<AtomTuple> tuples();

  public final boolean isConcrete() {
    return kind() == Kind.CONCRETE;
  }

  public final boolean isUnspecified() {
    return kind() == Kind.UNSPECIFIED;
  }

  public final boolean isPartialFunction() {
    return kind() == Kind.PARTIAL_FUNCTION;
  }

  public final boolean isPredicateCall() {
    return kind() == Kind.PREDICATE_CALL;
  }

  private boolean isPredFunCall() {
    return this instanceof PredFunTupleSet;
  }

  public Atom getScalar() {
    List<AtomTuple> tuples = concreteTuples();
    if (tuples.size() != 1 || tuples.getFirst().arity() != 1) {
      throw AlloyEvaluatorImplError.scalarRequired(
          tuples.size(), tuples.size() == 1 ? tuples.getFirst().arity() : null);
    }
    return tuples.getFirst().first();
  }

  public ThreeVal contains(AtomTuple check) {
    rejectPredFunCall(this);
    if (isUnspecified()) return UNKNOWN;

    ThreeVal result = FALSE;
    for (AtomTuple tuple : tuples()) {
      result = AtomTuple.threeEqual(tuple, check).or(result);
      if (result.shortCircuitsOr()) return result;
    }
    return result;
  }

  public static ThreeVal threeSubset(TupleSet a, TupleSet b) {
    rejectPredFunCall(a, b);
    if (!a.isConcrete() || !b.isConcrete()) return UNKNOWN;

    ThreeVal result = TRUE;
    for (AtomTuple tuple : a.tuples()) {
      result = result.and(b.contains(tuple));
      if (result.shortCircuitsAnd()) return result;
    }
    return result;
  }

  public static ThreeVal threeEquals(TupleSet a, TupleSet b) {
    rejectPredFunCall(a, b);
    if (!a.isConcrete() || !b.isConcrete()) return UNKNOWN;
    return threeSubset(a, b).and(threeSubset(b, a));
  }

  public boolean containsOverflow() {
    return containsMatch(concreteTuples(), AtomTuple::containsOverflow);
  }

  public boolean isEmpty() {
    return concreteTuples().isEmpty();
  }

  public int size() {
    return concreteTuples().size();
  }

  public static TupleSet of(List<AtomTuple> tuples) {
    return new ConcreteTupleSet(tuples);
  }

  public static TupleSet unspecified() {
    return UNSPECIFIED;
  }

  public static TupleSet partialFunction(
      Qname function, int expectedArguments, Function<List<TupleSet>, TupleSet> evaluator) {
    if (expectedArguments < 0) {
      throw AlloyEvaluatorImplError.negativeCallableArity("Function", function, expectedArguments);
    }
    PartialFunctionTupleSet partial =
        new PartialFunctionTupleSet(
            function, expectedArguments, Collections.emptyList(), evaluator);
    return partial.completeIfReady();
  }

  public static TupleSet predicateCall(
      Qname predicate, int expectedArguments, Function<List<TupleSet>, ThreeVal> evaluator) {
    if (expectedArguments < 0) {
      throw AlloyEvaluatorImplError.negativeCallableArity(
          "Predicate", predicate, expectedArguments);
    }
    return new PredicateTupleSet(predicate, expectedArguments, Collections.emptyList(), evaluator);
  }

  public final ThreeVal evaluatePredicate() {
    if (!(this instanceof PredicateTupleSet predicate)) {
      throw AlloyEvaluatorImplError.predicateValueRequired(kind());
    }
    return predicate.evaluate();
  }

  public static TupleSet union(TupleSet a, TupleSet b) {
    rejectPredFunCall(a, b);
    if (!a.isConcrete() || !b.isConcrete()) return unspecified();
    return of(concat(a.tuples(), b.tuples()));
  }

  public static TupleSet intersect(TupleSet a, TupleSet b) {
    rejectPredFunCall(a, b);
    if (!a.isConcrete() || !b.isConcrete()) return unspecified();
    return filterByThree(a, b::contains);
  }

  public static TupleSet diff(TupleSet a, TupleSet b) {
    rejectPredFunCall(a, b);
    if (!a.isConcrete() || !b.isConcrete()) return unspecified();
    return filterByThree(a, tuple -> b.contains(tuple).not());
  }

  public static TupleSet emptySet() {
    return of(Collections.emptyList());
  }

  public static TupleSet crossProduct(TupleSet a, TupleSet b) {
    rejectPredFunCall(a, b);
    if (!a.isConcrete() || !b.isConcrete()) return unspecified();

    List<AtomTuple> tuples = new ArrayList<>();
    for (AtomTuple at : a.tuples()) {
      for (AtomTuple bt : b.tuples()) {
        tuples.add(AtomTuple.concat(at, bt));
      }
    }
    return of(tuples);
  }

  public static TupleSet join(TupleSet a, TupleSet b) {
    if (a instanceof PredFunTupleSet call) {
      throw call.usageError();
    }
    if (b instanceof PredFunTupleSet call) {
      return call.addArgument(a);
    }
    if (!a.isConcrete() || !b.isConcrete()) return unspecified();

    List<AtomTuple> tuples = new ArrayList<>();
    for (AtomTuple at : a.tuples()) {
      for (AtomTuple bt : b.tuples()) {
        ThreeVal matches = Atom.threeEqual(at.last(), bt.first());
        if (matches == UNKNOWN) return unspecified();
        if (matches == TRUE) tuples.add(AtomTuple.join(at, bt));
      }
    }
    return of(tuples);
  }

  public static TupleSet filterByThree(TupleSet set, Function<AtomTuple, ThreeVal> filter) {
    rejectPredFunCall(set);
    if (!set.isConcrete()) return unspecified();

    List<AtomTuple> tuples = new ArrayList<>();
    for (AtomTuple tuple : set.tuples()) {
      ThreeVal keep = filter.apply(tuple);
      if (keep == UNKNOWN) return unspecified();
      if (keep == TRUE) tuples.add(tuple);
    }
    return of(tuples);
  }

  public static TupleSet mapBy(TupleSet set, Function<AtomTuple, AtomTuple> map) {
    rejectPredFunCall(set);
    if (!set.isConcrete()) return unspecified();
    return of(GeneralUtil.mapBy(set.tuples(), map));
  }

  public static TupleSet createScalar(Atom atom) {
    return of(List.of(new AtomTuple(List.of(atom))));
  }

  @Override
  public Iterator<AtomTuple> iterator() {
    return Collections.unmodifiableList(concreteTuples()).iterator();
  }

  private List<AtomTuple> concreteTuples() {
    if (!isConcrete()) {
      if (this instanceof PredFunTupleSet call) throw call.usageError();
      throw AlloyEvaluatorImplError.nonConcreteTupleAccess(kind());
    }
    return tuples();
  }

  private static void rejectPredFunCall(TupleSet... sets) {
    for (TupleSet set : sets) {
      if (set instanceof PredFunTupleSet call) throw call.usageError();
    }
  }

  private static AlloyEvaluatorImplError partialFunctionUsageError() {
    return AlloyEvaluatorImplError.partialFunctionMisuse();
  }

  private static final class ConcreteTupleSet extends TupleSet {
    private final List<AtomTuple> tuples;

    private ConcreteTupleSet(List<AtomTuple> tuples) {
      Objects.requireNonNull(tuples);
      this.tuples = new ArrayList<>(tuples);
      prune();
    }

    private void prune() {
      for (int i = 0; i < tuples.size(); i++) {
        AtomTuple ti = tuples.get(i);
        for (int j = tuples.size() - 1; j > i; j--) {
          if (AtomTuple.structurallyIdentical(ti, tuples.get(j))) tuples.remove(j);
        }
      }
    }

    @Override
    public Kind kind() {
      return Kind.CONCRETE;
    }

    @Override
    protected List<AtomTuple> tuples() {
      return tuples;
    }

    @Override
    public String toString() {
      return tuples.toString();
    }
  }

  private static final class UnspecifiedTupleSet extends TupleSet {
    @Override
    public Kind kind() {
      return Kind.UNSPECIFIED;
    }

    @Override
    protected List<AtomTuple> tuples() {
      throw AlloyEvaluatorImplError.nonConcreteTupleAccess(kind());
    }

    @Override
    public String toString() {
      return "<unspecified>";
    }
  }

  private abstract static class PredFunTupleSet extends TupleSet {
    protected final Qname callable;
    protected final int expectedArguments;
    protected final List<TupleSet> arguments;

    private PredFunTupleSet(Qname callable, int expectedArguments, List<TupleSet> arguments) {
      this.callable = Objects.requireNonNull(callable);
      this.expectedArguments = expectedArguments;
      this.arguments = List.copyOf(arguments);
    }

    private TupleSet addArgument(TupleSet argument) {
      Objects.requireNonNull(argument);
      if (argument.isPredFunCall()) throw argumentCallUsageError(argument);
      if (arguments.size() >= expectedArguments) {
        throw AlloyEvaluatorImplError.excessCallableArguments(
            callableKind(), callable, expectedArguments, arguments.size() + 1);
      }

      List<TupleSet> updatedArguments = new ArrayList<>(arguments);
      updatedArguments.add(argument);
      return withArguments(updatedArguments);
    }

    protected abstract TupleSet withArguments(List<TupleSet> updatedArguments);

    protected abstract String callableKind();

    protected abstract AlloyEvaluatorImplError usageError();

    @Override
    protected final List<AtomTuple> tuples() {
      throw usageError();
    }
  }

  private static final class PartialFunctionTupleSet extends PredFunTupleSet {
    private final Function<List<TupleSet>, TupleSet> evaluator;

    private PartialFunctionTupleSet(
        Qname function,
        int expectedArguments,
        List<TupleSet> arguments,
        Function<List<TupleSet>, TupleSet> evaluator) {
      super(function, expectedArguments, arguments);
      this.evaluator = Objects.requireNonNull(evaluator);
    }

    @Override
    protected TupleSet withArguments(List<TupleSet> updatedArguments) {
      return new PartialFunctionTupleSet(callable, expectedArguments, updatedArguments, evaluator)
          .completeIfReady();
    }

    private TupleSet completeIfReady() {
      if (arguments.size() < expectedArguments) return this;
      TupleSet result = Objects.requireNonNull(evaluator.apply(arguments));
      if (result.isPredFunCall()) {
        throw AlloyEvaluatorImplError.partialFunctionResult(callable);
      }
      return result;
    }

    @Override
    public Kind kind() {
      return Kind.PARTIAL_FUNCTION;
    }

    @Override
    protected String callableKind() {
      return "Function";
    }

    @Override
    protected AlloyEvaluatorImplError usageError() {
      return partialFunctionUsageError();
    }

    @Override
    public String toString() {
      return "<partial function "
          + callable
          + " ("
          + arguments.size()
          + "/"
          + expectedArguments
          + ")>";
    }
  }

  private static final class PredicateTupleSet extends PredFunTupleSet {
    private final Function<List<TupleSet>, ThreeVal> evaluator;

    private PredicateTupleSet(
        Qname predicate,
        int expectedArguments,
        List<TupleSet> arguments,
        Function<List<TupleSet>, ThreeVal> evaluator) {
      super(predicate, expectedArguments, arguments);
      this.evaluator = Objects.requireNonNull(evaluator);
    }

    @Override
    protected TupleSet withArguments(List<TupleSet> updatedArguments) {
      return new PredicateTupleSet(callable, expectedArguments, updatedArguments, evaluator);
    }

    private ThreeVal evaluate() {
      if (arguments.size() != expectedArguments) {
        throw AlloyEvaluatorImplError.incompletePredicateCall(
            callable, expectedArguments, arguments.size());
      }
      return Objects.requireNonNull(evaluator.apply(arguments));
    }

    @Override
    public Kind kind() {
      return Kind.PREDICATE_CALL;
    }

    @Override
    protected String callableKind() {
      return "Predicate";
    }

    @Override
    protected AlloyEvaluatorImplError usageError() {
      return AlloyEvaluatorImplError.predicateCallMisuse(
          callable, arguments.size(), expectedArguments);
    }

    @Override
    public String toString() {
      return "<predicate " + callable + " (" + arguments.size() + "/" + expectedArguments + ")>";
    }
  }

  private static AlloyEvaluatorImplError argumentCallUsageError(TupleSet argument) {
    return argument instanceof PredFunTupleSet call
        ? call.usageError()
        : partialFunctionUsageError();
  }
}
