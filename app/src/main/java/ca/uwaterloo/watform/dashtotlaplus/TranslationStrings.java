package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaAppl;
import ca.uwaterloo.watform.tlaplusast.TlaVar;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusAnd;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusOr;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusUnionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusFalse;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TLAPlusTrue;
import ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators.TLAPlusSet;
import java.util.ArrayList;
import java.util.List;

class TranslationStrings {
    // this class stores information about things that are common to every part of the translation

    public static final String SPECIAL = "_"; // special character used for naming translation artefacts
    public static final String TRANSITIONS = SPECIAL + "transitions";
    public static final String NEXT = SPECIAL + "Next";
    public static final String INIT = SPECIAL + "Init";
    public static final String TYPEOK = SPECIAL + "typeOK";

    public static final String SET_ = SPECIAL + "set";
    public static final String CONF = SPECIAL + "conf";
    public static final String EVENTS = SPECIAL + "events";
    public static final String TRANS_TAKEN = SPECIAL + "trans" + SPECIAL + "taken";
    public static final String SCOPE_USED = SPECIAL + "scope" + SPECIAL + "used";
    public static final String STABLE = SPECIAL + "stable";

    public static final String TAKEN = SPECIAL + "taken";
    public static final String PRE = SPECIAL + "pre";
    public static final String POST = SPECIAL + "post";
    public static final String ENABLED = SPECIAL + "enabled";

    public static final String NEXT_IS_STABLE = SPECIAL + "next" + SPECIAL + "stable";
    public static final String SOME_TRANSITION = SPECIAL + "some" + SPECIAL + "transition";

    public static final String ARG = "arg";

    public static final String NONE = "none";

    public static final String QUALIFIER = DashStrings.SLASH;

    public static TlaVar getConf() {
        return new TlaVar(CONF);
    }

    public static TlaVar getScopeUsed() {
        return new TlaVar(SCOPE_USED);
    }

    public static String getSetConf() {
        return SET_ + CONF;
    }

    public static String getArg(String v) {
        return SPECIAL + ARG + v;
    }

    public static String getSetScopesUsed() {
        return SET_ + SCOPE_USED;
    }

    public static TlaVar getStable() {
        return new TlaVar(STABLE);
    }

    public static TlaVar getEvents() {
        return new TlaVar(EVENTS);
    }

    public static String getSetEvents() {
        return SET_ + EVENTS;
    }

    public static TlaVar getTransTaken() {
        return new TlaVar(TRANS_TAKEN);
    }

    public static String getSetTransTaken() {
        return SET_ + TRANS_TAKEN;
    }

    public static TlaVar getScopesUsed() {
        return new TlaVar(SCOPE_USED);
    }

    public static TlaFormulaAppl getNext() {
        return new TlaFormulaAppl(NEXT);
    }

    public static TlaFormulaAppl getInit() {
        return new TlaFormulaAppl(INIT);
    }

    public static TLAPlusSet getNullSet() {
        return new TLAPlusSet(new ArrayList<>());
    }

    public static String getStateFormulaName(String stateFullyQualifiedName) {
        return SPECIAL + stateFullyQualifiedName.replace(QUALIFIER, SPECIAL);
    }

    public static String getTakenTransFormulaName(String transitionFullyQualifiedName) {
        return SPECIAL + TAKEN + SPECIAL + getTransFormulaName(transitionFullyQualifiedName);
    }

    public static String getPreTransFormulaName(String transitionFullyQualifiedName) {
        return PRE + SPECIAL + getTransFormulaName(transitionFullyQualifiedName);
    }

    public static String getPostTransFormulaName(String transitionFullyQualifiedName) {
        return POST + SPECIAL + getTransFormulaName(transitionFullyQualifiedName);
    }

    public static String getEnabledTransFormulaName(String transitionFullyQualifiedName) {
        return ENABLED + SPECIAL + getTransFormulaName(transitionFullyQualifiedName);
    }

    public static String getTransFormulaName(String transitionFullyQualifiedName) {
        return transitionFullyQualifiedName.replace(QUALIFIER, SPECIAL);
    }



    // todo: combine these into one

    public static TlaExp repeatedUnion(List<? extends TlaExp> operands) {
        if (operands.size() == 0) return getNullSet();
        if (operands.size() == 1) return operands.get(0);
        TlaExp top = new TLAPlusUnionSet(operands.get(0), operands.get(1));
        for (int i = 2; i < operands.size(); i++) {
            top = new TLAPlusUnionSet(top, operands.get(i));
        }
        return top;
    }

    public static TlaExp repeatedAnd(List<? extends TlaExp> operands) {
        if (operands.size() == 0) return new TLAPlusTrue();
        if (operands.size() == 1) return operands.get(0);
        TlaExp top = new TLAPlusAnd(operands.get(0), operands.get(1));
        for (int i = 2; i < operands.size(); i++) {
            top = new TLAPlusAnd(top, operands.get(i));
        }
        return top;
    }

    public static TlaExp repeatedOr(List<? extends TlaExp> operands) {
        if (operands.size() == 0) return new TLAPlusFalse();
        if (operands.size() == 1) return operands.get(0);
        TlaExp top = new TLAPlusOr(operands.get(0), operands.get(1));
        for (int i = 2; i < operands.size(); i++) {
            top = new TLAPlusOr(top, operands.get(i));
        }
        return top;
    }
}
