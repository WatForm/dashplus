package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusEquivalence extends TLAPlusBinOperatorInfix {

    public TLAPlusEquivalence(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.EQUIVALENCE, operandOne, operandTwo);
    }
}
