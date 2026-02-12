package ca.uwaterloo.watform.tlaast.tlanaryops;

import ca.uwaterloo.watform.tlaast.*;
import java.util.List;

public class TlaTuple extends TlaNaryOp {

    /*
    <<exp1, exp2...>>

    where children are exp1, exp2...
    */

    public TlaTuple(List<? extends TlaExp> children) {
        super(
                TlaStrings.TUPLE_OPEN,
                TlaStrings.TUPLE_CLOSE,
                TlaStrings.COMMA,
                children,
                TlaOperator.PrecedenceGroup.SAFE);
    }
}
