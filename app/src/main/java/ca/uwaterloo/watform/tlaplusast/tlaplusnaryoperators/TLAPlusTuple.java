package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;
import java.util.List;

public class TLAPlusTuple extends TLAPlusNaryOperator {

    public TLAPlusTuple(List<ASTNode> children) {
        super(
                TLAPlusStrings.TUPLE_OPEN,
                TLAPlusStrings.TUPLE_CLOSE,
                TLAPlusStrings.COMMA,
                children);
    }
}
