package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaImplies extends TlaInfixBinOp {

  /*
  exp1 => exp2

  */

  public TlaImplies(TlaExp operandOne, TlaExp operandTwo) {
    super(
        TlaStrings.IMPLICATION,
        operandOne,
        operandTwo,
        TlaOperator.Associativity.UNSAFE,
        PrecedenceGroup.IMPLICATION);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
