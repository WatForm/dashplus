package ca.uwaterloo.watform.tlaast.tlaliterals;

import ca.uwaterloo.watform.tlaast.TlaSimpleExp;
import ca.uwaterloo.watform.tlaast.TlaStrings;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaStringLiteral extends TlaSimpleExp {

  /*
  G == "abc"

  here, "abc" is an StringLiteral object, constructed by passing s="abc" to the constructor
  */

  public TlaStringLiteral(String s) {
    super(TlaStrings.STRING_START + s + TlaStrings.STRING_END);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
