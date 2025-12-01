package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.Arrays;
import java.util.List;

public abstract class TLAPlusBinOp extends TLAPlusOp {

    public final TLAPlusExp operandOne;
    public final TLAPlusExp operandTwo;

    public TLAPlusBinOp(
            TLAPlusExp operandOne,
            TLAPlusExp operandTwo,
            TLAPlusOp.Associativity associativity,
            TLAPlusOp.PrecedenceGroup precedenceGroup) {
        super(associativity, precedenceGroup);
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
    }

    public List<TLAPlusExp> getChildren() {
        return Arrays.asList(this.operandOne, this.operandTwo);
    }
}
