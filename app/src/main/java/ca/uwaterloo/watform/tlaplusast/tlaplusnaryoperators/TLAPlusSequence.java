package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.List;

public class TLAPlusSequence extends TLAPlusNaryOp {

    public TLAPlusSequence(List<TLAPlusExp> children) {
        super(
                TLAPlusStrings.SEQUENCE_OPEN,
                TLAPlusStrings.SEQUENCE_CLOSE,
                TLAPlusStrings.COMMA,
                children,
                TLAPlusOp.PrecedenceGroup.SAFE);
    }
}
