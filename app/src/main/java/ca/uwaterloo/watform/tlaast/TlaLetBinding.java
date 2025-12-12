package ca.uwaterloo.watform.tlaast;

import java.util.Arrays;
import java.util.List;

public class TlaLetBinding extends TlaOperator {

    public final TlaDefn definition;
    public final TlaExp expression;

    public TlaLetBinding(TlaDefn definition, TlaExp expression) {
        super(TlaOperator.Associativity.IRRELEVANT, TlaOperator.PrecedenceGroup.SAFE);
        this.definition = definition;
        this.expression = expression;
    }

    @Override
    public List<TlaExp> getChildren() {
        return Arrays.asList(this.definition, this.expression);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TlaStrings.LET
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(definition)
                + TlaStrings.SPACE
                + TlaStrings.IN
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(expression);
    }
}
