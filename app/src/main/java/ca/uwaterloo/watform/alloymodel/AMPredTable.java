package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPredPara;
import ca.uwaterloo.watform.utils.*;
import java.util.*;
import java.util.function.BiFunction;

public class AMPredTable extends AMSigTable {

    // maps predicate to its number of columns: #args+1
    private Map<String, Integer> predTable = new LinkedHashMap<>();

    protected AMPredTable(AlloyFile alloyFile) {
        super(alloyFile);
    }

    protected AMPredTable(AMPredTable other) {
        super(other);
        this.predTable = new LinkedHashMap<>(other.predTable);
    }

    protected void resolve(
            BiFunction<AlloyExpr, String, CalcAritySetMulDefaultsExprVis.Result> arityAndSetMul) {
        super.resolve(arityAndSetMul);
        // TODO: getting arity of arguments
        // TODO: checking if predicate calling is in a cycle
    }

    public List<String> allPreds() {
        return setToList(this.predTable.keySet());
    }

    public void entry(Pos p, String predName, int numArgs) {
        // System.out.println(this.allPreds());
        // System.out.println(this.allSigs());
        if (this.allPreds().contains(predName)
                || this.allFields().contains(predName)
                || this.allSigs().contains(predName))
            throw AlloyModelError.duplicatePredName(p, predName);
        else this.predTable.put(predName, numArgs);
    }

    public boolean isPred(String predName) {
        return this.allPreds().contains(predName);
    }

    public Integer predArity(String predName) {
        if (this.allPreds().contains(predName)) {
            return this.predTable.get(predName);
        } else throw AlloyModelImplError.predNotFound(predName);
    }

    public void addToPredTable(AlloyPredPara predPara) {
        String predName = predPara.getName();
        // TODO: something better than just counting them??
        Integer numArgs = 0;
        for (AlloyDecl d : predPara.arguments) {
            numArgs += d.expand().size();
        }
        // System.out.println("Adding to pred table: " + predName + Integer.toString(numArgs + 1));
        entry(predPara.pos, predName, numArgs + 1);
    }

    public void removeFromPredTable(String predName) {
        // this won't work well for overloading
        if (!this.allPreds().contains(predName)) {
            throw AlloyModelImplError.predNotFound(predName);
        } else {
            this.predTable.remove(predName);
        }
    }
}
