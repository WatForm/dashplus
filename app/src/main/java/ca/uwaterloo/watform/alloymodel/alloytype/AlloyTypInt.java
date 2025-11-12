package ca.uwaterloo.watform.alloymodel.alloytype;

import java.util.List;
import java.util.Set;

public final class AlloyTypInt extends AlloyTypRel {
    public final AlloyTypInt INSTANCE = new AlloyTypInt();

    private AlloyTypInt() {
        super(Set.of(List.of("AlloyBuiltinInteger")));
    }
}
