package ca.uwaterloo.watform.tlaplusast;

import java.util.Arrays;
import java.util.List;

public class TLAPlusFormulaDefinition extends TLAPlusExpression {
    public final TLAPlusFormulaDeclaration declaration;
    public final TLAPlusExpression body;

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
        return Arrays.asList(this.declaration, this.body);
    }

    @Override
    public String toTLAPlusSnippetCore() {

        // precedence and associativity is never a problem with definitions
        return this.declaration.toTLAPlusSnippet(false)
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.DEFINITION
                + TLAPlusStrings.SPACE
                + this.body.toTLAPlusSnippet(false);
    }
}
