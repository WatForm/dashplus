package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusGreater extends TLAPlusBinOperatorInfix {

    public TLAPlusGreater(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.GREATER_THAN, operandOne, operandTwo);
    }
}
