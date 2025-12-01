package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusOp;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;

public abstract class TLAPlusInfixBinOp extends TLAPlusBinOp {

    private final String infixOperator;

    public TLAPlusInfixBinOp(
            String infixOperator,
            TLAPlusExp operandOne,
            TLAPlusExp operandTwo,
            TLAPlusOp.Associativity associativity,
            TLAPlusOp.PrecedenceGroup precedenceGroup) {

        super(operandOne, operandTwo, associativity, precedenceGroup);
        this.infixOperator = infixOperator;
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return this.getTLASnippetOfChild(this.operandOne)
                + TLAPlusStrings.SPACE
                + this.infixOperator
                + TLAPlusStrings.SPACE
                + this.getTLASnippetOfChild(this.operandTwo);
    }
}
