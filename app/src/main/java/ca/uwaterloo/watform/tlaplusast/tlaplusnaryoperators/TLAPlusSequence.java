package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;
import java.util.List;

public class TLAPlusSequence extends TLAPlusNaryOperator {

    public TLAPlusSequence(List<ASTNode> children) {
        super(
                TLAPlusStrings.SEQUENCE_OPEN,
                TLAPlusStrings.SEQUENCE_CLOSE,
                TLAPlusStrings.COMMA,
                children);
    }
}
