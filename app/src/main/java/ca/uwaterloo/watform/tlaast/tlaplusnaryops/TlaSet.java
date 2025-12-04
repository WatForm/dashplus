package ca.uwaterloo.watform.tlaast.tlaplusnaryops;

import ca.uwaterloo.watform.tlaast.*;
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
