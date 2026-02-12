package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.foldLeft;

import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaUnionSet;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaFalse;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaIntLiteral;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaTrue;
import ca.uwaterloo.watform.tlaast.tlanaryops.TlaSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlloyToTlaHelpers {

    public static String sigSet(String sigName) {
        return sigName + SIG_SET_SUFFIX;
    }

    public static String sigConstraint(String sigName) {
        return sigName + SIG_CONSTRAINT_SUFFIX;
    }

    public static TlaVar fieldVar(String fieldName, String sigName) {
        return new TlaVar(sigName + SPECIAL + fieldName);
    }

    public static String unnamedFact(int n) {
        return UNNAMED_FACT_PREFIX + n;
    }

    public static TlaAppl SIG_SETS_PRIMED() {
        return TlaAppl(SIG_SETS_PRIMED);
    }

    public static TlaAppl SIG_SETS_UNPRIMED() {
        return TlaAppl(SIG_SETS_UNPRIMED);
    }

    public static final TlaSet NULL_SET() {
        return TlaSet(new ArrayList<>());
    }

    public static final TlaIntLiteral ZERO() {
        return TlaIntLiteral(0);
    }

    public static final TlaIntLiteral ONE() {
        return TlaIntLiteral(1);
    }

    public static final TlaTrue TRUE() {
        return TlaTrue();
    }

    public static final TlaFalse FALSE() {
        return TlaFalse();
    }

    public static TlaExp repeatedUnion(List<? extends TlaExp> operands) {
        int n = operands.size();
        if (n == 0) return NULL_SET();
        return foldLeft(operands.subList(1, n), TlaUnionSet::new, operands.get(0));
    }

    public static TlaExp repeatedAnd(List<? extends TlaExp> operands) {
        if (operands.size() == 0) return TlaTrue();
        return TlaAndList(operands);
    }

    public static TlaExp repeatedAnd(TlaExp... operands) {
        return TlaAndList(Arrays.asList(operands));
    }

    public static TlaExp repeatedOr(TlaExp... operands) {
        return TlaOrList(Arrays.asList(operands));
    }

    public static TlaExp repeatedOr(List<? extends TlaExp> operands) {
        if (operands.size() == 0) return TlaFalse();
        return TlaOrList(operands);
    }
}
