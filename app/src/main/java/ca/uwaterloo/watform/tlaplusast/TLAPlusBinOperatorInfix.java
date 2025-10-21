package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public abstract class TLAPlusBinOperatorInfix extends TLAPlusBinaryOperator {
    private String middle;

    public TLAPlusBinOperatorInfix(
            String middle, TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo) {
        this.middle = middle;
        super(operandOne, operandTwo);
    }

    @Override
    public List<String> toStringList() {
        List<String> result = new ArrayList<>();
        result.addAll(this.getOperandOne().toStringList());
        result.add(this.middle);
        result.addAll(this.getOperandTwo().toStringList());
        return result;
    }
}
