package ca.uwaterloo.watform.evaluation;

import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.alloymodel.Qname;
import ca.uwaterloo.watform.evaluation.OverflowAtom.OverflowDirection;
import ca.uwaterloo.watform.utils.Pos;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class EvaluationTable {
  private final Deque<Map<Qname, TupleSet>> relations = new ArrayDeque<>();
  private final AtomFactory atomFactory;
  private final TupleSet intSet;
  private final TupleSet intNext;
  private final TupleSet univ;
  private final TupleSet iden;
  private final Map<Qname, List<AlloyDecl>> callableArguments = new LinkedHashMap<>();
  private final Map<Qname, AlloyBlock> callableBodies = new LinkedHashMap<>();
  private final Set<Qname> predicates = new LinkedHashSet<>();

  public EvaluationTable(Instance instance, AlloyModel model) {
    Objects.requireNonNull(instance);
    Objects.requireNonNull(model);
    this.atomFactory = new AtomFactory(instance.minInt(), instance.maxInt());

    for (Qname function : model.allFuns()) {
      AlloyBlock body = model.predFunBody(function);
      if (body.exprs.size() != 1) {
        throw AlloyEvaluatorImplError.functionBodyExpressionCount(
            body.pos, function, body.exprs.size());
      }
      callableArguments.put(function, List.copyOf(model.predFunArgDecls(function)));
      callableBodies.put(function, body);
    }
    for (Qname predicate : model.allPreds()) {
      callableArguments.put(predicate, List.copyOf(model.predFunArgDecls(predicate)));
      callableBodies.put(predicate, model.predFunBody(predicate));
      predicates.add(predicate);
    }

    Map<Qname, TupleSet> base = new LinkedHashMap<>();
    relations.addLast(base);

    List<AtomTuple> univTuples = new ArrayList<>();
    for (Qname sig : instance.allSigQnames()) {
      TupleSet value = sigValue(instance, sig);
      base.put(sig, value);
      for (AtomTuple tuple : value) {
        if (tuple.arity() == 1) univTuples.add(tuple);
      }
    }

    for (Qname field : instance.allFieldQnames()) {
      base.put(field, fieldValue(instance, field));
    }

    List<AtomTuple> intTuples = new ArrayList<>();
    List<AtomTuple> intNextTuples = new ArrayList<>();
    for (int value = instance.minInt(); value <= instance.maxInt(); value++) {
      intTuples.add(tuple(atomFactory.createAtom(value)));
      if (value < instance.maxInt()) {
        intNextTuples.add(
            new AtomTuple(
                List.of(atomFactory.createAtom(value), atomFactory.createAtom(value + 1))));
      }
    }
    this.intSet = TupleSet.of(intTuples);
    this.intNext = TupleSet.of(intNextTuples);
    univTuples.addAll(intTuples);

    this.univ = TupleSet.of(univTuples);

    List<AtomTuple> idenTuples = new ArrayList<>();
    for (AtomTuple value : univ) {
      idenTuples.add(AtomTuple.concat(value, value));
    }
    this.iden = TupleSet.of(idenTuples);
  }

  private TupleSet sigValue(Instance instance, Qname sig) {
    List<AtomTuple> tuples = new ArrayList<>();
    for (String label : instance.getAllSigValues(sig)) {
      tuples.add(tuple(atomFactory.createAtom(label)));
    }
    return TupleSet.of(tuples);
  }

  private TupleSet fieldValue(Instance instance, Qname field) {
    List<AtomTuple> tuples = new ArrayList<>();
    for (List<String> labels : instance.getAllFieldValues(field)) {
      List<Atom> atoms = new ArrayList<>();
      for (String label : labels) atoms.add(atomFactory.createAtom(label));
      tuples.add(new AtomTuple(atoms));
    }
    return TupleSet.of(tuples);
  }

  private static AtomTuple tuple(Atom atom) {
    return new AtomTuple(List.of(atom));
  }

  public Optional<TupleSet> get(Qname relation) {
    for (Map<Qname, TupleSet> frame : relations) {
      TupleSet value = frame.get(relation);
      if (value != null) return Optional.of(value);
    }
    return Optional.empty();
  }

  public Optional<List<AlloyDecl>> getCallableArguments(Qname callable) {
    return Optional.ofNullable(callableArguments.get(callable));
  }

  public Optional<AlloyBlock> getCallableBody(Qname callable) {
    return Optional.ofNullable(callableBodies.get(callable));
  }

  public boolean isPredicate(Qname callable) {
    return predicates.contains(callable);
  }

  public TupleSet getUniv() {
    return univ;
  }

  public TupleSet getIden() {
    return iden;
  }

  public TupleSet getIntSet() {
    return intSet;
  }

  public TupleSet getIntNext() {
    return intNext;
  }

  public TupleSet getIntScalar(int value, Pos pos) {
    return TupleSet.createScalar(atomFactory.createAtom(value, pos));
  }

  public TupleSet getOverflowScalar(OverflowDirection direction, Pos pos) {
    return TupleSet.createScalar(atomFactory.createAtom(direction, pos));
  }

  public TupleSet getCardinality(TupleSet set, Pos pos) {
    if (set.isUnspecified()) return TupleSet.unspecified();
    if (set.containsOverflow()) {
      return getOverflowScalar(OverflowDirection.OVERFLOW_UNKNOWN, pos);
    }
    return getIntScalar(set.size(), pos);
  }

  public int minInt() {
    return atomFactory.minInt();
  }

  public int maxInt() {
    return atomFactory.maxInt();
  }

  public void addStackFrame() {
    relations.addFirst(new LinkedHashMap<>());
  }

  public void popStackFrame() {
    if (relations.size() == 1) {
      throw AlloyEvaluatorImplError.baseEvaluationFrameRemoval();
    }
    relations.removeFirst();
  }

  public void addRelation(Qname relation, TupleSet value) {
    relations.getFirst().put(Objects.requireNonNull(relation), Objects.requireNonNull(value));
  }

  public void removeRelation(Qname relation) {
    relations.getFirst().remove(relation);
  }

  public Map<Qname, TupleSet> baseRelations() {
    return Collections.unmodifiableMap(relations.getLast());
  }
}
