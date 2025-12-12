// This is for predicates defined within a Dash model
// there is something separate for predicates defined in the Alloy
// parts of the file.

package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashFile;
import ca.uwaterloo.watform.utils.Pos;
import java.util.HashMap;
import java.util.List;

public class PredsDM extends EventsDM {

    private HashMap<String, PredEntry> pt = new HashMap<String, PredEntry>();

    public PredsDM() {
        super();
    }

    public PredsDM(DashFile d) {
        super(d);
    }

    public AlloyExpr predExp(String pfqn) {
        return pt.get(pfqn).exp;
    }

    public List<String> allPredNames() {
        return setToList(pt.keySet());
    }

    public boolean hasPreds() {
        return !pt.isEmpty();
    }

    public boolean containsPred(String pfqn) {
        return pt.containsKey(pfqn);
    }

    public String ptToString() {
        String s = new String("PRED TABLE\n");
        for (String k : pt.keySet()) {
            s += " ----- \n";
            s += k + "\n";
            s += pt.get(k).toString();
        }
        return s;
    }

    public void addPred(Pos pos, String pfqn, AlloyExpr e) {
        if (pt.containsKey(pfqn)) {
            DashModelErrors.duplicateName(pos, "pred", pfqn);
        } else {
            pt.put(pfqn, new PredEntry(pos, e));
        }
    }

    private class PredEntry {

        // this expression must be resolved in the context of the guard/action
        // it is used in
        // because otherwise we might have orphan parameter values
        // from the context where it is declared
        public Pos pos;
        public AlloyExpr exp;

        public PredEntry(Pos p, AlloyExpr e) {
            assert (p != null);
            this.pos = p;
            this.exp = e;
        }

        public String toString() {
            String s = new String();
            s += "exp: " + exp.toString() + "\n";
            return s;
        }
    }
}
