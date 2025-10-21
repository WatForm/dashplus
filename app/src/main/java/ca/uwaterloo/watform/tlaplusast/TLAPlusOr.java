package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusOr extends TLAPlusBinOperatorInfix {

    public TLAPlusOr(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.OR, operandOne, operandTwo);
    }
}
