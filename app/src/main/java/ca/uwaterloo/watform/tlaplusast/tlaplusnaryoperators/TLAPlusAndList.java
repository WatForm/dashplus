package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaOperator;
import ca.uwaterloo.watform.tlaplusast.TlaStrings;
import java.util.List;

public class TLAPlusAndList extends TLAPlusNaryOp {
    public TLAPlusAndList(List<TlaExp> children) {
        super("", "", TlaStrings.AND, children, TlaOperator.PrecedenceGroup.AND_LIST);
    }
}
