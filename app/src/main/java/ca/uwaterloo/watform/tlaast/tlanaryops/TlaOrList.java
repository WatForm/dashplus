package ca.uwaterloo.watform.tlaast.tlanaryops;

import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaOperator;
import ca.uwaterloo.watform.tlaast.TlaStrings;
import java.util.List;

public class TlaOrList extends TlaNaryOp {
    public TlaOrList(List<? extends TlaExp> children) {
        super(
                "",
                "",
                TlaStrings.SPACE + TlaStrings.OR + TlaStrings.SPACE,
                children,
                TlaOperator.PrecedenceGroup.UNSAFE);
    }
}
