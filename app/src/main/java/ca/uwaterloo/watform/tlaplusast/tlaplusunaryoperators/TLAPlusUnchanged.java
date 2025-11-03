package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;

public class TLAPlusUnchanged extends TLAPlusUnaryOperator {
    public TLAPlusUnchanged(ASTNode operand) {
        super(operand);
    }

    /*
    @Override
    public List<String> toStringList() {
        List<String> result = new ArrayList<>();
        result.add(TLAPlusStrings.UNCHANGED);
        result.addAll(this.getOperand().toStringList());
        return result;
    }
    */
}
