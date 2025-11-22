package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.TLAPlusOperator;
import ca.uwaterloo.watform.tlaplusast.TLAPlusStrings;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVariable;

public class TLAPlusPrime extends TLAPlusUnaryOperator {
    public TLAPlusPrime(TLAPlusVariable operand) {
        super(operand, TLAPlusOperator.PrecedenceGroup.SAFE);
    }

    @Override
    public void toString(StringBuilder sb, int ident) {

        this.getOperand().toString(sb, ident);
        sb.append(TLAPlusStrings.PRIME);
        return;
    }
}
