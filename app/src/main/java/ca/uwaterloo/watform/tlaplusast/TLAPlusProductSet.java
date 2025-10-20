package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusProductSet extends TLAPlusBinOpMid {

    public TLAPlusProductSet(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.SET_PRODUCT, operandOne, operandTwo);
    }
}
