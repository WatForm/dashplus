package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusIntersectionSet extends TLAPlusBinOpMid {

    public TLAPlusIntersectionSet(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.SET_INTERSECTION, operandOne, operandTwo);
    }
}
