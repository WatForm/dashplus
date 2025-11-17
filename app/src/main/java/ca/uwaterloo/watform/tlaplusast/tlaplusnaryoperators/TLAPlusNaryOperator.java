package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.List;

public abstract class TLAPlusNaryOperator extends TLAPlusExpression {
    private String start;
    private String end;
    private String separator;
    private List<TLAPlusExpression> children;

    public TLAPlusNaryOperator(
            String start, String end, String separator, List<TLAPlusExpression> children) {
        this.start = start;
        this.end = end;
        this.separator = separator;
        this.children = children;
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
        return this.children;
    }
}
