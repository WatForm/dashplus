package ca.uwaterloo.watform.tlaplusast;

import ca.uwaterloo.watform.utils.*;

public class TLAPlusFormulaDefinition extends ASTNode {
    private TLAPlusFormulaDeclaration definition;
    private TLAPlusExpression body;

    public TLAPlusFormulaDefinition(TLAPlusFormulaDeclaration definition, TLAPlusExpression body) {
        this.definition = definition;
        this.body = body;
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        return;
        // TODO fix this
    }
}
