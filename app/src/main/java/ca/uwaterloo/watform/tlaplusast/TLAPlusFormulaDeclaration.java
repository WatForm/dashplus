package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TLAPlusFormulaDeclaration extends TLAPlusFormulaApplication {

    public TLAPlusFormulaDeclaration(String name) {
        super(name);
    }

    public TLAPlusFormulaDeclaration(String name, List<TLAPlusVariable> parameters) {
        List<TLAPlusExpression> params = new ArrayList<>();
        for (TLAPlusVariable t : parameters) params.add(t);
        super(name, params);
    }
}
