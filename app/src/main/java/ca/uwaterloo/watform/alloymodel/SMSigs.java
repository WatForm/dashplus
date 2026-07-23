/*
	Just info about sigs (including enums)

    table is indexed by namespace + sig Name

    creation always includes nameSpace

    nameSpace + sig is unique

    but can lookup by combo or by sigName alone, which is where overloading can occur
    b/c the same sigName may be in multiple namespaces

*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.nullField;

import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloySigRefExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class SMSigs {

    // Qname is unique for sigs
    public HashMap<Qname, SigData> sigTable = new HashMap<>();
    // this is here just for error checking
    public List<AlloySigRefExpr> valsSubstitutedFromImports = emptyList();

    // init --------------

    protected SMSigs() {}

    protected SMSigs(SMSigs other) {
        this.sigTable = new HashMap<>(other.sigTable);
    }

    // no other function should do a 'put' into the sigTable
    protected void createSig(Pos p, Qname qname, SigData sd) {
        // System.out.println("Adding sig: " + name);
        reqNonNull(nullField(p, this), qname, sd);
        assert (qname.nameSpace != UNKNOWN_NAMESPACE);
        // nameSpace + sig is unique
        if (!this.sigTable.containsKey(qname)) this.sigTable.put(qname, sd);
        else throw AlloyModelError.duplicateSigName(p, qname.fullName());
    }

    protected void createValsSubstitutedFromImport(List<AlloySigRefExpr> ll) {
        valsSubstitutedFromImports.addAll(ll);
    }

    // resolve

    protected void resolveSMSigs() {

        // check values substituted on imports are okay
        // because o/w error will show up a weird place
        for (AlloySigRefExpr sigRefExpr : valsSubstitutedFromImports) {
            if (!(sigRefExpr instanceof AlloyQnameExpr)) {
                throw AlloyModelError.argToImportMustBeSigs(
                        sigRefExpr.getPos(), sigRefExpr.toString());
            } else if (!this.isSig(thisQname(sigRefExpr.getName()))) {
                throw AlloyModelError.argToImportMustBeUnique(
                        sigRefExpr.getPos(), sigRefExpr.toString());
            }
        }

        // now that all sigs are in the sigTable
        // get the child links set up
        this.setChildren();

        // done after all sigs and enums are added
        DetectCycles.topoOrderCycleDetector(this.allSigQnames(), x -> this.allChildren(x));

        // error detection
        for (Qname sigQname : this.allSigQnames()) {
            // if parent is an "in" child can't be an extends
            if (!this.inParents(sigQname).isEmpty()) {
                // it is an in child
                if (!this.extendsChildren(sigQname).isEmpty()) {
                    // one of the children (could be multiple)
                    Qname childQname = this.extendsChildren(sigQname).get(0);
                    throw AlloyModelError.cantExtendSubsetSig(
                            this.pos(childQname), childQname.toString());
                }
            }
        }
    }

    private void setChildren() {
        // set child attributes for sigs in sigNames
        // when adding a few sigs, they might add children
        // to a previously existing sig or sigs in sigNames
        // but nothing in previously existing sigs can have
        // children in this list of sigNames
        for (Qname sigQname : this.allSigQnames()) {
            for (Qname inParent : sigTable.get(sigQname).inParents) {
                sigTable.get(inParent).inChildren.add(sigQname);
            }
            if (this.sigTable.get(sigQname).extendsParent.isPresent()) {
                this.sigTable
                        .get(sigTable.get(sigQname).extendsParent.get())
                        .extendsChildren
                        .add(sigQname);
            }
        }
    }

    public List<Qname> topoSortedSigs() {
        Queue<Qname> queue = new ArrayDeque<>();
        List<Qname> returnList = new ArrayList<>();
        queue.addAll(this.topLevelSigs());
        while (!queue.isEmpty()) {
            Qname first = queue.poll();
            if (!returnList.contains(first)) {
                returnList.add(first);
                queue.addAll(this.allChildren(first));
            }
        }
        return returnList;
    }

    // lookup ------------------

    public List<Qname> sigQnameMatches(Qname qname) {
        // either matches exactly (which would mean only one match)
        // or could match on multiple of UNKNOWN_NAMESPACE
        return sigTable.keySet().stream()
                .filter(
                        q ->
                                q.name.equals(qname.name)
                                        & (q.nameSpace.equals(qname.nameSpace)
                                                || qname.nameSpace.equals(UNKNOWN_NAMESPACE)))
                .toList();
    }

    public boolean isSig(Qname qname) {
        // System.out.println(this.sigQnameMatches(qname));
        return !this.sigQnameMatches(qname).isEmpty();
    }

    /*
    public boolean hasExactMatch(Qname qname) {
        return this.sigQnameMatches(qname).size() == 1;
    }
    */

    // arity is always ONE for sigs so no need for a special function?

    // gives their full names
    public List<String> allSigs() {
        return mapBy(setToList(this.sigTable.keySet()), x -> x.fullName());
    }

    // gives their full names
    private List<Qname> allSigQnames() {
        return setToList(this.sigTable.keySet());
    }

    // general getters

    public List<Qname> topLevelSigs() {
        return filterBy(this.allSigQnames(), s -> this.isTopLevelSig(s));
    }

    public List<Qname> nonTopLevelSigNames() {
        return filterBy(this.allSigQnames(), s -> !this.isTopLevelSig(s));
    }

    // individual getters and testers
    // they expect nameSpace/sigName in the string
    // but if only sigName is called, they will look for "this/sigName"

    private void sigExists(Qname sigName) {
        if (!this.sigTable.containsKey(sigName))
            throw AlloyModelImplError.tryingToAccessNonExistentSig(sigName.toString());
    }

    public Pos pos(Qname sigName) {
        sigExists(sigName);
        return this.sigTable.get(sigName).pos;
    }

    public List<Qname> inParents(Qname sigName) {
        sigExists(sigName);
        return this.sigTable.get(sigName).inParents;
    }

    public List<Qname> inChildren(Qname sigName) {
        sigExists(sigName);
        return this.sigTable.get(sigName).inChildren;
    }

    public boolean isInChild(Qname sigName) {
        sigExists(sigName);
        return !(this.sigTable.get(sigName).inParents.isEmpty());
    }

    public boolean isExtendsChild(Qname sigName) {
        sigExists(sigName);
        return this.sigTable.get(sigName).extendsParent.isPresent();
    }

    public Optional<Qname> extendsParent(Qname sigName) {
        sigExists(sigName);
        return this.sigTable.get(sigName).extendsParent;
    }

    public Qname topLevelExtendsAncestor(Qname sigName) {
        sigExists(sigName);
        Optional<Qname> parent = this.sigTable.get(sigName).extendsParent;
        if (parent.isPresent()) return topLevelExtendsAncestor(parent.get());
        else return sigName;
    }

    public List<Qname> extendsChildren(Qname sigName) {
        sigExists(sigName);
        return this.sigTable.get(sigName).extendsChildren;
    }

    public List<Qname> allChildren(Qname sigName) {
        sigExists(sigName);
        return concat(
                this.sigTable.get(sigName).inChildren, this.sigTable.get(sigName).extendsChildren);
    }

    public List<Qname> allParents(Qname sigName) {
        sigExists(sigName);
        SigData sd = this.sigTable.get(sigName);
        return concat(sd.inParents, sd.extendsParent.map(p -> List.of(p)).orElse(emptyList()));
    }

    public boolean isTopLevelSig(Qname sigName) {
        sigExists(sigName);
        return this.sigTable.get(sigName).isTopLevelSig;
    }

    public boolean isAbstractSig(Qname sigName) {
        sigExists(sigName);
        return this.sigTable.get(sigName).isAbstractSig;
    }

    public boolean isOneSig(Qname sigName) {
        sigExists(sigName);
        return this.sigTable.get(sigName).isOneSig;
    }

    public boolean isSomeSig(Qname sigName) {
        sigExists(sigName);
        return this.sigTable.get(sigName).isSomeSig;
    }

    public boolean isLoneSig(Qname sigName) {
        sigExists(sigName);
        return this.sigTable.get(sigName).isLoneSig;
    }

    public void debugSMSigs() {
        // written by ChatGPT
        StringBuilder sb = new StringBuilder("SMSigs:\n");

        for (Map.Entry<Qname, SigData> entry : sigTable.entrySet()) {
            sb.append("  ")
                    .append(entry.getKey())
                    .append(" -> ")
                    .append(entry.getValue())
                    .append('\n');
        }

        sb.append("valsSubstitutedFromImports=").append(valsSubstitutedFromImports);
        System.out.println(sb.toString() + "\n");
    }
}
