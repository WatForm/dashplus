package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusNotEquals extends TLAPlusBinOpMid {

    public TLAPlusNotEquals(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.NOT_EQUALS, operandOne, operandTwo);
    }
}
