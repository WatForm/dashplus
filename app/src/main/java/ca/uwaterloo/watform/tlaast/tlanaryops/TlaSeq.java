package ca.uwaterloo.watform.tlaast.tlanaryops;

import ca.uwaterloo.watform.tlaast.*;
import java.util.List;

public class TlaSeq extends TlaNaryOp {

    /*
    <<exp1, exp2...>>

    where children are exp1, exp2...
    */

    public TlaSeq(List<? extends TlaExp> children) {
        super(
                TlaStrings.SEQUENCE_OPEN,
                TlaStrings.SEQUENCE_CLOSE,
                TlaStrings.COMMA,
                children,
                TlaOperator.PrecedenceGroup.SAFE);
    }
}
