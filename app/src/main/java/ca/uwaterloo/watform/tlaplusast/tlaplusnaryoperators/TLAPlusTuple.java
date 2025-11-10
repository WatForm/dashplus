package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.List;

public class TLAPlusTuple extends TLAPlusNaryOperator {

    public TLAPlusTuple(List<TLAPlusExpression> children) {
        super(
                TLAPlusStrings.TUPLE_OPEN,
                TLAPlusStrings.TUPLE_CLOSE,
                TLAPlusStrings.COMMA,
                children);
    }
}
