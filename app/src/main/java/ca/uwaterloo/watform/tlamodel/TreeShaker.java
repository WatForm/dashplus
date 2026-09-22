package ca.uwaterloo.watform.tlamodel;

import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaComment;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaexpvisitor.TlaApplCollector;
import java.util.ArrayList;
import java.util.List;

public class TreeShaker {
  public static TlaModel removeUnusedDefns(TlaModel tlaModel) {

    TlaModel answer = new TlaModel(tlaModel.name, tlaModel.getInit(), tlaModel.getNext());

    for (var v : tlaModel.module.variables) answer.addVar(v.var(), v.type());
    for (var l : tlaModel.module.extended_libraries) answer.addSTL(l);

    List<TlaDefn> needed = actuallyUsed(tlaModel);
    for (var b : tlaModel.module.body)
      switch (b) {
        case TlaComment c:
          answer.addComment(c);
          break;
        case TlaDefn d:
          if (needed.contains(d)) answer.addDefn(d);
        default:
      }

    return answer;
  }

  private static List<TlaDefn> actuallyUsed(TlaModel tlaModel) {

    List<TlaDefn> answer = new ArrayList<>();

    List<TlaAppl> usedAppls = new ArrayList<>();
    // add Init and Next and constants and invariants and properties
    var collector = new TlaApplCollector();
    usedAppls.addAll(collector.visit(tlaModel.getInit()));
    usedAppls.addAll(collector.visit(tlaModel.getNext()));
    for (var prop : tlaModel.cfg.properties) usedAppls.addAll(collector.visit(prop));
    for (var inv : tlaModel.cfg.invariants) usedAppls.addAll(collector.visit(inv));
    for (var c : tlaModel.cfg.properties) usedAppls.addAll(collector.visit(c));

    boolean changes;
    do {
      changes = false;
      List<TlaDefn> toAddDefns = new ArrayList<>();
      for (var defn : tlaModel.module.getFormulaDefinitions()) {
        for (var a : usedAppls) {
          if (defn.decl.name.equals(a.name)) {
            if (!answer.contains(defn) && toAddDefns.contains(defn)) toAddDefns.add(defn);
          }
        }
      }
      if (toAddDefns.size() != 0) changes = true;
      answer.addAll(toAddDefns);

    } while (changes);

    return answer;
  }
}
