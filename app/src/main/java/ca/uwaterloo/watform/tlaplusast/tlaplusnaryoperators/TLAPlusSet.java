package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.List;

public class TLAPlusSet extends TLAPlusNaryOp {

    public TLAPlusSet(List<TLAPlusExp> children) {
        super(
                TLAPlusStrings.SET_START,
                TLAPlusStrings.SET_END,
                TLAPlusStrings.COMMA,
                children,
                TLAPlusOp.PrecedenceGroup.SAFE);
    }
}
