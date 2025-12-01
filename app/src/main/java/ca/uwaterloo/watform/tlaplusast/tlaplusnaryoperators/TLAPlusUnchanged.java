package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.List;

public class TLAPlusUnchanged extends TLAPlusNaryOp {
    public TLAPlusUnchanged(List<TlaVar> children) {
        super(
                TlaStrings.UNCHANGED + TlaStrings.SPACE + TlaStrings.TUPLE_OPEN,
                TlaStrings.TUPLE_CLOSE,
                TlaStrings.COMMA,
                children,
                TlaOperator.PrecedenceGroup.SAFE);
    }
}
