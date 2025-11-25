package ca.uwaterloo.watform.tlaplusast;

import java.util.Arrays;
import java.util.List;

public class TLAPlusLetBinding extends TLAPlusOperator {

    private TLAPlusFormulaDefinition definition;
    private TLAPlusExpression expression;

    public TLAPlusLetBinding(TLAPlusFormulaDefinition definition, TLAPlusExpression expression) {
        super(TLAPlusOperator.Associativity.IRRELEVANT, TLAPlusOperator.PrecedenceGroup.SAFE);
        this.definition = definition;
        this.expression = expression;
    }

    @Override
    public List<TLAPlusExpression> getChildren() {
        return Arrays.asList(this.definition, this.expression);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TLAPlusStrings.LET
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(definition)
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.IN
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(expression);
    }
}
