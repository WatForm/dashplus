package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TlaFormulaDecl extends TlaFormulaAppl {

    public TlaFormulaDecl(String name) {
        super(name);
    }

    public TlaFormulaDecl(String name, List<TlaVar> parameters) {
        List<TlaExp> params = new ArrayList<>();
        for (TlaVar t : parameters) params.add(t);
        super(name, params);
    }
}
