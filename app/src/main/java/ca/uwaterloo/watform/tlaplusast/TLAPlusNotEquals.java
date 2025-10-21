package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusNotEquals extends TLAPlusBinOperatorInfix {

    public TLAPlusNotEquals(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.NOT_EQUALS, operandOne, operandTwo);
    }
}
