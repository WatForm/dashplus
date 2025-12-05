package ca.uwaterloo.watform.tlaast.tlaplusnaryops;

import ca.uwaterloo.watform.tlaast.*;
import java.util.List;

public class TlaTuple extends TlaNaryOp {

    public TlaTuple(List<? extends TlaExp> children) {
        super(
                TlaStrings.TUPLE_OPEN,
                TlaStrings.TUPLE_CLOSE,
                TlaStrings.COMMA,
                children,
                TlaOperator.PrecedenceGroup.SAFE);
    }
}
