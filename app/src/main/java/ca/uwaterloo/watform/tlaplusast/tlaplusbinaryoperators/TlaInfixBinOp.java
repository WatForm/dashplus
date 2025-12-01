package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaOperator;
import ca.uwaterloo.watform.tlaplusast.TlaStrings;

public abstract class TlaInfixBinOp extends TlaBinOp {

    private final String infixOperator;

    public TlaInfixBinOp(
            String infixOperator,
            TlaExp operandOne,
            TlaExp operandTwo,
            TlaOperator.Associativity associativity,
            TlaOperator.PrecedenceGroup precedenceGroup) {

        super(operandOne, operandTwo, associativity, precedenceGroup);
        this.infixOperator = infixOperator;
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return this.getTLASnippetOfChild(this.operandOne)
                + TlaStrings.SPACE
                + this.infixOperator
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(this.operandTwo);
    }
}
