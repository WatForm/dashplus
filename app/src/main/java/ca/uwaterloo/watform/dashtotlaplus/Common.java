package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaApplication;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVariable;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusUnionSet;
import ca.uwaterloo.watform.tlaplusast.tlaplusnaryoperators.TLAPlusSet;
import java.util.ArrayList;
import java.util.List;

class Common {
    // this class stores information about things that are common to every part of the translation

    public static final String SPECIAL = "_";
    public static final String TRANSITIONS = SPECIAL + "transitions";
    public static final String NEXT = SPECIAL + "Next";
    public static final String INIT = SPECIAL + "Init";
    public static final String TYPEOK = SPECIAL + "typeOK";

    public static final String CONF = SPECIAL + "conf";
    public static final String SET_CONF = SPECIAL + "set_conf";
    public static final String EVENTS = SPECIAL + "events";
    public static final String SET_EVENTS = SPECIAL + "set_events";

    public static final String QUALIFIER = DashStrings.SLASH;

    public static TLAPlusVariable getConf() {
        return new TLAPlusVariable(CONF);
    }

    public static TLAPlusVariable getEvents() {
        return new TLAPlusVariable(EVENTS);
    }

    public static TLAPlusFormulaApplication getNext() {
        return new TLAPlusFormulaApplication(NEXT);
    }

    public static TLAPlusFormulaApplication getInit() {
        return new TLAPlusFormulaApplication(INIT);
    }

    public static TLAPlusSet getNullSet() {
        return new TLAPlusSet(new ArrayList<>());
    }

    public static String getStateFormulaName(String stateFullyQualifiedName) {
        return SPECIAL + stateFullyQualifiedName.replace(QUALIFIER, SPECIAL);
    }

    public static TLAPlusExpression repeatedUnion(List<? extends TLAPlusExpression> operands) {
        if (operands.size() == 0) return getNullSet();
        if (operands.size() == 1) return operands.get(0);
        TLAPlusExpression top = new TLAPlusUnionSet(operands.get(0), operands.get(1));
        for (int i = 2; i < operands.size(); i++) {
            top = new TLAPlusUnionSet(top, operands.get(i));
        }
        return top;
    }
}
