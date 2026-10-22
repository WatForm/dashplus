package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaLesser extends TlaInfixBinOp {

  /*
  exp1 < exp2

  */

  public TlaLesser(TlaExp operandOne, TlaExp operandTwo) {
    super(
        TlaStrings.LESSER_THAN,
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
