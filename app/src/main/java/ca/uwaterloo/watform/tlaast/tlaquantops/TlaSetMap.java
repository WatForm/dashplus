package ca.uwaterloo.watform.tlaast.tlaquantops;

import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;
import static ca.uwaterloo.watform.utils.GeneralUtil.strCommaList;

import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaOperator;
import ca.uwaterloo.watform.tlaast.TlaStrings;
import java.util.Arrays;
import java.util.List;

public class TlaSetMap extends TlaQuantOp {

    /*

    {exp : v \in S}

    variable: v
    set: S  (can be an expression that evaluates to a set)
    expression: exp (usually written in terms of v)

    used to construct a set by applying a filter to another set

    */

    public TlaSetMap(List<TlaQuantOpHead> heads, TlaExp expression) {
        super(heads, expression, TlaOperator.PrecedenceGroup.SAFE);
    }

    public TlaSetMap(TlaQuantOpHead head, TlaExp expression) {
        super(Arrays.asList(head), expression, TlaOperator.PrecedenceGroup.SAFE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TlaStrings.SET_START
                + this.getTLASnippetOfChild(this.expression)
                + TlaStrings.SPACE
                + TlaStrings.COLON
                + TlaStrings.SPACE
                + strCommaList(mapBy(this.heads, h -> h.toTLAPlusSnippetCore(this)))
                + TlaStrings.SET_END;
    }
}
