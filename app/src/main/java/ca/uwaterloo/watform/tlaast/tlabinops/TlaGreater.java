package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaGreater extends TlaInfixBinOp {

  /*
  exp1 > exp2
  */

  public TlaGreater(TlaExp operandOne, TlaExp operandTwo) {
    super(
        TlaStrings.GREATER_THAN,
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
