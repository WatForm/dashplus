package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;
import java.util.List;

public class TLAPlusOrList extends TLAPlusNaryOp {
    public TLAPlusOrList(List<TLAPlusExp> children) {
        super("", "", TLAPlusStrings.OR, children, TLAPlusOp.PrecedenceGroup.OR_LIST);
    }
}
