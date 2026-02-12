package ca.uwaterloo.watform.tlaast.tlanaryops;

import ca.uwaterloo.watform.tlaast.*;
import java.util.List;

public class TlaUnchanged extends TlaNaryOp {

    /* 
    UNCHANGED <<V1,V2...>>

    where V1, V2.. are the children
    this is equivalent to V1' = V1 /\ V2' = V2 /\ ...
    (syntactic sugar)
    
    */

    public TlaUnchanged(List<? extends TlaVar> children) {
        super(
                TlaStrings.UNCHANGED + TlaStrings.SPACE + TlaStrings.TUPLE_OPEN,
                TlaStrings.TUPLE_CLOSE,
                TlaStrings.COMMA,
                children,
                TlaOperator.PrecedenceGroup.SAFE);
    }
}
