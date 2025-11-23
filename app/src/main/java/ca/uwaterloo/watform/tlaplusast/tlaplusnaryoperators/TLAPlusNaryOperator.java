package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.List;

public abstract class TLAPlusNaryOperator extends TLAPlusOperator {
    private final String start;
    private final String end;
    private final String separator;
    private final List<? extends TLAPlusExpression> children;

    public TLAPlusNaryOperator(
            String start,
            String end,
            String separator,
            List<? extends TLAPlusExpression> children,
            TLAPlusOperator.PrecedenceGroup precedenceGroup) {
        super(TLAPlusOperator.Associativity.IRRELEVANT, precedenceGroup);
        this.start = start;
        this.end = end;
        this.separator = separator;
        this.children = children;
    }

    @Override
    public String toTLAPlusSnippetCore() {
        int n = this.children.size();
        StringBuilder sb = new StringBuilder();
        sb.append(this.start);
        if (n != 0) {
            for (int i = 0; i < n; i++) {
                sb.append(this.getTLASnippetOfChild(this.children.get(i)));
                if (i != n - 1) sb.append(this.separator);
            }
        }
        sb.append(this.end);
        return sb.toString();
    }

    @Override
    public void toString(StringBuilder sb, int ident) {

        int n = this.children.size();
        sb.append(this.start);
        if (n != 0) {
            for (int i = 0; i < n; i++) {
                this.children.get(i).toString(sb, ident);
                if (i != n - 1) sb.append(this.separator);
            }
        }
        sb.append(this.end);

        return;
    }

    public List<TLAPlusExpression> getChildren() {
        return GeneralUtil.mapBy(this.children, c -> (TLAPlusExpression) c);
    }
}
