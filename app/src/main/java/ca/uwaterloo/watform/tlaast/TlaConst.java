package ca.uwaterloo.watform.tlaast;

import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaConst extends TlaSimpleExp {

  /*

  CONSTANTS A, B

  G == A + B

  here, A and B are represented by this node

  */

  public TlaConst(String name) {
    super(name);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
