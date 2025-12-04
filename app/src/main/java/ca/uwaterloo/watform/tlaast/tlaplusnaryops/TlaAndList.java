package ca.uwaterloo.watform.tlaast.tlaplusnaryops;

import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaOperator;
import ca.uwaterloo.watform.tlaast.TlaStrings;
import java.util.List;

public class TlaAndList extends TlaNaryOp {
    public TlaAndList(List<TlaExp> children) {
        super("", "", TlaStrings.AND, children, TlaOperator.PrecedenceGroup.AND_LIST);
    }
}
