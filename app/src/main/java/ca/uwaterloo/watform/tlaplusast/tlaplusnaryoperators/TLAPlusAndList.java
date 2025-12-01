package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;
import java.util.List;

public class TLAPlusAndList extends TLAPlusNaryOp {
    public TLAPlusAndList(List<TLAPlusExp> children) {
        super("", "", TLAPlusStrings.AND, children, TLAPlusOp.PrecedenceGroup.AND_LIST);
    }
}
