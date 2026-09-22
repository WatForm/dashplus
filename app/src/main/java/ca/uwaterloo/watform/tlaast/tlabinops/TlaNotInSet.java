package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaNotInSet extends TlaInfixBinOp {

  /*
  element \notin Set
  */

  public TlaNotInSet(TlaExp operandOne, TlaExp operandTwo) {
    super(
        TlaStrings.SET_NOT_IN,
        operandOne,
        operandTwo,
        TlaOperator.Associativity.UNSAFE,
        PrecedenceGroup.SET_MEMBERSHIP);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
