package ca.uwaterloo.watform.tlaast;

import ca.uwaterloo.watform.tlaast.tlaunops.TlaPrime;
import ca.uwaterloo.watform.tlaexpvisitor.TlaExpVis;

public class TlaVar extends TlaSimpleExp {

  /*

  VARIABLES A, B

  F == A + B...
  G(X) == X + A...

  Here, X, A and B are TlaVars

  */

  public TlaVar(String name) {
    super(name);
  }

  public TlaPrime PRIME() {
    return new TlaPrime(this);
  }

  @Override
  public <T> T accept(TlaExpVis<T> visitor) {
    return visitor.visit(this);
  }
}
