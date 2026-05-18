/*
    Storage and special functionality for command paragraphs
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyImportPara;
import java.util.*;

public class AMImports extends AMModules {

    // importParas never have names
    protected List<AlloyImportPara> imports = emptyList();

    protected AMImports(AMImports other) {
        super(other);
        this.imports = new ArrayList<AlloyImportPara>(other.imports);
    }

    public AMImports(AlloyFile alloyFile) {
        super(alloyFile);
        this.imports = emptyList();
        extractItemsOfClass(alloyFile.paras, AlloyImportPara.class)
                .forEach(p -> this.addImportPara(p));
    }

    protected void resolve() {
        super.resolve();
    }

    public void addImportPara(AlloyImportPara importPara) {
        // no need to set default multiplicities
        this.imports.add(importPara);
    }

    public List<AlloyImportPara> allImportParas() {
        // just to be safe, make a copy
        return new ArrayList<AlloyImportPara>(this.imports);
    }

    public void addImport(List<String> names, String sigName, String asName) {
        this.addImportPara(
                new AlloyImportPara(
                        false,
                        new AlloyQnameExpr(mapBy(names, x -> new AlloyNameExpr(x))),
                        List.of(new AlloyQnameExpr(sigName)),
                        new AlloyQnameExpr(asName)));
    }

    public void addImport(List<String> names, String sigName) {
        this.addImportPara(
                new AlloyImportPara(
                        false,
                        new AlloyQnameExpr(mapBy(names, x -> new AlloyNameExpr(x))),
                        List.of(new AlloyQnameExpr(sigName)),
                        null));
    }

    public void addImport(List<String> names) {
        this.addImportPara(
                new AlloyImportPara(
                        false,
                        new AlloyQnameExpr(mapBy(names, x -> new AlloyNameExpr(x))),
                        emptyList(),
                        null));
    }
}
