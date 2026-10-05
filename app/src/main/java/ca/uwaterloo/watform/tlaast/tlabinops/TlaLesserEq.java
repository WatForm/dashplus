package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaLesserEq extends TlaInfixBinOp {

  /*
  exp1 <= exp2
  */

  public TlaLesserEq(TlaExp operandOne, TlaExp operandTwo) {
    super(
        TlaStrings.LESSER_THAN_EQUALS,
        operandOne,
        operandTwo,
        TlaOperator.Associativity.LEFT,
        TlaOperator.PrecedenceGroup.COMPARISON);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
