package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public abstract class TlaSimpleExp extends TlaExp {

    private final String core;

    public TlaSimpleExp(String core) {
        this.core = core;
    }

    @Override
    public List<TlaExp> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return core;
    }
}
