package ca.uwaterloo.watform.tlaplusast;

import ca.uwaterloo.watform.utils.*;
import java.util.List;

public abstract class TLAPlusExpression extends ASTNode {
    public abstract List<TLAPlusExpression> getChildren();

    public abstract String toTLAPlusSnippetCore();

    public String toTLAPlusSnippet(boolean brackets) {
        if (brackets)
            return TLAPlusStrings.BRACKET_OPEN
                    + toTLAPlusSnippetCore()
                    + TLAPlusStrings.BRACKET_CLOSE;
        return toTLAPlusSnippetCore();
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        sb.append(this.toTLAPlusSnippet(false));
        return;
    }
}
