/*
	Just info about sigs (not paras because some sigs are dervied from enum paras)
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyEnumPara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.utils.*;
import java.util.*;
import java.util.function.BiFunction;

public class AMSigTable extends AMFieldTable {

    private LinkedHashMap<String, SigData> sigTable = new LinkedHashMap<>();

    protected AMSigTable(AlloyFile alloyFile) {
        // no sigTables in an AlloyFile
        super(alloyFile);
    }

    protected AMSigTable(AMSigTable other) {
        super(other);
        // we assume this has all arities and default mul set
        this.sigTable = new LinkedHashMap<>(other.sigTable);
    }

    protected void resolve(
            BiFunction<AlloyExpr, Optional<String>, CalcAritySetMulDefaultsExprVis.Result>
                    fieldArityAndSetMul) {
        super.resolve(fieldArityAndSetMul);
        // done after all sigs and enums are added
        DetectCycles.topoOrderCycleDetector(this.allSigs(), x -> this.allChildren(x));
    }

    protected void addToSigTable(AlloySigPara multiSigPara) {
        for (AlloySigPara sigPara : multiSigPara.expand()) {
            String sigName = sigPara.getName();
            this.entry(sigPara.pos, sigName, new SigData(sigPara));
            this.addToFieldTable(sigPara.fields, sigName);
        }
    }

    protected void addToSigTable(AlloyEnumPara enumPara) {
        // enum Color { Red, Green, Blue }
        // means
        // abstract sig Color {}
        // one sig Red extends Color {}
        // one sig Green extends Color {}
        // one sig Blue extends Color {}
        // no fields
        // no facts
        String parent = enumPara.qname.getName();
        this.entry(enumPara.pos, parent, SigData.abstractSigData());
        for (AlloyQnameExpr enumValue : enumPara.qnames) {
            this.entry(enumPara.pos, enumValue.getName(), SigData.oneSigData(parent));
        }
    }

    private void entry(Pos p, String name, SigData sd) {
        // System.out.println("Adding sig: " + name);
        if (!this.allSigs().contains(name)) this.sigTable.put(name, sd);
        else throw AlloyModelError.duplicateSigName(p, name);
    }

    public List<String> allSigs() {
        return setToList(this.sigTable.keySet());
    }

    public List<String> topoSortedSigs() {
        Queue<String> queue = new ArrayDeque<>();
        List<String> returnList = new ArrayList<String>();
        queue.addAll(this.topLevelSigs());
        while (!queue.isEmpty()) {
            String first = queue.poll();
            if (!returnList.contains(first)) {
                returnList.add(first);
                queue.addAll(this.allChildren(first));
            }
        }
        return returnList;
    }

    private void setChildren(List<String> sigNames) {
        // set child attributes for sigs in sigNames
        // when adding a few sigs, they might add children
        // to a previously existing sig or sigs in sigNames
        // but nothing in previously existing sigs can have
        // children in this list of sigNames
        for (String sigName : sigNames) {
            if (!this.containsSig(sigName))
                throw AlloyModelImplError.tryingToAccessNonExistentSig(sigName);
            else {
                for (String inParent : sigTable.get(sigName).inParents()) {
                    sigTable.get(inParent).addInChild(sigName);
                }
                if (sigTable.get(sigName).extendsParent().isPresent()) {
                    sigTable.get(sigTable.get(sigName).extendsParent().get())
                            .addExtendsChild(sigName);
                }
            }
        }
    }

    public boolean containsSig(String sigName) {
        return this.allSigs().contains(sigName);
    }

    // general getters

    public List<String> topLevelSigs() {
        return filterBy(this.allSigs(), s -> this.isTopLevelSig(s));
    }

    public List<String> nonTopLevelSigNames() {
        return filterBy(this.allSigs(), s -> !this.isTopLevelSig(s));
    }

    // individual getters

    public List<String> inParents(String sigName) {
        if (!this.containsSig(sigName))
            throw AlloyModelImplError.tryingToAccessNonExistentSig(sigName);
        else return this.sigTable.get(sigName).inParents();
    }

    public List<String> inChildren(String sigName) {
        if (!this.containsSig(sigName))
            throw AlloyModelImplError.tryingToAccessNonExistentSig(sigName);
        else return this.sigTable.get(sigName).inChildren();
    }

    public Optional<String> extendsParent(String sigName) {
        if (!this.containsSig(sigName))
            throw AlloyModelImplError.tryingToAccessNonExistentSig(sigName);
        else return this.sigTable.get(sigName).extendsParent();
    }

    public List<String> extendsChildren(String sigName) {
        if (!this.containsSig(sigName))
            throw AlloyModelImplError.tryingToAccessNonExistentSig(sigName);
        else return this.sigTable.get(sigName).extendsChildren();
    }

    public List<String> allChildren(String sigName) {
        if (!this.containsSig(sigName))
            throw AlloyModelImplError.tryingToAccessNonExistentSig(sigName);
        else return this.sigTable.get(sigName).allChildren();
    }

    public List<String> allParents(String sigName) {
        if (!this.containsSig(sigName))
            throw AlloyModelImplError.tryingToAccessNonExistentSig(sigName);
        else return this.sigTable.get(sigName).allParents();
    }

    // individual testers

    public boolean isTopLevelSig(String sigName) {
        if (!this.containsSig(sigName))
            throw AlloyModelImplError.tryingToAccessNonExistentSig(sigName);
        else return this.sigTable.get(sigName).isTopLevelSig;
    }

    public boolean isAbstractSig(String sigName) {
        if (!this.containsSig(sigName))
            throw AlloyModelImplError.tryingToAccessNonExistentSig(sigName);
        else return this.sigTable.get(sigName).isAbstractSig;
    }

    public boolean isOneSig(String sigName) {
        if (!this.containsSig(sigName))
            throw AlloyModelImplError.tryingToAccessNonExistentSig(sigName);
        else return this.sigTable.get(sigName).isOneSig;
    }

    public boolean isSomeSig(String sigName) {
        if (!this.containsSig(sigName))
            throw AlloyModelImplError.tryingToAccessNonExistentSig(sigName);
        else return this.sigTable.get(sigName).isSomeSig;
    }

    public boolean isLoneSig(String sigName) {
        if (!this.containsSig(sigName))
            throw AlloyModelImplError.tryingToAccessNonExistentSig(sigName);
        else return this.sigTable.get(sigName).isLoneSig;
    }
}
