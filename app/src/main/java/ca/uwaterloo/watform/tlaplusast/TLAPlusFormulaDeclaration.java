package ca.uwaterloo.watform.tlaplusast;

import java.util.List;

public class TLAPlusFormulaDeclaration extends TLAPlusFormulaApplication {

    public TLAPlusFormulaDeclaration(String name) {
        super(name);
    }

    public TLAPlusFormulaDeclaration(String name, List<TLAPlusVariable> parameters) {
        super(name, parameters);
    }
}
