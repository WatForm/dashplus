package ca.uwaterloo.watform.tlaast.tlaplusnaryops;

import ca.uwaterloo.watform.tlaast.*;
import java.util.List;

public class TlaSeq extends TlaNaryOp {

    public TlaSeq(List<TlaExp> children) {
        super(
                TlaStrings.SEQUENCE_OPEN,
                TlaStrings.SEQUENCE_CLOSE,
                TlaStrings.COMMA,
                children,
                TlaOperator.PrecedenceGroup.SAFE);
    }
}
