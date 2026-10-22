package ca.uwaterloo.watform.tlaast.tlaliterals;

import ca.uwaterloo.watform.tlaast.TlaSimpleExp;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaIntLiteral extends TlaSimpleExp {

  /*
  G == 3

  here, 3 is an IntLiteral object, constructed by passing n=3 to the constructor
  */

  public TlaIntLiteral(int n) {
    super(Integer.toString(n));
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
