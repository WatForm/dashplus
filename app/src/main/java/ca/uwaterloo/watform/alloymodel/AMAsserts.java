/*
    Storage and special functionality for command paragraphs
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyAssertPara;
import java.util.*;

public class AMAsserts extends AMFacts {

    // these sometimes have names (see AlloyAssertPara.getId())
    // so we could put a table here to lookup by name
    // but there would always be some unnamed ones
    protected List<AlloyAssertPara> asserts = emptyList();

    protected AMAsserts(AMAsserts other) {
        super(other);
        this.asserts = new ArrayList<AlloyAssertPara>(other.asserts);
    }

    protected AMAsserts(AlloyFile alloyFile) {
        super(alloyFile);
        extractItemsOfClass(alloyFile.paras, AlloyAssertPara.class)
                .forEach(p -> this.addAssertPara(p));
    }

    protected void resolve() {
        super.resolve();
        List<AlloyAssertPara> newAsserts = emptyList();
        for (AlloyAssertPara assertPara : this.asserts) {
            AlloyAssertPara newAssertPara =
                    assertPara.rebuild(((AlloyBlock) this.setMul(assertPara.block)));
            newAsserts.add(newAssertPara);
        }
        this.asserts = newAsserts;
    }

    public void addAssertPara(AlloyAssertPara assertPara) {
        this.asserts.add(assertPara);
    }

    public List<AlloyAssertPara> allAssertParas() {
        // just to be safe, make a copy
        return new ArrayList<AlloyAssertPara>(this.asserts);
    }

    public AlloyAssertPara getAssertPara(String name) {
        for (AlloyAssertPara p : this.asserts) {
            if (p.getName().equals(name)) return p;
        }
        return null;
    }
}
