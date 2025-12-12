package ca.uwaterloo.watform.tlaast;

import ca.uwaterloo.watform.tlaast.tlaunops.TlaPrime;

public class TlaVar extends TlaSimpleExp {
    public TlaVar(String name) {
        super(name);
    }

    public TlaPrime PRIME() {
        return new TlaPrime(this);
    }
}
