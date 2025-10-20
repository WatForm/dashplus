package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusDifferenceSet extends TLAPlusBinOpMid {

    public TLAPlusDifferenceSet(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.SET_DIFFERENCE, operandOne, operandTwo);
    }
}
