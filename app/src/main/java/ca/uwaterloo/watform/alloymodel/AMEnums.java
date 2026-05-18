/*
    Storage and special functionality for enum paragraphs

    Needs to go before arity checking because enums are shortcuts
    for one sig declarations.
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyEnumPara;
import java.util.*;

public class AMEnums extends AMArity {

    // these have a name (see AlloyEnumPara.getId())
    // so we could put a table here to lookup by name
    protected List<AlloyEnumPara> enums = emptyList();

    protected AMEnums(AMEnums other) {
        super(other);
        this.enums = new ArrayList<AlloyEnumPara>(other.enums);
    }

    protected void resolve() {
        super.resolve();
        // detect cycles is done in AMSigs
        // no need to set multiplicities because no fields or fact blocks
    }

    protected AMEnums(AlloyFile alloyFile) {
        super(alloyFile);
        this.enums = emptyList();
        extractItemsOfClass(alloyFile.paras, AlloyEnumPara.class).forEach(p -> this.addEnumPara(p));
    }

    // API for adding enums
    public void addEnumPara(AlloyEnumPara newEnumPara) {
        this.addToSigTable(newEnumPara);
        this.enums.add(newEnumPara);
        // no need to check anything else
        // no need to set multiplicities because no fields or fact blocks
    }

    public List<AlloyEnumPara> allEnumParas() {
        // just to be safe, make a copy
        return new ArrayList<AlloyEnumPara>(this.enums);
    }
}
