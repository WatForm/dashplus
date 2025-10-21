package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusInSet extends TLAPlusBinOperatorInfix {

    public TLAPlusInSet(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.SET_IN, operandOne, operandTwo);
    }
}
