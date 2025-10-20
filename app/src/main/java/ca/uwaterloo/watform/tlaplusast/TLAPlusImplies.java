package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusImplies extends TLAPlusBinOpMid {

    public TLAPlusImplies(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.IMPLICATION, operandOne, operandTwo);
    }
}
