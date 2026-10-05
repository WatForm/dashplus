package ca.uwaterloo.watform.tlaexpvisitor;

import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaConst;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaIfThenElse;
import ca.uwaterloo.watform.tlaast.TlaLetBinding;
import ca.uwaterloo.watform.tlaast.TlaOperator;
import ca.uwaterloo.watform.tlaast.TlaRecord;
import ca.uwaterloo.watform.tlaast.TlaStdLibs;
import ca.uwaterloo.watform.tlaast.TlaVar;
import java.util.ArrayList;
import java.util.List;

public class TlaApplCollector implements TlaExpVis<List<TlaAppl>> {

  private List<TlaAppl> common(TlaExp exp) {
    List<TlaAppl> answer = new ArrayList<>();
    for (var c : exp.getChildren()) answer.addAll(visit(c));
    return answer;
  }

  @Override
  public List<TlaAppl> visit(TlaOperator OperatorExp) {
    return common(OperatorExp);
  }

  @Override
  public List<TlaAppl> visit(TlaAppl ApplExp) {
    List<TlaAppl> answer = new ArrayList<>();
    answer.add(ApplExp);
    for (var c : ApplExp.getChildren()) answer.addAll(visit(c));
    return answer;
  }

  @Override
  public List<TlaAppl> visit(TlaConst ConstExp) {
    return new ArrayList<>();
  }

  @Override
  public List<TlaAppl> visit(TlaDefn DefnExp) {
    return visit(DefnExp.body);
  }

  @Override
  public List<TlaAppl> visit(TlaIfThenElse ifThenElseExp) {
    return common(ifThenElseExp);
  }

  @Override
  public List<TlaAppl> visit(TlaLetBinding letBindingExp) {
    return common(letBindingExp);
  }

  @Override
  public List<TlaAppl> visit(TlaRecord recordExp) {
    return common(recordExp);
  }

  @Override
  public List<TlaAppl> visit(TlaStdLibs stdLibExp) {
    return new ArrayList<>();
  }

  @Override
  public List<TlaAppl> visit(TlaVar varExp) {
    return new ArrayList<>();
  }

  /*
  when run on a TLA defn, returns a list of TlaAppls that the defn relies on
  */

}
