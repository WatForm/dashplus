/*
    Storage and special functionality for command paragraphs
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.parser.Parser.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara;
import ca.uwaterloo.watform.paravisitor.TestAndReplaceExprParaVis;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class AMImports extends AMScopes {

    // importParas never have names
    protected List<AlloyImportPara> imports = emptyList();

    public AMImports(AlloyFile alloyFile) {
        super(alloyFile);
        this.imports = emptyList();
        extractItemsOfClass(alloyFile.paras, AlloyImportPara.class)
                .forEach(p -> this.addImportPara(p));
    }

    protected AMImports(AMImports other) {
        super(other);
        this.imports = new ArrayList<AlloyImportPara>(other.imports);
    }

    private void loadImport(AlloyImportPara importPara) {
        /*
            1) add in exactly ordered or non-ordered
            2) substitute arguments for parameters
            3) rename per aliases
            4) initialize paragraphs into sigTable, etc
        */

        // open name[A, B] as X -> [A,B]
        List<AlloySigRefExpr> valsToSubstitute = importPara.sigRefs;

        // will be only one modPara in the importedFile
        AlloyModulePara modPara =
                extractItemsOfClass(importPara.importedFile.paras, AlloyModulePara.class).get(0);
        // parameters to substitute for
        List<AlloyModulePara.AlloyModuleArg> modArgs = modPara.moduleArgs;
        if (modArgs.size() != valsToSubstitute.size()) {
            throw AlloyModelError.importArgsNumDoesntMatch(importPara.pos, importPara.toString());
        }

        // paras in importedFile to walk over

        // no need for module name para
        List<AlloyPara> paras =
                extractItemsNotOfClass(importPara.importedFile.paras, AlloyModulePara.class);
        // no need for cmds or asserts
        paras = extractItemsNotOfClass(paras, AlloyCmdPara.class);
        paras = extractItemsNotOfClass(paras, AlloyAssertPara.class);

        // 1)
        for (int i = 0; i < modArgs.size(); i++) {
            if (modArgs.get(i).isExactly) {
                if (importPara.qname.equals("util/ordering")) {
                    // ordering module import is handled
                    // specially so can produce error
                    // in AMScopes about applying only
                    // to top-level sigs
                    // Assumption: ordering module has only one param
                    this.addOrderedSigWithExactScope(valsToSubstitute.get(i).getName());
                } else {
                    this.addNonOrderedSigWithExactScope(valsToSubstitute.get(i).getName());
                }
            }
        }

        // 2)
        Map<AlloyQnameExpr, AlloySigRefExpr> substMap = new HashMap<>();
        for (int i = 0; i < modArgs.size(); i++) {
            substMap.put(modArgs.get(i).qname, valsToSubstitute.get(i));
        }
        // System.out.println(substMap);

        // replace modArg name with value to substitute

        TestAndReplaceExprParaVis vis =
                new TestAndReplaceExprParaVis(
                        e -> substMap.keySet().contains(e), e -> ((AlloyExpr) substMap.get(e)));

        List<AlloyPara> newParas = emptyList();
        for (AlloyPara para : paras) {
            newParas.add(vis.visit(para));
        }
        System.out.println(new AlloyFile(newParas));
        // 3)

        // 4)
        // what do we do with these paras now???
        // add to sigTable, fieldTable, preds/funs table
        // TODO: I think we need a fact table also
        // or do we want to just add them to our sigParas, factParas, etc.????

    }

    protected void resolve() {
        super.resolve();
        List<AlloyImportPara> newImports = emptyList();
        for (AlloyImportPara importPara : this.imports) {
            AlloyImportPara newImportPara = (AlloyImportPara) this.setMul(importPara);
            newImports.add(newImportPara);
        }
        this.imports = newImports;
    }

    public void addImportPara(AlloyImportPara importPara) {
        this.imports.add(importPara);
        loadImport(importPara);
    }

    public List<AlloyImportPara> allImportParas() {
        // just to be safe, make a copy
        return new ArrayList<AlloyImportPara>(this.imports);
    }

    public void addUtilBooleanImport() {
        this.addImport(List.of(AlloyStrings.utilName, AlloyStrings.booleanName));
    }

    // import name1/name2[sigName] as asName
    public void addImport(List<String> names, String sigName, String asName) {
        String fileName = String.join("/", names);
        this.addImportPara(
                new AlloyImportPara(
                        false,
                        new AlloyQnameExpr(mapBy(names, x -> new AlloyNameExpr(x))),
                        List.of(new AlloyQnameExpr(sigName)),
                        new AlloyQnameExpr(asName),
                        parseImport(Pos.UNKNOWN, fileName)));
    }

    // import name1/name2[sigName]
    public void addImport(List<String> names, String sigName) {
        String fileName = String.join("/", names);
        this.addImportPara(
                new AlloyImportPara(
                        false,
                        new AlloyQnameExpr(mapBy(names, x -> new AlloyNameExpr(x))),
                        List.of(new AlloyQnameExpr(sigName)),
                        null,
                        parseImport(Pos.UNKNOWN, fileName)));
    }

    // import name1/name2[sigName1, sigName2]
    public void addImport(List<String> names, List<AlloySigRefExpr> sigNames) {
        String fileName = String.join("/", names);
        this.addImportPara(
                new AlloyImportPara(
                        false,
                        new AlloyQnameExpr(mapBy(names, x -> new AlloyNameExpr(x))),
                        sigNames,
                        null,
                        parseImport(Pos.UNKNOWN, fileName)));
    }

    // import name1/name2
    public void addImport(List<String> names) {
        String fileName = String.join("/", names);
        this.addImportPara(
                new AlloyImportPara(
                        false,
                        new AlloyQnameExpr(mapBy(names, x -> new AlloyNameExpr(x))),
                        emptyList(),
                        null,
                        parseImport(Pos.UNKNOWN, fileName)));
    }
}
