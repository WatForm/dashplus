package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.List;

public class TLAPlusSet extends TLAPlusNaryOperator {

    public TLAPlusSet(List<TLAPlusExpression> children) {
        super(TLAPlusStrings.SET_START, TLAPlusStrings.SET_END, TLAPlusStrings.COMMA, children);
    }
}
