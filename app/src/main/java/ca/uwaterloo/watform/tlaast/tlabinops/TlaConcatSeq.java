package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaConcatSeq extends TlaInfixBinOp {

  /*
  <<a,b,c>> \o <<1,2,3>>

  concatenates sequences
  operandOne: <<a,b,c>> (can be an exp)
  operandTwo: <<1,2,3>> (can be an exp)
  */

  public TlaConcatSeq(TlaExp operandOne, TlaExp operandTwo) {
    super(
        TlaStrings.CONCATENATE,
        operandOne,
        operandTwo,
        TlaOperator.Associativity.IRRELEVANT,
        TlaOperator.PrecedenceGroup.CONCAT);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
