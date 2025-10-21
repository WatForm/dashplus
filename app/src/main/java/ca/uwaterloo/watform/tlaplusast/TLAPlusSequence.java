package ca.uwaterloo.watform.tlaplusast;

import java.util.List;

public class TLAPlusSequence extends TLAPlusNaryOperator {

    public TLAPlusSequence(List<TLAPlusASTNode> children) {
        super(
                TLAPlusStrings.SEQUENCE_OPEN,
                TLAPlusStrings.SEQUENCE_CLOSE,
                TLAPlusStrings.COMMA,
                children);
    }
}
