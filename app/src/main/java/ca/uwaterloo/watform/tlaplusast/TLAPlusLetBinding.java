package ca.uwaterloo.watform.tlaplusast;

import java.util.Arrays;
import java.util.List;

public class TLAPlusLetBinding extends TLAPlusOp {

    private TLAPlusFormulaDefn definition;
    private TLAPlusExp expression;

    public TLAPlusLetBinding(TLAPlusFormulaDefn definition, TLAPlusExp expression) {
        super(TLAPlusOp.Associativity.IRRELEVANT, TLAPlusOp.PrecedenceGroup.SAFE);
        this.definition = definition;
        this.expression = expression;
    }

    @Override
    public List<TLAPlusExp> getChildren() {
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
