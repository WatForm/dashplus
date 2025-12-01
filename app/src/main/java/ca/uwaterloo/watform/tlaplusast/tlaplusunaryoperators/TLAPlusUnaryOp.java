package ca.uwaterloo.watform.tlaplusast.tlaplusunaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.Arrays;
import java.util.List;

public abstract class TLAPlusUnaryOp extends TLAPlusOp {

    public final TLAPlusExp operand;

    public TLAPlusUnaryOp(
            TLAPlusExp operand, TLAPlusOp.PrecedenceGroup precedenceGroup) {

        super(TLAPlusOp.Associativity.IRRELEVANT, precedenceGroup);
        this.operand = operand;
    }

    public List<TLAPlusExp> getChildren() {
        return Arrays.asList(this.operand);
    }
}
