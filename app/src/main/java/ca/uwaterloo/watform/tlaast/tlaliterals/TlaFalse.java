package ca.uwaterloo.watform.tlaast.tlaliterals;

import ca.uwaterloo.watform.tlaast.TlaSimpleExp;
import ca.uwaterloo.watform.tlaast.TlaStrings;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaFalse extends TlaSimpleExp {

  /*
  FALSE
  */

  public TlaFalse() {
    super(TlaStrings.FALSE);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
