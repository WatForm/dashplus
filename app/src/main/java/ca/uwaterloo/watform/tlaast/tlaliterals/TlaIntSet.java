package ca.uwaterloo.watform.tlaast.tlaliterals;

import ca.uwaterloo.watform.tlaast.TlaSimpleExp;
import ca.uwaterloo.watform.tlaast.TlaStrings;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaIntSet extends TlaSimpleExp {

  /*

  Int

  this is a TLA+ constant that refers to the set of all integers
  */

  public TlaIntSet() {
    super(TlaStrings.INT_SET);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
