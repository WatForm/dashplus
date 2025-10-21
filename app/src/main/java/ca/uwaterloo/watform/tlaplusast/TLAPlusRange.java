package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusRange extends TLAPlusBinOperatorInfix {

    public TLAPlusRange(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.RANGE, operandOne, operandTwo);
    }
}
