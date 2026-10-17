package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaDot extends TlaInfixBinOp {

  /*
     f.x

  f is a record
     */

  public TlaDot(TlaExp operandOne, TlaExp operandTwo) {
    super(
        TlaStrings.DOT,
        operandOne,
        operandTwo,
        TlaOperator.Associativity.IRRELEVANT,
        TlaOperator.PrecedenceGroup.UNSAFE);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
