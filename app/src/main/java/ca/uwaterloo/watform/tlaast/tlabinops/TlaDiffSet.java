package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaDiffSet extends TlaInfixBinOp {

  /*
  S1 \X S2

  set difference
  */

  public TlaDiffSet(TlaExp operandOne, TlaExp operandTwo) {
    super(
        TlaStrings.SET_DIFFERENCE,
        operandOne,
        operandTwo,
        TlaOperator.Associativity.LEFT,
        TlaOperator.PrecedenceGroup.SET_OPERATORS);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
