package ca.uwaterloo.watform.tlaast.tlanaryops;

import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaOperator;
import ca.uwaterloo.watform.tlaast.TlaStrings;
import java.util.List;

public class TlaOrList extends TlaNaryOp {

    /*
    exp1
    \/ exp2
    \/ exp3
    ...

    where children are exp1, exp2, exp3...
    */

    public TlaOrList(List<? extends TlaExp> children) {
        super(
                "\n",
                "",
                TlaStrings.SPACE + "\n" + TlaStrings.OR + TlaStrings.SPACE,
                children,
                TlaOperator.PrecedenceGroup.AND_OR_LIST);
    }
}
