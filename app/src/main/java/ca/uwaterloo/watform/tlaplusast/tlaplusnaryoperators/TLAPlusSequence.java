package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.List;

public class TLAPlusSequence extends TLAPlusNaryOperator {

    public TLAPlusSequence(List<TLAPlusExpression> children) {
        super(
                TLAPlusStrings.SEQUENCE_OPEN,
                TLAPlusStrings.SEQUENCE_CLOSE,
                TLAPlusStrings.COMMA,
                children);
    }
}
