package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaIntersectionSet extends TlaInfixBinOp {

  /*
  S1 \intersect S2

  set intersection
  */

  public TlaIntersectionSet(TlaExp operandOne, TlaExp operandTwo) {
    super(
        TlaStrings.SET_INTERSECTION,
        operandOne,
        operandTwo,
        TlaOperator.Associativity.IRRELEVANT,
        PrecedenceGroup.SET_OPERATORS);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
