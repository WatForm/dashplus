package ca.uwaterloo.watform.tlaast.tlanaryops;

import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.Collections;
import java.util.List;

public abstract class TlaNaryOp extends TlaOperator {
    private final String start;
    private final String end;
    private final String separator;

    public final List<? extends TlaExp> children;

    public TlaNaryOp(
            String start,
            String end,
            String separator,
            List<? extends TlaExp> children,
            TlaOperator.PrecedenceGroup precedenceGroup) {
        super(TlaOperator.Associativity.IRRELEVANT, precedenceGroup);
        this.start = start;
        this.end = end;
        this.separator = separator;
        this.children = Collections.unmodifiableList(children);
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

    public List<TlaExp> getChildren() {
        return GeneralUtil.mapBy(this.children, c -> c);
    }
}
