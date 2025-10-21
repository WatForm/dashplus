package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusAdd extends TLAPlusBinOperatorInfix {

    public TLAPlusAdd(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.PLUS, operandOne, operandTwo);
    }
}
