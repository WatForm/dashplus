package ca.uwaterloo.watform.tlaast.tlaunops;

import ca.uwaterloo.watform.tlaast.*;
import java.util.Arrays;
import java.util.List;

public abstract class TlaUnaryOp extends TlaOperator {

    public final TlaExp operand;

    public TlaUnaryOp(TlaExp operand, TlaOperator.PrecedenceGroup precedenceGroup) {

        super(TlaOperator.Associativity.IRRELEVANT, precedenceGroup);
        this.operand = operand;
    }

    public List<TlaExp> getChildren() {
        return Arrays.asList(this.operand);
    }
}
