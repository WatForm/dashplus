package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;

public class TLAPlusNot extends TLAPlusUnaryOperator {
    public TLAPlusNot(TLAPlusExpression operand) {
        super(operand);
    }

    /*
    @Override
    public List<String> toStringList() {
        List<String> result = new ArrayList<>();
        result.add(TLAPlusStrings.NOT);
        result.addAll(this.getOperand().toStringList());
        return result;
    }
    */
}
