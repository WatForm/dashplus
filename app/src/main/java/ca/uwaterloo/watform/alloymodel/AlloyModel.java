/*
    AlloyModel has paragraphs from an AlloyFile
    and "additional paragraphs"

    Printing is done by printing the original AlloyFile
    and then adding the additional paragraphs to a
    new AlloyFile and printing it.

    TODO: we probably need to make AlloyModel just print
    its own paragraphs directly with no difference between
    original and additional ones.

*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.utils.PrintContext;
import java.io.StringWriter;
import java.util.*;

public class AlloyModel extends AMModules {

    public AlloyModel() {
        this(new AlloyFile(Collections.emptyList()));
    }

    public AlloyModel copy() {
        return new AlloyModel(this);
    }

    public AlloyModel copyImportsAndSigs() {
        List<AlloyPara> paras = new ArrayList<AlloyPara>();
        paras.addAll(this.allModuleParas());
        paras.addAll(this.allImportParas());
        paras.addAll(this.allSigParas());
        AlloyFile af = new AlloyFile(paras);
        return new AlloyModel(af);
    }

    // we need copies of these two constructors in every parent class
    public AlloyModel(AlloyFile alloyFile) {
        super(alloyFile);
    }

    public AlloyModel(AlloyModel other) {
        super(other);
    }

    public void resolve() {
        super.resolve();
    }

    public List<AlloyPara> getAllParas(boolean withCmds) {
        List<AlloyPara> allParas = new ArrayList<AlloyPara>();

        // first two must come before the rest
        allParas.addAll(this.allModuleParas());
        allParas.addAll(this.allImportParas());

        allParas.addAll(this.allEnumParas());
        allParas.addAll(this.allSigParas());
        allParas.addAll(this.allMacroParas());
        allParas.addAll(this.allFunParas());
        allParas.addAll(this.allPredParas());
        allParas.addAll(this.allFactParas());
        allParas.addAll(this.allAssertParas());
        if (withCmds) allParas.addAll(this.allCmdParas());
        return allParas;
    }

    private AlloyFile toAlloyFile(boolean withCmds) {
        return new AlloyFile(this.getAllParas(withCmds));
    }

    public AlloyFile toAlloyFile() {
        return new AlloyFile(this.getAllParas(true));
    }

    public AlloyFile toAlloyFileNoCmds() {
        return new AlloyFile(this.getAllParas(false));
    }

    private String toString(boolean withCmds) {
        StringWriter sw = new StringWriter();
        PrintContext pCtx = new PrintContext(sw);

        AlloyFile newAlloyFile = this.toAlloyFile(withCmds);
        newAlloyFile.ppNewBlock(pCtx);
        return sw.toString();
    }

    public String toString() {
        return this.toString(true);
    }

    public String toStringNoCmds() {
        return this.toString(false);
    }
}
