package ca.uwaterloo.watform.tlaast;

import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TlaAppl extends TlaOperator {

    public final String name;
    public final List<? extends TlaExp> params;

    public TlaAppl(String name, List<? extends TlaExp> params) {
        super(TlaOperator.Associativity.IRRELEVANT, TlaOperator.PrecedenceGroup.SAFE);
        this.name = name;
        this.params = Collections.unmodifiableList(params);
    }

    public TlaAppl(String name) {
        this(name, new ArrayList<>());
    }

    public List<TlaExp> getChildren() {
        return GeneralUtil.mapBy(this.params, x -> x);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        int n = this.params.size();
        if (n == 0) return this.name;

        StringBuilder sb = new StringBuilder();
        sb.append(this.name + TlaStrings.BRACKET_OPEN);
        for (int i = 0; i < n; i++) {
            sb.append(this.getTLASnippetOfChild(this.params.get(i)));
            if (i != n - 1) sb.append(TlaStrings.COMMA);
        }
        sb.append(TlaStrings.BRACKET_CLOSE);
        return sb.toString();
    }
}
