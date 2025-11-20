package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaApplication;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVariable;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;

public class Util {

    public static final String TRANSITIONS = "_transitions";
    public static final String NEXT = "_next";
    public static final String INIT = "_init";
    public static final String TYPEOK = "_typeOK";
    public static final String CONF = "_conf";

    public static TLAPlusVariable getConf() {
        return new TLAPlusVariable(CONF);
    }

    public static TLAPlusFormulaApplication getNext() {
        return new TLAPlusFormulaApplication(NEXT);
    }

    public static TLAPlusFormulaApplication getInit() {
        return new TLAPlusFormulaApplication(INIT);
    }

    public static void makeInit(TLAPlusModel tm) {}

    public static void makeNext(TLAPlusModel tm) {}
}
