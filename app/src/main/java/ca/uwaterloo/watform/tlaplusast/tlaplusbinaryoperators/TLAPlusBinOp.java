package ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.Arrays;
import java.util.List;

public abstract class TLAPlusBinOp extends TlaOperator {

    public final TlaExp operandOne;
    public final TlaExp operandTwo;

    public TLAPlusBinOp(
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
