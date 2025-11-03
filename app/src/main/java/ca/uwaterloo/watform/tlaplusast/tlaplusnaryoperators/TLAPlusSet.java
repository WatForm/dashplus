package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;
import java.util.List;

public class TLAPlusSet extends TLAPlusNaryOperator {

    public TLAPlusSet(List<ASTNode> children) {
        super(TLAPlusStrings.SET_START, TLAPlusStrings.SET_END, TLAPlusStrings.COMMA, children);
    }
}
