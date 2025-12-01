package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.List;

public class TLAPlusTuple extends TLAPlusNaryOp {

    public TLAPlusTuple(List<TLAPlusExp> children) {
        super(
                TLAPlusStrings.TUPLE_OPEN,
                TLAPlusStrings.TUPLE_CLOSE,
                TLAPlusStrings.COMMA,
                children,
                TLAPlusOp.PrecedenceGroup.SAFE);
    }
}
