package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public abstract class TLAPlusSimpleExp extends TLAPlusExp {

    private final String core;

    public TLAPlusSimpleExp(String core) {
        this.core = core;
    }

    @Override
    public List<TLAPlusExp> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return core;
    }
}
