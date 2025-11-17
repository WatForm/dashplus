package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TLAPlusFormulaDefinition extends TLAPlusExpression {
    private TLAPlusFormulaDeclaration declaration;
    private TLAPlusExpression body;

    public TLAPlusFormulaDefinition(TLAPlusFormulaDeclaration declaration, TLAPlusExpression body) {
        this.declaration = declaration;
        this.body = body;
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        this.declaration.toString(sb, ident);
        sb.append(TLAPlusStrings.SPACE + TLAPlusStrings.DEFINITION + TLAPlusStrings.SPACE);
        this.body.toString(sb, ident);
        return;
    }

    public List<TLAPlusExpression> getChildren() {
        List<TLAPlusExpression> children = new ArrayList<>();
        children.add(this.declaration);
        children.add(this.body);
        return children;
    }
}
