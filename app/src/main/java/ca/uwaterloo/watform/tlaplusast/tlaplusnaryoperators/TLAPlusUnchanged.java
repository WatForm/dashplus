package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.List;

public class TLAPlusUnchanged extends TLAPlusNaryOp {
    public TLAPlusUnchanged(List<TLAPlusVar> children) {
        super(
                TLAPlusStrings.UNCHANGED + TLAPlusStrings.SPACE + TLAPlusStrings.TUPLE_OPEN,
                TLAPlusStrings.TUPLE_CLOSE,
                TLAPlusStrings.COMMA,
                children,
                TLAPlusOp.PrecedenceGroup.SAFE);
    }
}
