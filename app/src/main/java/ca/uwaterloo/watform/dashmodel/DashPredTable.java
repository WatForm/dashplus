// This is for predicates defined within a Dash model
// there is something separate for predicates defined in the Alloy
// parts of the file.

package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.Pos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashPredTable {

    private HashMap<String, PredElement> pt;
    private String tableName = "DashPred";

    public DashPredTable() {
        this.pt = new HashMap<String, PredElement>();
    }

    public class PredElement {

        // this expression must be resolved in the context of the guard/action
        // it is used in
        // because otherwise we might have orphan parameter values
        // from the context where it is declared
        public AlloyExpr exp;

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

    public void add(Pos pos, String n, AlloyExpr e) {
        if (pt.containsKey(n)) {
            DashModelErrors.duplicateName(pos, "pred", n);
        } else {
            pt.put(n, new PredElement(e));
        }
    }

    // so we can treat this as a table
    // to the outside world
    public PredElement get(String pfqn) {
        return pt.get(pfqn);
    }

    public List<String> keySet() {
        return setToList(pt.keySet());
    }

    public boolean isEmpty() {
        return pt.isEmpty();
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
