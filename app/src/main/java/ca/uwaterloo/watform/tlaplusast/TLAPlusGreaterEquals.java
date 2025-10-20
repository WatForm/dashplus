package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusGreaterEquals extends TLAPlusBinOpMid {

    public TLAPlusGreaterEquals(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.GREATER_THAN_EQUALS, operandOne, operandTwo);
    }
}
