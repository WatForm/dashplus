package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusNot extends TLAPlusUnaryOperator {
    public TLAPlusNot(ASTNode operand) {
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
