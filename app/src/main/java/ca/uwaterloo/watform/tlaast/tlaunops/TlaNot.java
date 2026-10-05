package ca.uwaterloo.watform.tlaast.tlaunops;

import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaNot extends TlaUnaryOp {

  /*
  ~(exp)

  logical negation
  */

  public TlaNot(TlaExp operand) {
    super(operand, TlaOperator.PrecedenceGroup.NOT);
  }

  @Override
  public String toTLAPlusSnippetCore() {
    return TlaStrings.NOT + this.getTLASnippetOfChild(this.operand);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
