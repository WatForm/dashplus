package ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.List;

public class TlaSet extends TlaNaryOp {

    public TlaSet(List<TlaExp> children) {
        super(
                TlaStrings.SET_START,
                TlaStrings.SET_END,
                TlaStrings.COMMA,
                children,
                TlaOperator.PrecedenceGroup.SAFE);
    }
}
