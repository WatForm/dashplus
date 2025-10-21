package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusLesser extends TLAPlusBinOperatorInfix {

    public TLAPlusLesser(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        super(TLAPlusStrings.LESSER_THAN, operandOne, operandTwo);
    }
}
