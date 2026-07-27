/*
    Facts are never looked up by name anywhere so only namespace is used.
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyFactPara;
import java.util.*;

public class AMThisFactParas extends AMThisFunParas {

    protected List<AlloyFactPara> facts = emptyList();

    // init -----------

    protected AMThisFactParas() {}

    protected AMThisFactParas(AMThisFactParas other) {
        super(other);
        this.facts = new ArrayList<AlloyFactPara>(other.facts);
    }

    protected void addSMPara(AlloyFactPara factPara, String nameSpace) {
        for (AlloyExpr expr : factPara.block.exprs) {
            this.createConstraint(nameSpace, expr);
        }
    }

    /*
    protected void addPara(AlloyFactPara factPara, String nameSpace) {
        addSMPara(factPara, nameSpace);
        this.facts.add(factPara);
    }
    */

    protected void addPara(AlloyFactPara factPara) {
        addSMPara(factPara, THIS_NAMESPACE);
        this.facts.add(factPara);
    }

    // API -------

    public void addFact(String name, List<AlloyExpr> eList) {
        this.addPara(new AlloyFactPara(new AlloyQnameExpr(name), new AlloyBlock(eList)));
    }

    public void addFact(String name, AlloyExpr eList) {
        this.addPara(new AlloyFactPara(new AlloyQnameExpr(name), new AlloyBlock(List.of(eList))));
    }

    public List<AlloyFactPara> allFactParas() {
        // just to be safe, make a copy
        return new ArrayList<AlloyFactPara>(this.facts);
    }
}
