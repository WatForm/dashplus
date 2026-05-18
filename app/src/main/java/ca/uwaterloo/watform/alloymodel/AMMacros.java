/*
    Storage and special functionality for command paragraphs
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyMacroPara;
import java.util.*;

public class AMMacros extends AMImports {

    // these sometimes have names (see AlloyMacroPara.getId())
    // so we could put a table here to lookup by name
    // but there would always be some unnamed ones
    protected List<AlloyMacroPara> macros = emptyList();

    protected AMMacros(AMMacros other) {
        super(other);
        // creates a new list but not a deep copy
        this.macros = new ArrayList<AlloyMacroPara>(other.macros);
    }

    protected AMMacros(AlloyFile alloyFile) {
        super(alloyFile);
        this.macros = emptyList();
        extractItemsOfClass(alloyFile.paras, AlloyMacroPara.class)
                .forEach(p -> this.addMacroPara(p));
    }

    protected void resolve() {
        super.resolve();
        List<AlloyMacroPara> newMacros = emptyList();
        for (AlloyMacroPara macroPara : this.macros) {
            AlloyMacroPara newMacroPara =
                    macroPara.rebuild(
                            ((AlloyBlock) (macroPara.block.map(b -> this.setMul(b)).orElse(null))),
                            (macroPara.sub.map(b -> this.setMul(b)).orElse(null)));
            newMacros.add(newMacroPara);
        }
        this.macros = newMacros;
    }

    private void addMacroPara(AlloyMacroPara macroPara) {
        this.macros.add(macroPara);
    }

    public List<AlloyMacroPara> allMacroParas() {
        // just to be safe, make a copy of the list
        return new ArrayList<AlloyMacroPara>(this.macros);
    }
}
