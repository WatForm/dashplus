package ca.uwaterloo.watform.tlamodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.filterBy;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.tlaast.TlaComment;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaexpvisitor.TlaApplCollector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

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

  private static <T> List<T> closure(HashMap<T, List<T>> mapping, List<T> initial) {
    // HashSet keeps track of visited nodes to handle cycles safely
    HashSet<T> visited = new HashSet<>();
    // Stack facilitates the Depth-First Search traversal
    Stack<T> stack = new Stack<>();

    // Initialize the stack and visited set with the initial elements
    if (initial != null) {
      for (T element : initial) {
        if (element != null && visited.add(element)) {
          stack.push(element);
        }
      }
    }

    while (!stack.isEmpty()) {
      T current = stack.pop();

      // Get the neighbors of the current node from the mapping map
      List<T> neighbors = mapping.get(current);
      if (neighbors != null) {
        for (T neighbor : neighbors) {
          // If the neighbor hasn't been visited yet, mark it visited and push to stack
          if (neighbor != null && visited.add(neighbor)) {
            stack.push(neighbor);
          }
        }
      }
    }

    return new ArrayList<>(visited);
  }

  private static List<TlaDefn> actuallyUsed(TlaModel tlaModel) {

    // returns a list of tlaModel defns that are actually used
    // if the body of a TlaDefn A uses a TlaAppl whose name matches the TlaDefn B, then A uses B
    // Init and Next are always used
    // all invariants and properties are also used

    TlaApplCollector collector = new TlaApplCollector();

    HashMap<String, List<String>> useMap = new HashMap<>();
    for (var defn : tlaModel.module.getFormulaDefinitions())
      useMap.put(defn.decl.name, mapBy(collector.visit(defn.body), a -> a.name));

    List<String> initUsed = new ArrayList<>();
    initUsed.add(tlaModel.getInit().name);
    initUsed.add(tlaModel.getNext().name);
    for (var prop : tlaModel.cfg.properties) {
      List<String> usedNames = mapBy(collector.visit(prop), appl -> appl.name);
      for (var s : usedNames) initUsed.add(s);
    }
    for (var inv : tlaModel.cfg.invariants) {
      List<String> usedNames = mapBy(collector.visit(inv), appl -> appl.name);
      for (var s : usedNames) initUsed.add(s);
    }

    List<String> answerFilter = closure(useMap, initUsed);
    return filterBy(
        tlaModel.module.getFormulaDefinitions(), defn -> answerFilter.contains(defn.decl.name));
  }
}
