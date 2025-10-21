package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusNotInSet extends TLAPlusBinOperatorInfix {

    public TLAPlusNotInSet(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.SET_NOT_IN, operandOne, operandTwo);
    }
}
