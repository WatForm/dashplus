package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaOperator;
import ca.uwaterloo.watform.tlaplusast.TlaStrings;
import java.util.List;

public class TLAPlusOrList extends TLAPlusNaryOp {
    public TLAPlusOrList(List<TlaExp> children) {
        super("", "", TlaStrings.OR, children, TlaOperator.PrecedenceGroup.OR_LIST);
    }
}
