package ca.uwaterloo.watform.tlaast.tlaquantops;

import ca.uwaterloo.watform.tlaast.*;
import java.util.Arrays;
import java.util.List;

public abstract class TlaQuantOp extends TlaOperator {

    public final TlaVar variable; // bound variable
    public final TlaExp set; // set that the iteration takes place over
    public final TlaExp expression; // expression used

    public TlaQuantOp(
            TlaVar variable,
            TlaExp set,
            TlaExp expression,
            TlaOperator.PrecedenceGroup precedenceGroup) {
        super(TlaOperator.Associativity.IRRELEVANT, precedenceGroup);
        this.variable = variable;
        this.expression = expression;
        this.set = set;
    }

    public List<TlaExp> getChildren() {
        return Arrays.asList(this.variable, this.set, this.expression);
    }

    // this is a common style for exists and for-all
    static String predicateSnippetCore(TlaQuantOp o, String symbol) {
        return symbol
                + TlaStrings.SPACE
                + o.getTLASnippetOfChild(o.variable)
                + TlaStrings.SPACE
                + TlaStrings.SET_IN
                + TlaStrings.SPACE
                + o.getTLASnippetOfChild(o.set)
                + TlaStrings.SPACE
                + TlaStrings.COLON
                + TlaStrings.SPACE
                + o.getTLASnippetOfChild(o.expression);
    }
}
