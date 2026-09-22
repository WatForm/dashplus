package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaAnd extends TlaInfixBinOp {

  /*
  exp1 /\ exp2
  */

  public TlaAnd(TlaExp operandOne, TlaExp operandTwo) {
    super(
        TlaStrings.AND,
        operandOne,
        operandTwo,
        TlaOperator.Associativity.IRRELEVANT,
        TlaOperator.PrecedenceGroup.AND_OR);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
