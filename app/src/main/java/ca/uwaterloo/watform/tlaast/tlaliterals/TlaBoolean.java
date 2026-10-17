package ca.uwaterloo.watform.tlaast.tlaliterals;

import ca.uwaterloo.watform.tlaast.TlaSimpleExp;
import ca.uwaterloo.watform.tlaast.TlaStrings;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaBoolean extends TlaSimpleExp {

  /*
  BOOLEAN

  (this is a constant that is equal to {TRUE,FALSE})
  */

  public TlaBoolean() {
    super(TlaStrings.BOOLEAN);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
