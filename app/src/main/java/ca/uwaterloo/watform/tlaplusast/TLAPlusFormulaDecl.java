package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TLAPlusFormulaDecl extends TLAPlusFormulaAppl {

    public TLAPlusFormulaDecl(String name) {
        super(name);
    }

    public TLAPlusFormulaDecl(String name, List<TLAPlusVar> parameters) {
        List<TLAPlusExp> params = new ArrayList<>();
        for (TLAPlusVar t : parameters) params.add(t);
        super(name, params);
    }
}
