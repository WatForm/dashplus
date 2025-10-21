package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusMultiply extends TLAPlusBinOperatorInfix {

    public TLAPlusMultiply(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.TIMES, operandOne, operandTwo);
    }
}
