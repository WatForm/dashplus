package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TLAPlusPrime extends TLAPlusUnaryOperator {
    public TLAPlusPrime(TLAPlusASTNode operand) {
        super(operand);
    }

    @Override
    public List<String> toStringList() {
        List<String> result = new ArrayList<>();
        result.addAll(this.getOperand().toStringList());
        result.add(TLAPlusStrings.PRIME);
        return result;
    }
}
