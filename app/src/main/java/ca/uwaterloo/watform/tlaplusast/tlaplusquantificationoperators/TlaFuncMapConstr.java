package ca.uwaterloo.watform.tlaplusast.tlaplusquantificationoperators;

import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaOperator;
import ca.uwaterloo.watform.tlaplusast.TlaStrings;
import ca.uwaterloo.watform.tlaplusast.TlaVar;

public class TlaFuncMapConstr extends TlaQuantOp {
    public TlaFuncMapConstr(
            TlaVar variable, TlaExp set, TlaExp expression) {
        super(variable, set, expression, TlaOperator.PrecedenceGroup.SAFE);
    }

    // [x \in S |-> e]
    @Override
    public String toTLAPlusSnippetCore() {
        return TlaStrings.SQUARE_BRACKET_OPEN
                + this.getTLASnippetOfChild(this.variable)
                + TlaStrings.IN
                + TlaStrings.SPACE
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(this.set)
                + TlaStrings.SPACE
                + TlaStrings.MAP
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(this.expression)
                + TlaStrings.SPACE;
    }
}
