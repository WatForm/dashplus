package ca.uwaterloo.watform.tlaast.tlaunops;

import ca.uwaterloo.watform.tlaast.TlaOperator;
import ca.uwaterloo.watform.tlaast.TlaStrings;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaPrime extends TlaUnaryOp {

  /*
  VARIABLES V

  F == V' ...

  here, V is a TlaVar object, which is the child of a TlaPrime Object

  */

  public TlaPrime(TlaVar operand) {
    super(operand, TlaOperator.PrecedenceGroup.SAFE);
  }

  @Override
  public String toTLAPlusSnippetCore() {
    return this.getTLASnippetOfChild(this.operand) + TlaStrings.PRIME;
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
