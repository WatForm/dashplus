/*
    Storage and special functionality for command paragraphs
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.parser.Parser.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.paravisitor.TestAndReplaceExprParaVis;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

// must come after everything else b/c it uses cmd, fact, etc add methods
public class AMThisImportParas extends AMThisCmdParas {

    // importParas never have names
    protected List<AlloyImportPara> imports = emptyList();

    public AMThisImportParas() {}

    protected AMThisImportParas(AMThisImportParas other) {
        super(other);
        // does not recursively load
        this.imports = new ArrayList<AlloyImportPara>(other.imports);
    }

    /*
    protected void addPara(AlloyImportPara importPara, String nameSpace) {
        this.addSMPara(importPara, nameSpace);
        this.imports.add(importPara);
    }
    */

    public void addPara(AlloyImportPara importPara) {
        this.addSMPara(importPara, THIS_NAMESPACE);
        this.imports.add(importPara);
    }

    private void addSMPara(AlloyImportPara importPara, String nameSpace) {
        /*
            1) add in exactly ordered or non-ordered
            2) substitute arguments for parameters
            3) put in common model within this namespace
            Note: we do not put paras imported into the classes that store 'this's paras (which are used to create a string of the top-level file)
            This will recursively load any imported files from this importPara (these will have a nested namespace)
        */

        // from `open name[A, B] as X` get [A,B]
        // We will check in resolve that these are either already
        // fully qualified or they belong to this namespace
        List<AlloySigRefExpr> valsToSubstitute = importPara.sigRefs;
        // to check in resolve that these are all resolved sigs
        this.createValsSubstitutedFromImport(valsToSubstitute);

        // will be only one modPara in the importedFile
        AlloyModulePara modPara =
                extractItemsOfClass(importPara.importedFile.paras, AlloyModulePara.class).get(0);
        // parameters to substitute for
        List<AlloyModulePara.AlloyModuleArg> modArgs = modPara.moduleArgs;
        if (modArgs.size() != valsToSubstitute.size()) {
            throw AlloyModelError.importArgsNumDoesntMatch(importPara.pos, importPara.toString());
        }

        Map<AlloyQnameExpr, AlloySigRefExpr> substMap = new HashMap<>();
        for (int i = 0; i < modArgs.size(); i++) {
            substMap.put(modArgs.get(i).qname, valsToSubstitute.get(i));
        }

        // 1)
        // set what is "isExactly"
        for (int i = 0; i < modArgs.size(); i++) {
            if (modArgs.get(i).isExactly) {
                if (importPara.qname.equals("util/ordering")) {
                    // ordering module import is handled
                    // specially so can produce error
                    // in AMScopes about applying only
                    // to top-level sigs
                    // Assumption: ordering module has only one param
                    this.createOrderedSigWithExactScope(
                            unknownQname(valsToSubstitute.get(i).getName()));
                } else {
                    this.createNonOrderedSigWithExactScope(
                            unknownQname(valsToSubstitute.get(i).getName()));
                }
            }
        }

        // 2)
        // replace modArg name with value to substitute in all paragraphs
        TestAndReplaceExprParaVis vis =
                new TestAndReplaceExprParaVis(
                        e -> substMap.keySet().contains(e), e -> ((AlloyExpr) substMap.get(e)));
        List<AlloyPara> importParas =
                extractItemsNotOfClass(importPara.importedFile.paras, AlloyCmdPara.class);
        importParas = extractItemsNotOfClass(importParas, AlloyModulePara.class);
        List<AlloyPara> newParas = emptyList();
        for (AlloyPara para : importParas) {
            newParas.add(vis.visit(para));
        }

        String importNameSpace = THIS_NAMESPACE;
        // add all the paragraphs in the namespace
        // open name[A, B] as X -> X
        if (importPara.asQname.isPresent()) importNameSpace = importPara.asQname.get().getName();

        for (AlloyPara alloyPara : newParas) {
            // only added to SM (not AMThis)
            if (alloyPara instanceof AlloyEnumPara p) addSMPara(p, importNameSpace);
            else if (alloyPara instanceof AlloySigPara p) addSMPara(p, importNameSpace);
            else if (alloyPara instanceof AlloyPredPara p) addSMPara(p, importNameSpace);
            else if (alloyPara instanceof AlloyFunPara p) addSMPara(p, importNameSpace);
            else if (alloyPara instanceof AlloyFactPara p) addSMPara(p, importNameSpace);
            else if (alloyPara instanceof AlloyAssertPara p) addSMPara(p, importNameSpace);
            // else if (alloyPara instanceof AlloyCmdPara p) addSMPara(p, importNameSpace);
            else if (alloyPara instanceof AlloyImportPara p)
                // this will cause a recursive call if nested imports
                addSMPara(p, importNameSpace);
            // else if (alloyPara instanceof AlloyModulePara p) addSMPara(p, importNameSpace);
            else {
                System.out.println(alloyPara);
                throw ImplementationError.shouldNotReach();
            }
        }
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
        this.addPara(
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
        this.addPara(
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
        this.addPara(
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
        this.addPara(
                new AlloyImportPara(
                        false,
                        new AlloyQnameExpr(mapBy(names, x -> new AlloyNameExpr(x))),
                        emptyList(),
                        null,
                        parseImport(Pos.UNKNOWN, fileName)));
    }
}
