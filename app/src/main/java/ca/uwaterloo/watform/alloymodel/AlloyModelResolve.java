package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara.Qual;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class AlloyModelResolve extends AlloyModelInitialize {

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

    public Optional<AlloyBlock> getAlloyBlockOfSig(String signame) {
        return sigTable.get(signame).para.block;
    }

    public List<String> getAllChildren(String signame) {
        List<String> answer = getInChildren(signame);
        answer.addAll(getExtendsChildren(signame));
        return answer;
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

    public List<String> getFieldNames(String signame) {
        return new ArrayList<>();
    }

    public AlloyModelResolve() {
        super(new AlloyFile(Collections.emptyList()));
        resolve();
    }

    protected AlloyModelResolve(AlloyModelResolve other) {
        super(other);
        resolve();
    }

    public AlloyModelResolve copy() {
        return new AlloyModelResolve(this);
    }

    public AlloyModelResolve(AlloyFile alloyFile) {

        super(alloyFile);
        resolve();
    }

    private static class SignatureData {
        AlloySigPara para;
        List<String> inParents = null;
        Optional<String> extendsParent = null;
        List<String> inChildren = null;
        List<String> extendsChildren = null;
        List<String> descs = null; // null when not resolved
        List<String> ances = null;

        @Override
        public String toString() {
            return "\ninParents: "
                    + inParents.toString()
                    + "\nextendsParent: "
                    + extendsParent.toString()
                    + "\ninChildren: "
                    + inChildren.toString()
                    + "\nextendsChildren: "
                    + extendsChildren.toString()
                    + "\ndescendants: "
                    + descs.toString()
                    + "\nancestors: "
                    + ances.toString()
                    + "\n";
        }

        SignatureData(AlloySigPara p) {
            this.para = p;
        }
    }

    private static class FieldData {
        String sigParent = null;
        AlloyExpr expr = null;

        @Override
        public String toString() {
            return "\nsigParent: " + this.sigParent + "\ndecl: " + expr.toString() + "\n";
        }
    }

    private HashMap<String, SignatureData> sigTable;
    private HashMap<String, FieldData> fieldTable;

    private void resolve() {
        this.sigTable = new HashMap<>();
        this.fieldTable = new HashMap<>();

        populateNames(); // first pass, to get the sig names
        populateParentsChildren(); // second pass, to populate the parents and children
        populateAncestorsDescendants(); // recursive pass, transitively fill table, with memoization
        resolveFields();
    }

    private void resolveFields() {
        this.getParas(AlloySigPara.class)
                .forEach(
                        sp -> {
                            sp.fields.forEach(
                                    f -> {
                                        FieldData fd = new FieldData();
                                        fd.expr = f.expr;
                                        fd.sigParent = sp.qnames.get(0).toString();
                                        fieldTable.put(f.toString(), fd);
                                    });
                        });
    }

    private void populateNames() {
        this.getParas(AlloySigPara.class)
                .forEach(
                        sp -> {
                            String name = sp.qnames.get(0).toString();
                            SignatureData r = new SignatureData(sp);
                            this.sigTable.put(name, r);
                        });
    }

    private void populateAncestorsDescendants() {

        this.sigTable
                .keySet()
                .forEach(
                        sig -> {
                            AncestorsHelper(sig);
                            DescendantsHelper(sig);
                        });
    }

    private List<String> AncestorsHelper(String signame) {
        if (sigTable.get(signame).ances != null) return sigTable.get(signame).ances; // base case

        List<String> answer = new ArrayList<>();
        getAllParents(signame)
                .forEach(
                        sc -> {
                            answer.add(sc);
                            answer.addAll(AncestorsHelper(sc)); // recursion
                        });

        sigTable.get(signame).ances = answer; // memoization
        return answer;
    }

    private List<String> DescendantsHelper(String signame) {
        if (sigTable.get(signame).descs != null) return sigTable.get(signame).descs; // base case

        List<String> answer = new ArrayList<>();
        getAllChildren(signame)
                .forEach(
                        sp -> {
                            answer.add(sp);
                            answer.addAll(DescendantsHelper(sp)); // recursion
                        });

        sigTable.get(signame).descs = answer; // memoization
        return answer;
    }

    private void populateParentsChildren() {
        this.sigTable
                .keySet()
                .forEach(
                        sn -> {
                            SignatureData entry = this.sigTable.get(sn);
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

    private void populateExtendsParentsChildren(String sig) {

        AtomicReference<String> answer = new AtomicReference<>(null);
        sigTable.get(sig)
                .para
                .rel
                .ifPresent(
                        x -> {
                            if (x instanceof AlloySigPara.Extends) {
                                String sigParent = ((AlloySigPara.Extends) x).sigRef.toString();
                                answer.set(sigParent);
                                sigTable.get(sigParent).extendsChildren.add(sig);
                            }
                        });

        sigTable.get(sig).extendsParent = Optional.ofNullable(answer.get());
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
}
