package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.List;

public class TLAPlusTuple extends TLAPlusNaryOp {

    public TLAPlusTuple(List<TlaExp> children) {
        super(
                TlaStrings.TUPLE_OPEN,
                TlaStrings.TUPLE_CLOSE,
                TlaStrings.COMMA,
                children,
                TlaOperator.PrecedenceGroup.SAFE);
    }
}
