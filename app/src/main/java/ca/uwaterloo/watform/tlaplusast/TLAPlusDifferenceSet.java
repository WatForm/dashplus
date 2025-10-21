package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusDifferenceSet extends TLAPlusBinOperatorInfix {

    public TLAPlusDifferenceSet(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.SET_DIFFERENCE, operandOne, operandTwo);
    }
}
