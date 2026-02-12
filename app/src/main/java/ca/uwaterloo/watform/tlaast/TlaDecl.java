package ca.uwaterloo.watform.tlaast;

import java.util.ArrayList;
import java.util.List;

public class TlaDecl extends TlaAppl {

    /*
    G(arg1,arg2...) == exp
    F == G(exp1,exp2...)

    Here, G(arg1,arg2...) is represented by this node
    G is the name
    (arg1,arg2...) is params
    */

    public TlaDecl(String name) {
        super(name);
    }

    public TlaDecl(String name, List<TlaVar> parameters) {
        List<TlaExp> params = new ArrayList<>();
        for (TlaVar t : parameters) params.add(t);
        super(name, params);
    }
}
