package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusEquals extends TLAPlusBinOperatorInfix {

    public TLAPlusEquals(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.EQUALS, operandOne, operandTwo);
    }
}
