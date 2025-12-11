package ca.uwaterloo.watform.tlaast;

import ca.uwaterloo.watform.utils.*;
import java.util.List;

public abstract class TlaExp extends ASTNode {
    public abstract List<TlaExp> getChildren();

    public abstract String toTLAPlusSnippetCore();

    public String toTLAPlusSnippet(boolean brackets) {
        if (brackets)
            return TlaStrings.BRACKET_OPEN + toTLAPlusSnippetCore() + TlaStrings.BRACKET_CLOSE;
        return toTLAPlusSnippetCore();
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        sb.append(this.toTLAPlusSnippet(false));
        return;
    }
}
