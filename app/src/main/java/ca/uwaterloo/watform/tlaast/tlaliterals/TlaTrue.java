package ca.uwaterloo.watform.tlaast.tlaliterals;

import ca.uwaterloo.watform.tlaast.TlaSimpleExp;
import ca.uwaterloo.watform.tlaast.TlaStrings;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaTrue extends TlaSimpleExp {

  /*
  TRUE
  */

  public TlaTrue() {
    super(TlaStrings.TRUE);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
