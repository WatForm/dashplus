/*
    Storage and special functionality for command paragraphs
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.AlloyAssertPara;
import java.util.*;

public class AMThisAssertParas extends AMThisFactParas {

    // these sometimes have names (see AlloyAssertPara.getId())
    // so we could put a table here to lookup by name
    // but there would always be some unnamed ones
    protected List<AlloyAssertPara> asserts = emptyList();

    protected AMThisAssertParas() {}

    protected AMThisAssertParas(AMThisAssertParas other) {
        super(other);
        this.asserts = new ArrayList<AlloyAssertPara>(other.asserts);
    }

    // API
    public void addSMPara(AlloyAssertPara assertPara, String nameSpace) {
        Qname qname;
        if (assertPara.strLit.isPresent()) {
            qname = nameSpaceQname(nameSpace, assertPara.strLit.get().label);
        } else {
            qname = thisQname(assertPara.qname.get().getName());
        }
        this.createAssert(qname, assertPara.block);
    }

    /*
    protected void addPara(AlloyAssertPara assertPara, String nameSpace) {
        this.addSMPara(assertPara, nameSpace);
        this.asserts.add(assertPara);
    }
    */

    public void addPara(AlloyAssertPara assertPara) {
        this.addSMPara(assertPara, THIS_NAMESPACE);
        this.asserts.add(assertPara);
    }

    public List<AlloyAssertPara> allAssertParas() {
        // just to be safe, make a copy
        return new ArrayList<AlloyAssertPara>(this.asserts);
    }

    /*
    public AlloyAssertPara getAssertPara(String name) {
        for (AlloyAssertPara p : this.asserts) {
            if (p.getName().equals(name)) return p;
        }
        return null;
    }
    */
}
