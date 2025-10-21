package ca.uwaterloo.watform.tlaplusast;

import java.util.List;

public class TLAPlusTuple extends TLAPlusNaryOperator {

    public TLAPlusTuple(List<TLAPlusASTNode> children) {
        super(
                TLAPlusStrings.TUPLE_OPEN,
                TLAPlusStrings.TUPLE_CLOSE,
                TLAPlusStrings.COMMA,
                children);
    }
}
