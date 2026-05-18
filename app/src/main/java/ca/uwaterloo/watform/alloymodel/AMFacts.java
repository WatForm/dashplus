/*
    Storage and special functionality for command paragraphs
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyFactPara;
import java.util.*;

public class AMFacts extends AMFuns {

    // importParas never have names
    protected List<AlloyFactPara> facts = emptyList();

    protected AMFacts(AMFacts other) {
        super(other);
        this.facts = new ArrayList<AlloyFactPara>(other.facts);
    }

    protected AMFacts(AlloyFile alloyFile) {
        super(alloyFile);
        this.facts = emptyList();
        extractItemsOfClass(alloyFile.paras, AlloyFactPara.class).forEach(p -> this.addFactPara(p));
    }

    protected void resolve() {
        super.resolve();
        List<AlloyFactPara> newFacts = emptyList();
        for (AlloyFactPara factPara : this.facts) {
            AlloyFactPara newFactPara =
                    factPara.rebuild(((AlloyBlock) this.setMul(factPara.block)));
            newFacts.add(factPara);
        }
        this.facts = newFacts;
    }

    private void addFactPara(AlloyFactPara factPara) {
        // set the default multiplicities in the paragraph
        this.facts.add(factPara);
    }

    public List<AlloyFactPara> allFactParas() {
        // just to be safe, make a copy
        return new ArrayList<AlloyFactPara>(this.facts);
    }

    public void addFact(String name, List<AlloyExpr> eList) {
        this.addFactPara(new AlloyFactPara(new AlloyQnameExpr(name), new AlloyBlock(eList)));
    }

    public void addFact(String name, AlloyExpr expr) {
        this.addFactPara(
                new AlloyFactPara(new AlloyQnameExpr(name), new AlloyBlock(Arrays.asList(expr))));
    }
}
