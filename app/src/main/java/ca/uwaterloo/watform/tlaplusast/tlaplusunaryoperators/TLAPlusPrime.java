package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusVariable;

public class TLAPlusPrime extends TLAPlusUnaryOperator {
    public TLAPlusPrime(TLAPlusVariable operand) {
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
