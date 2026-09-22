package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaMult extends TlaInfixBinOp {

  /*
  exp1 * exp2

  integer multiplication
  */

  public TlaMult(TlaExp operandOne, TlaExp operandTwo) {
    super(
        TlaStrings.TIMES,
        operandOne,
        operandTwo,
        TlaOperator.Associativity.IRRELEVANT,
        PrecedenceGroup.MULT);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
