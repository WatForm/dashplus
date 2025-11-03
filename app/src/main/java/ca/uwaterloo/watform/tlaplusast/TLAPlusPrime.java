package ca.uwaterloo.watform.tlaplusast;

import ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators.TLAPlusUnaryOperator;

public class TLAPlusPrime extends TLAPlusUnaryOperator {
    public TLAPlusPrime(TLAPlusExpression operand) {
        super(operand);
    }

    /*
    @Override
    public List<String> toStringList() {
        List<String> result = new ArrayList<>();
        result.addAll(this.getOperand().toStringList());
        result.add(TLAPlusStrings.PRIME);
        return result;
    }*/

    @Override
    public void toString(StringBuilder sb, int ident) {
        return;
        // TODO fix this
    }
}
