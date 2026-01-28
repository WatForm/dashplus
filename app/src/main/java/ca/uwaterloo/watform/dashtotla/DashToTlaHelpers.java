package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaUnionSet;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaFalse;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaIntLiteral;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaStringLiteral;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaTrue;
import ca.uwaterloo.watform.tlaast.tlanaryops.TlaSet;
import java.util.ArrayList;
import java.util.List;

public class DashToTlaHelpers {

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

    // variable creators
    public static TlaVar CONF() {
        return TlaVar(CONF);
    }

    public static TlaVar EVENTS() {
        return TlaVar(EVENTS);
    }

    public static TlaVar TRANS_TAKEN() {
        return TlaVar(TRANS_TAKEN);
    }

    public static TlaVar SCOPES_USED() {
        return TlaVar(SCOPES_USED);
    }

    public static TlaVar STABLE() {
        return TlaVar(STABLE);
    }

    public static TlaAppl INIT() {
        return TlaAppl(INIT);
    }

    public static TlaAppl TYPE_OK() {
        return TlaAppl(TYPE_OK);
    }

    public static TlaAppl STUTTER() {
        return TlaAppl(STUTTER);
    }

    public static TlaAppl SMALL_STEP() {
        return TlaAppl(SMALL_STEP);
    }

    public static TlaAppl SOME_TRANSITION() {
        return TlaAppl(SOME_TRANSITION);
    }

    public static TlaAppl SOME_PRE_TRANSITION() {
        return TlaAppl(SOME_PRE_TRANSITION);
    }

    public static TlaAppl NONE_TRANSITION() {
        return TlaAppl(NONE_TRANSITION);
    }

    public static TlaAppl VALID_PRIMED() {
        return TlaAppl(VALID_PRIMED);
    }

    public static TlaAppl VALID_UNPRIMED() {
        return TlaAppl(VALID_UNPRIMED);
    }

    public static TlaAppl SINGLE_ENV_INPUT() {
        return TlaAppl(SINGLE_ENV_INPUT);
    }

    public static TlaAppl INTERNAL_EVENTS() {
        return TlaAppl(INTERNAL_EVENTS);
    }

    public static TlaAppl ENVIRONMENTAL_EVENTS() {
        return TlaAppl(ENVIRONMENTAL_EVENTS);
    }

    public static TlaStringLiteral NONE_TRANSITION_LITERAL() {
        return TlaStringLiteral(NONE_TRANSITION_LITERAL);
    }

    public static final String paramVar(String varName) {
        return ARG + varName;
    }

    public static final String validDefn(String varName) {
        return VALID + varName;
    }

    public static final TlaVar ARGUMENT() {
        return TlaVar(ARG);
    }

    public static String tlaFQN(String dashFQN) {
        return SPECIAL + dashFQN.replace(QUALIFIER, SPECIAL);
    }

    public static String takenTransTlaFQN(String transitionFQN) {
        return TAKEN + tlaFQN(transitionFQN);
    }

    public static String preTransTlaFQN(String transitionFQN) {
        return PRE + tlaFQN(transitionFQN);
    }

    public static String postTransTlaFQN(String transitionFQN) {
        return POST + tlaFQN(transitionFQN);
    }

    public static String enabledTransTlaFQN(String transitionFQN) {
        return ENABLED + tlaFQN(transitionFQN);
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

    public static TlaExp repeatedOr(List<? extends TlaExp> operands) {
        if (operands.size() == 0) return TlaFalse();
        return TlaOrList(operands);
    }

    // TODO varargs

}
