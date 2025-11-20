package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;
import java.util.List;

public class TLAPlusAndList extends TLAPlusNaryOperator {
    public TLAPlusAndList(List<TLAPlusExpression> children) {
        super("", "", TLAPlusStrings.AND, children);
    }
}
