package ca.uwaterloo.watform.tlaast;

import java.util.Arrays;
import java.util.List;

public class TlaDefn extends TlaExp {
    public final TlaDecl decl;
    public final TlaExp body;

    public TlaDefn(TlaDecl decl, TlaExp body) {
        this.decl = decl;
        this.body = body;
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        this.decl.toString(sb, ident);
        sb.append(TlaStrings.SPACE + TlaStrings.DEFINITION + TlaStrings.SPACE);
        this.body.toString(sb, ident);
        return;
    }

    public List<TlaExp> getChildren() {
        return Arrays.asList(this.decl, this.body);
    }

    @Override
    public String toTLAPlusSnippetCore() {

        // precedence and associativity is never a problem with definitions
        return this.decl.toTLAPlusSnippet(false)
                + TlaStrings.SPACE
                + TlaStrings.DEFINITION
                + TlaStrings.SPACE
                + this.body.toTLAPlusSnippet(false);
    }
}
