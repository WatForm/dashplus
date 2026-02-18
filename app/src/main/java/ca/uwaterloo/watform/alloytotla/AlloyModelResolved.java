package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara.Qual;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class AlloyModelResolved extends AlloyModel {

    public AlloyModelResolved(AlloyFile alloyFile) {
        super(alloyFile);
        resolve();
    }

    private static class SignatureRecord {
        AlloySigPara para;
        List<String> inParents;
        Optional<String> extendsParent;
        List<String> inChildren;
        List<String> extendsChildren;
        List<String> topLevelParents;
        List<String> descs;
        List<String> ances;

        SignatureRecord(AlloySigPara p) {
            this.para = p;
        }
    }

    private static class FieldRecord {
        String sigParent;
        AlloyDecl decl;
    }

    private HashMap<String, SignatureRecord> sigTable;
    private HashMap<String, FieldRecord> fieldTable;

    private void resolve() {
        populateNames(); // first pass, to get the sig names
        populateParentsChildren(); // second pass, to populate the parents and children
        populateAncestorsDescendants(); // third pass, transitively fill table with memoization
    }

    private void populateAncestorsDescendants() {}

    private void populateNames() {
        this.getParas(AlloySigPara.class)
                .forEach(
                        sp -> {
                            String name = sp.qnames.get(0).toString();
                            SignatureRecord r = new SignatureRecord(sp);
                            this.sigTable.put(name, r);
                        });
    }

    private void populateParentsChildren() {
        this.sigTable
                .keySet()
                .forEach(
                        sn -> {
                            SignatureRecord entry = this.sigTable.get(sn);
                            entry.inParents = new ArrayList<>();
                            entry.inChildren = new ArrayList<>();
                            entry.extendsChildren = new ArrayList<>();
                        });
        this.sigTable
                .keySet()
                .forEach(
                        sn -> {
                            populateInParentsChildren(sn);
                            populateExtendsParentsChildren(sn);
                        });
    }

    private void populateExtendsParentsChildren(String sn) {

        AtomicReference<String> answer = new AtomicReference<>(null);
        sigTable.get(sn)
                .para
                .rel
                .ifPresent(
                        x -> {
                            if (x instanceof AlloySigPara.Extends)
                                answer.set(((AlloySigPara.Extends) x).sigRef.toString());
                        });

        sigTable.get(sn).extendsParent = Optional.ofNullable(answer.get());
    }

    private void populateInParentsChildren(String sig) {

        sigTable.get(sig)
                .para
                .rel
                .ifPresent(
                        x -> {
                            if (x instanceof AlloySigPara.In)
                                ((AlloySigPara.In) x)
                                        .sigRefs.forEach(
                                                y -> {
                                                    // sn in sc1 + sc2 + sc3...
                                                    String sigParent = y.toString();
                                                    sigTable.get(sig).inParents.add(sigParent);
                                                    sigTable.get(sigParent).inChildren.add(sig);
                                                });
                        });
    }

    public List<String> getAllSigNames() {
        return new ArrayList<>(this.sigTable.keySet());
    }

    public List<String> getTopLevelSigNames() {
        return filterBy(getAllSigNames(), s -> this.isTopLevelSig(s));
    }

    public List<String> getNonTopLevelSigNames() {
        return filterBy(getAllSigNames(), s -> !this.isTopLevelSig(s));
    }

    public List<String> getInParents(String signame) {
        return this.sigTable.get(signame).inParents;
    }

    public Optional<String> getExtendsParent(String signame) {
        return this.sigTable.get(signame).extendsParent;
    }

    public List<String> getAllParents(String signame) {
        List<String> answer = getInParents(signame);
        getExtendsParent(signame).ifPresent(sp -> answer.add(sp));
        return answer;
    }

    public List<String> getInChildren(String signame) {
        return this.sigTable.get(signame).inChildren;
    }

    public List<String> getExtendsChildren(String signame) {
        return this.sigTable.get(signame).extendsChildren;
    }

    public boolean isTopLevelSig(String signame) {
        return this.sigTable.get(signame).para.isTopLevel();
    }

    public boolean isAbstractSig(String signame) {
        return this.sigTable.get(signame).para.quals.contains(Qual.ABSTRACT);
    }

    public boolean isOneSig(String signame) {
        return this.sigTable.get(signame).para.quals.contains(Qual.ONE);
    }

    public boolean isSomeSig(String signame) {
        return this.sigTable.get(signame).para.quals.contains(Qual.SOME);
    }

    public boolean isLoneSig(String signame) {
        return this.sigTable.get(signame).para.quals.contains(Qual.LONE);
    }
}
