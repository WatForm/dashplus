package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaProductSet extends TlaInfixBinOp {

  /*
  S1 \X S2

  Cartesian product
  */

  public TlaProductSet(TlaExp operandOne, TlaExp operandTwo) {
    super(
        TlaStrings.SET_PRODUCT,
        operandOne,
        operandTwo,
        TlaOperator.Associativity.UNSAFE,
        PrecedenceGroup.SET_OPERATORS);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
