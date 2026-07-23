/*
    Storage and special functionality for enum paragraphs

    Needs to go before arity checking because enums are shortcuts
    for one sig declarations.
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyEnumPara;
import java.util.*;

public class AMThisEnumParas extends SMResolve {

    // these have a name (see AlloyEnumPara.getId())
    // so we could put a table here to lookup by name
    protected List<AlloyEnumPara> enums = emptyList();

    protected AMThisEnumParas() {}

    protected AMThisEnumParas(AMThisEnumParas other) {
        this.enums = new ArrayList<AlloyEnumPara>(other.enums);
    }

    /*
    protected void addPara(AlloyEnumPara enumPara, String nameSpace) {
        this.addSMPara(enumPara, nameSpace);
        this.enums.add(enumPara);
    }
    */

    public void addPara(AlloyEnumPara enumPara) {
        this.addSMPara(enumPara, THIS_NAMESPACE);
        this.enums.add(enumPara);
    }

    public void addSMPara(AlloyEnumPara enumPara, String nameSpace) {
        // enum Color { Red, Green, Blue }
        // means
        // abstract sig Color {}
        // one sig Red extends Color {}
        // one sig Green extends Color {}
        // one sig Blue extends Color {}
        // no fields
        // no facts

        // create the sig of the parent
        String parent = enumPara.qname.getName();
        Qname parentQname = nameSpaceQname(nameSpace, parent);
        this.createSig(enumPara.pos, parentQname, SigData.abstractSigData(enumPara.pos));

        // create the sigs of the children
        for (AlloyQnameExpr enumValue : enumPara.qnames) {
            Qname valQname = nameSpaceQname(nameSpace, enumValue.getName());
            this.createSig(enumPara.pos, valQname, SigData.oneSigData(enumPara.pos, parentQname));
            this.createNonOrderedSigWithExactScopeValue(valQname, 1);
        }
        this.createOrderedSigWithExactScopeValue(parentQname, enumPara.qnames.size());
    }

    public List<AlloyEnumPara> allEnumParas() {
        // just to be safe, make a copy
        return new ArrayList<AlloyEnumPara>(this.enums);
    }
}
