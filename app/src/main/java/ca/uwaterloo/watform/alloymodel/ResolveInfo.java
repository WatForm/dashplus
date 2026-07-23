package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import java.util.*;
import java.util.stream.Collectors;

class ResolveInfo {
    final Optional<Integer> arity;
    final AlloyExpr exp;
    // these are the arities of expected arguments to a pred/fun
    final List<Optional<Integer>> argArities;

    // KENG TODO: probably need returnType info here as well

    ResolveInfo(Optional<Integer> arity, AlloyExpr exp) {
        this.arity = arity;
        this.exp = exp;
        this.argArities = emptyList();
    }

    ResolveInfo(AlloyExpr exp) {
        this.arity = UNKNOWN_ARITY;
        this.exp = exp;
        this.argArities = emptyList();
    }

    ResolveInfo(List<Optional<Integer>> argArities, Optional<Integer> arity, AlloyExpr exp) {
        this.argArities = argArities;
        this.arity = arity;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return String.format(
                "(arity=%s, exp=%s, argArities=%s)",
                arity.map(a -> Integer.toString(a)).orElse("?"),
                exp,
                argArities.stream()
                        .map(a -> a.map(i -> Integer.toString(i)).orElse("?"))
                        .collect(Collectors.joining(", ", "[", "]")));
    }

    protected static final Optional<Integer> UNKNOWN_ARITY = Optional.empty();
    // private static final Optional<Integer> BOOLEAN_ARITY = Optional.of(1);
    protected static final Optional<Integer> ONE_ARITY = Optional.of(1);
    protected static final Optional<Integer> TWO_ARITY = Optional.of(2);
}
