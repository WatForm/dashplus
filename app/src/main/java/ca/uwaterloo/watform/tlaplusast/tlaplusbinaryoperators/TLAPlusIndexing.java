package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusIndexing extends TLAPlusBinOp {

    public TLAPlusIndexing(TlaExp operandOne, TlaExp operandTwo) {
        super(
                operandOne,
                operandTwo,
                TlaOperator.Associativity.IRRELEVANT,
                TlaOperator.PrecedenceGroup.SAFE);
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return this.getTLASnippetOfChild(this.operandOne)
                + TlaStrings.SQUARE_BRACKET_OPEN
                + this.getTLASnippetOfChild(this.operandTwo)
                + TlaStrings.SQUARE_BRACKET_CLOSE;
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
