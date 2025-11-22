package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusIndexing extends TLAPlusBinaryOperator {

    public TLAPlusIndexing(TLAPlusExpression operandOne, TLAPlusExpression operandTwo) {
        super(
                operandOne,
                operandTwo,
                TLAPlusOperator.Associativity.IRRELEVANT,
                TLAPlusOperator.PrecedenceGroup.SAFE);
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        return;
        // TODO fix this
    }

    /*
    @Override
    public List<String> toStringList() {
        List<String> result = new ArrayList<>();
        result.addAll(this.getOperandOne().toStringList());
        result.add(TLAPlusStrings.SQUARE_BRACKET_OPEN);
        result.addAll(this.getOperandTwo().toStringList());
        result.add(TLAPlusStrings.SQUARE_BRACKET_CLOSE);
        return result;
    }
    */
}
