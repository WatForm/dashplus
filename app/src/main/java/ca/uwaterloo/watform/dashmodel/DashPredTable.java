// This is for predicates defined within a Dash model
// there is something separate for predicates defined in the Alloy
// parts of the file.

package ca.uwaterloo.watform.dashmodel;

// import java.util.Set;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import java.util.ArrayList;
import java.util.HashMap;
// import java.util.LinkedHashMap;
// import java.util.Collections;
import java.util.List;

public class DashPredTable {

    private HashMap<String, PredElement> pt;
    public String name = "DashPred";

    public DashPredTable() {
        this.pt = new HashMap<String, PredElement>();
    }

    public class PredElement {

        // this expression must be resolved in the context of the guard/action
        // it is used in
        // because otherwise we might have orphan parameter values
        // from the context where it is declared
        private AlloyExpr exp;

        public PredElement(AlloyExpr e) {
            this.exp = e;
        }

        public String toString() {
            String s = new String();
            s += "exp: " + exp.toString() + "\n";
            return s;
        }
    }

    public String toString() {
        String s = new String("PRED TABLE\n");
        for (String k : pt.keySet()) {
            s += " ----- \n";
            s += k + "\n";
            s += pt.get(k).toString();
        }
        return s;
    }

    public Boolean addPred(String n, AlloyExpr e) {
        if (pt.containsKey(n)) return false;
        else {
            pt.put(n, new PredElement(e));
            return true;
        }
    }

    public boolean contains(String pfqn) {
        return pt.containsKey(pfqn);
    }

    public AlloyExpr getExp(String pfqn) {
        return pt.get(pfqn).exp;
    }

    public List<String> getAllNames() {
        return new ArrayList<String>(pt.keySet());
    }
}
