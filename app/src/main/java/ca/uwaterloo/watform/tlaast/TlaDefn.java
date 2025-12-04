package ca.uwaterloo.watform.tlaast;

import java.util.Arrays;
import java.util.List;

public class TlaDefn extends TlaExp {
    public final TlaDecl declaration;
    public final TlaExp body;

    public TlaDefn(TlaDecl declaration, TlaExp body) {
        this.declaration = declaration;
        this.body = body;
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        this.declaration.toString(sb, ident);
        sb.append(TlaStrings.SPACE + TlaStrings.DEFINITION + TlaStrings.SPACE);
        this.body.toString(sb, ident);
        return;
    }

    public List<TlaExp> getChildren() {
        return Arrays.asList(this.declaration, this.body);
    }

    @Override
    public String toTLAPlusSnippetCore() {

        // precedence and associativity is never a problem with definitions
        return this.declaration.toTLAPlusSnippet(false)
                + TlaStrings.SPACE
                + TlaStrings.DEFINITION
                + TlaStrings.SPACE
                + this.body.toTLAPlusSnippet(false);
    }
}
