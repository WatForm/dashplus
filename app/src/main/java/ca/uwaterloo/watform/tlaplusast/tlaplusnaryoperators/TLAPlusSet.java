package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.List;

public class TLAPlusSet extends TLAPlusNaryOp {

    public TLAPlusSet(List<TlaExp> children) {
        super(
                TlaStrings.SET_START,
                TlaStrings.SET_END,
                TlaStrings.COMMA,
                children,
                TlaOperator.PrecedenceGroup.SAFE);
    }
}
