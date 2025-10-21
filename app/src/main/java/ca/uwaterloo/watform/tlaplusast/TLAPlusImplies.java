package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusImplies extends TLAPlusBinOperatorInfix {

    public TLAPlusImplies(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.IMPLICATION, operandOne, operandTwo);
    }
}
