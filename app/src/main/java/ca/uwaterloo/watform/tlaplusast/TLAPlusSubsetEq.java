package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusSubsetEq extends TLAPlusBinOperatorInfix {

    public TLAPlusSubsetEq(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.SET_SUBSET_EQ, operandOne, operandTwo);
    }
}
