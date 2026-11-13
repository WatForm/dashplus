package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.getNonEmptySubsets;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import java.util.ArrayList;
import java.util.List;

/** Candidate relation values for one quantified variable, plus any unenumerable bindings. */
record QuantifierBindings(List<TupleSet> values, boolean uncertain) {
  static AlloyQtEnum multiplicity(AlloyDecl declaration) {
    return declaration.mul.orElseThrow(
        () ->
            AlloyEvaluatorImplError.unresolvedDeclarationMultiplicity(
                declaration.pos, declaration.toString()));
  }

  static QuantifierBindings of(AlloyDecl declaration, TupleSet domain) {
    AlloyQtEnum multiplicity = multiplicity(declaration);
    List<TupleSet> values = new ArrayList<>();
    boolean includesEmpty = multiplicity == AlloyQtEnum.SET || multiplicity == AlloyQtEnum.LONE;
    if (includesEmpty) values.add(TupleSet.emptySet());
    if (domain.isUnspecified()) return new QuantifierBindings(values, true);

    if (multiplicity == AlloyQtEnum.ONE || multiplicity == AlloyQtEnum.LONE) {
      for (AtomTuple tuple : domain) {
        values.add(TupleSet.of(List.of(tuple)));
      }
    } else {
      for (List<AtomTuple> subset : getNonEmptySubsets(domain)) {
        values.add(TupleSet.of(subset));
      }
    }
    return new QuantifierBindings(values, false);
  }

  static ThreeVal disjointnessOf(
      AlloyDecl declaration, List<TupleSet> pickedInDecl, TupleSet candidate) {
    if (!declaration.isDisj1) return TRUE;
    ThreeVal result = TRUE;
    for (TupleSet picked : pickedInDecl) {
      for (AtomTuple tuple : candidate) {
        result = result.and(picked.contains(tuple).not());
        if (result == FALSE) return FALSE;
      }
    }
    return result;
  }
}
