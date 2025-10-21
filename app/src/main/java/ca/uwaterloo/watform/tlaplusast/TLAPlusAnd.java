package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusAnd extends TLAPlusBinOperatorInfix {

    public TLAPlusAnd(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.AND, operandOne, operandTwo);
    }
}
