package ca.uwaterloo.watform.tlaast;

import java.util.ArrayList;
import java.util.List;

public class TlaDecl extends TlaAppl {

    public TlaDecl(String name) {
        super(name);
    }

    public TlaDecl(String name, List<TlaVar> parameters) {
        List<TlaExp> params = new ArrayList<>();
        for (TlaVar t : parameters) params.add(t);
        super(name, params);
    }
}
