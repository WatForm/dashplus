package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusSubtract extends TLAPlusBinOpMid {

    public TLAPlusSubtract(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.MINUS, operandOne, operandTwo);
    }
}
