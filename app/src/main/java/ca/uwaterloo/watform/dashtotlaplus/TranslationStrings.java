package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.tlaplusast.TlaExp;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaAppl;
import ca.uwaterloo.watform.tlaplusast.TlaFormulaDecl;
import ca.uwaterloo.watform.tlaplusast.TlaVar;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaAnd;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaOr;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TlaUnionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TlaFalse;
import ca.uwaterloo.watform.tlaplusast.tlaplusliterals.TlaTrue;
import ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators.TlaSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

class TranslationStrings {
    // this class stores information about things that are common to every part of the translation

    public static final String SPECIAL = "_";
    // special character used for naming translation artefacts

    public static final String TRANSITIONS = SPECIAL + "transitions";

    public static final String TAKEN = SPECIAL + "taken";
    public static final String PRE = SPECIAL + "pre";
    public static final String POST = SPECIAL + "post";
    public static final String ENABLED = SPECIAL + "enabled";

    public static final String ARG = SPECIAL + "arg";
    public static final String ALL = SPECIAL + "all";

    public static final String QUALIFIER = DashStrings.SLASH;

    public static class CommonFormula {
        public final String name;

        public CommonFormula(String name) {
            this.name = name;
        }

        public TlaFormulaDecl decl() {
            return new TlaFormulaDecl(name);
        }

        public TlaFormulaAppl appl() {
            return new TlaFormulaAppl(name);
        }
    }

    public static class CommonVar {
        public final String name;

        public CommonVar(String name) {
            this.name = name;
        }

        public TlaVar globalVar() {
            return new TlaVar(this.name);
        }

        public TlaVar paramVar() {
            return new TlaVar(ARG + this.name);
        }

        public TlaFormulaDecl typeDecl() {
            return new TlaFormulaDecl(ALL + this.name);
        }

        public TlaFormulaAppl typeAppl() {
            return new TlaFormulaAppl(ALL + this.name);
        }
    }

    public static final CommonVar CONF = new CommonVar(SPECIAL + "conf");
    public static final CommonVar EVENTS = new CommonVar(SPECIAL + "events");
    public static final CommonVar TRANS_TAKEN =
            new CommonVar(SPECIAL + "trans" + SPECIAL + "taken");
    public static final CommonVar SCOPE_USED = new CommonVar(SPECIAL + "scope" + SPECIAL + "used");
    public static final CommonVar STABLE = new CommonVar(SPECIAL + "stable");
    public static final CommonVar CT = new CommonVar(SPECIAL + "ct");

    public static final CommonFormula INIT = new CommonFormula(SPECIAL + "Init");
    public static final CommonFormula NEXT = new CommonFormula(SPECIAL + "Next");
    public static final CommonFormula TYPE_OK = new CommonFormula(SPECIAL + "TypeOK");

    public static final CommonFormula STUTTER = new CommonFormula(SPECIAL + "stutter");
    public static final CommonFormula SMALL_STEP =
            new CommonFormula(SPECIAL + "small" + SPECIAL + "step");
    public static final CommonFormula SOME_TRANSITION =
            new CommonFormula(SPECIAL + "some" + SPECIAL + "transition");
    public static final CommonFormula SOME_PRE_TRANSITION =
            new CommonFormula(SPECIAL + "some" + SPECIAL + PRE + SPECIAL + "transition");
    public static final String NEXT_IS_STABLE =
            SPECIAL + "next" + SPECIAL + "is" + SPECIAL + "stable";

    public static final TlaSet NULL_SET = new TlaSet(new ArrayList<>());

    public static String getStateFormulaName(String stateFQN) {
        return SPECIAL + stateFQN.replace(QUALIFIER, SPECIAL);
    }

    public static String getTakenTransFormulaName(String transitionFQN) {
        return SPECIAL + TAKEN + SPECIAL + getTransFormulaName(transitionFQN);
    }

    public static String getPreTransFormulaName(String transitionFQN) {
        return PRE + SPECIAL + getTransFormulaName(transitionFQN);
    }

    public static String getPostTransFormulaName(String transitionFQN) {
        return POST + SPECIAL + getTransFormulaName(transitionFQN);
    }

    public static String getEnabledTransFormulaName(String transitionFQN) {
        return ENABLED + SPECIAL + getTransFormulaName(transitionFQN);
    }

    public static String getTransFormulaName(String transitionFQN) {
        return transitionFQN.replace(QUALIFIER, SPECIAL);
    }

    private static TlaExp reduceBinaryOperation(
            List<? extends TlaExp> operands,
            BiFunction<TlaExp, TlaExp, TlaExp> constructor,
            TlaExp emptyCaseResult) {

        if (operands.isEmpty()) return emptyCaseResult;
        if (operands.size() == 1) return operands.get(0);

        TlaExp result = constructor.apply(operands.get(0), operands.get(1));
        for (int i = 2; i < operands.size(); i++)
            result = constructor.apply(result, operands.get(i));
        return result;
    }

    public static TlaExp repeatedUnion(List<? extends TlaExp> operands) {
        return reduceBinaryOperation(operands, TlaUnionSet::new, NULL_SET);
    }

    public static TlaExp repeatedAnd(List<? extends TlaExp> operands) {
        return reduceBinaryOperation(operands, TlaAnd::new, new TlaTrue());
    }

    public static TlaExp repeatedOr(List<? extends TlaExp> operands) {
        return reduceBinaryOperation(operands, TlaOr::new, new TlaFalse());
    }
}
