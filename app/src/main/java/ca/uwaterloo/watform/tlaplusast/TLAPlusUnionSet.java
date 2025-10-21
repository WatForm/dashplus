package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusUnionSet extends TLAPlusBinOperatorInfix {

    public TLAPlusUnionSet(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.SET_UNION, operandOne, operandTwo);
    }
}
