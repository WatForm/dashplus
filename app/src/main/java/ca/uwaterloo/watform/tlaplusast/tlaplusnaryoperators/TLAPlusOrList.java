package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOperator;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;
import java.util.List;

public class TLAPlusOrList extends TLAPlusNaryOperator {
    public TLAPlusOrList(List<TLAPlusExpression> children) {
        super("", "", TLAPlusStrings.OR, children, TLAPlusOperator.PrecedenceGroup.OR_LIST);
    }
}
