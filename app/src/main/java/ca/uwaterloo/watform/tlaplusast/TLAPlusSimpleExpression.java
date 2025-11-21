package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public abstract class TLAPlusSimpleExpression extends TLAPlusExpression {

    private final String core;

    public TLAPlusSimpleExpression(String core) {
        this.core = core;
    }

    @Override
    public List<TLAPlusExpression> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        sb.append(core);
        return;
    }
}
