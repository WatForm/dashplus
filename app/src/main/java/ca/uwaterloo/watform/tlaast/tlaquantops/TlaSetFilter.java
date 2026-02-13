package ca.uwaterloo.watform.tlaast.tlaquantops;

import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;
import static ca.uwaterloo.watform.utils.GeneralUtil.strCommaList;

import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaOperator;
import ca.uwaterloo.watform.tlaast.TlaStrings;
import java.util.List;

public class TlaSetFilter extends TlaQuantOp {

    /*

    {v \in S : exp}

    variable: v
    set: S  (can be an expression that evaluates to a set)
    expression: exp (boolean expression)

    used to construct a set by applying a filter to another set

    for more complex constructions:

    {<quantophead> : exp}

    where quantophead can be constructed from the QuantOP class

    */

    public TlaSetFilter(List<TlaQuantOpHead> heads, TlaExp expression) {
        super(heads, expression, TlaOperator.PrecedenceGroup.SAFE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TlaStrings.SET_START
                + strCommaList(mapBy(this.heads, h -> h.toTLAPlusSnippetCore(this)))
                + TlaStrings.SPACE
                + TlaStrings.COLON
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(this.expression)
                + TlaStrings.SET_END;
    }
}
