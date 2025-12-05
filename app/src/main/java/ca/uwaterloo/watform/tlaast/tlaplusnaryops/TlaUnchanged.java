package ca.uwaterloo.watform.tlaast.tlaplusnaryops;

import ca.uwaterloo.watform.tlaast.*;
import java.util.List;

public class TlaUnchanged extends TlaNaryOp {
    public TlaUnchanged(List<? extends TlaVar> children) {
        super(
                TlaStrings.UNCHANGED + TlaStrings.SPACE + TlaStrings.TUPLE_OPEN,
                TlaStrings.TUPLE_CLOSE,
                TlaStrings.COMMA,
                children,
                TlaOperator.PrecedenceGroup.SAFE);
    }
}
