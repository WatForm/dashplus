package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.List;

public class TLAPlusSequence extends TLAPlusNaryOp {

    public TLAPlusSequence(List<TlaExp> children) {
        super(
                TlaStrings.SEQUENCE_OPEN,
                TlaStrings.SEQUENCE_CLOSE,
                TlaStrings.COMMA,
                children,
                TlaOperator.PrecedenceGroup.SAFE);
    }
}
