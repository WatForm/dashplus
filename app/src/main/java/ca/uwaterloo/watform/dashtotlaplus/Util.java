package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.tlaplusast.TLAPlusVariable;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;

public class Util {

    public static TLAPlusVariable getConf() {
        return new TLAPlusVariable("_conf");
    }

    public static final String TRANSITIONS = "_transitions";

    public static void makeInit(TLAPlusModel tm) {}

    public static void makeNext(TLAPlusModel tm) {}
}
