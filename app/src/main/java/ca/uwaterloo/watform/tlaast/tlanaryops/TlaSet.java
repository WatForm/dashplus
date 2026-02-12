package ca.uwaterloo.watform.tlaast.tlanaryops;

import ca.uwaterloo.watform.tlaast.*;
import java.util.List;

public class TlaSet extends TlaNaryOp {

    /*
    {exp1, exp2...}

    where children are exp1, exp2...
    */

    public TlaSet(List<? extends TlaExp> children) {
        super(
                TlaStrings.SET_START,
                TlaStrings.SET_END,
                TlaStrings.COMMA,
                children,
                TlaOperator.PrecedenceGroup.SAFE);
    }
}
