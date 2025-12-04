package ca.uwaterloo.watform.tlaast.tlabinops;

import ca.uwaterloo.watform.tlaast.*;
import java.util.Arrays;
import java.util.List;

public abstract class TlaBinOp extends TlaOperator {

    public final TlaExp operandOne;
    public final TlaExp operandTwo;

    public TlaBinOp(
            TlaExp operandOne,
            TlaExp operandTwo,
            TlaOperator.Associativity associativity,
            TlaOperator.PrecedenceGroup precedenceGroup) {
        super(associativity, precedenceGroup);
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
    }

    public List<TlaExp> getChildren() {
        return Arrays.asList(this.operandOne, this.operandTwo);
    }
}
