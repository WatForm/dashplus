package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusConcatenateSequence extends TLAPlusBinOperatorInfix {

    public TLAPlusConcatenateSequence(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.CONCATENATE, operandOne, operandTwo);
    }
}
