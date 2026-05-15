package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara.Qual;
import ca.uwaterloo.watform.alloytotla.QnameExtractVis;
import java.util.*;

public class AlloyModelResolve extends AlloyModelInitialize {

    public List<String> allSigs() {
        return new ArrayList<>(this.sigTable.keySet());
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

    public List<String> topLevelSigs() {
        return filterBy(allSigs(), s -> this.isTopLevelSig(s));
    }

    public List<String> nonTopLevelSigNames() {
        return filterBy(allSigs(), s -> !this.isTopLevelSig(s));
    }

    public List<String> topoSortedSigs() {
        List<String> answer = this.topLevelSigs();

        /*
        algorithm:
        answer <- set of all top-level sigs
        in each step:
            for all sigs S:
                if all parents of S are in answer and S is not
                then S is added to the answer
        once no changes in answer's size is detected, the steps stop

        finally, the following property holds:
        for all sigs S: all of its parents lie before it in the list
        */

        int oldSize;
        do {
            oldSize = answer.size();
            allSigs()
                    .forEach(
                            sn -> {
                                if (answer.containsAll(allParentsOfSig(sn)))
                                    if (!answer.contains(sn)) answer.add(sn);
                            });
        } while (oldSize != answer.size());

        return answer;
    }

    public List<String> inParentsOfSig(String signame) {
        return new ArrayList<>(this.sigTable.get(signame).inParents);
    }

    public Optional<String> extendsParentOfSig(String signame) {
        return this.sigTable.get(signame).extendsParent;
    }

    public List<String> allParentsOfSig(String signame) {
        List<String> answer = inParentsOfSig(signame);
        extendsParentOfSig(signame).ifPresent(sp -> answer.add(sp));
        return answer;
    }

    public List<String> inChildrenOfSig(String signame) {
        return new ArrayList<>(this.sigTable.get(signame).inChildren);
    }

    public List<String> extendsChildrenOfSig(String signame) {
        return new ArrayList<>(this.sigTable.get(signame).extendsChildren);
    }

    public Optional<AlloyBlock> alloyBlockOfSig(String signame) {
        return sigTable.get(signame).para.block;
    }

    public List<String> allChildrenOfSig(String signame) {
        List<String> answer = inChildrenOfSig(signame);
        answer.addAll(extendsChildrenOfSig(signame));
        return answer;
    }

    public List<String> fieldNames(String signame) {

        // Logger l = CustomLoggerFactory.make("AlloyToTla", true);
        // l.info(signame + ": fields are " + sigTable.get(signame).fields);
        return new ArrayList<>(sigTable.get(signame).fields);
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
        // null when not resolved
        Set<String> inParents = null;
        Optional<String> extendsParent = null;
        Set<String> inChildren = null;
        Set<String> extendsChildren = null;
        Set<String> descs = null;
        Set<String> ances = null;
        Set<String> fields = new HashSet<>();

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
        String sigParent;
        AlloyExpr expr;
        List<String> parentFields;

        FieldData(String sigParent, AlloyExpr expr) {
            this.sigParent = sigParent;
            this.expr = expr;
            this.parentFields = new QnameExtractVis().visit(expr);
        }

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
        resolveFieldsInSigs();
    }

    private void resolveFieldsInSigs() {
        this.fieldTable
                .keySet()
                .forEach(
                        f -> {
                            var data = fieldTable.get(f);
                            sigTable.get(data.sigParent).fields.add(f);
                        });
    }

    private void resolveFields() {
        this.getParas(AlloySigPara.class)
                .forEach(
                        sp -> {
                            sp.fields.forEach(
                                    f -> {
                                        String sigParent = sp.qnames.get(0).toString();
                                        fieldTable.put(
                                                f.qnames.get(0).toString(),
                                                new FieldData(sigParent, f.expr));
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

    private Set<String> AncestorsHelper(String signame) {
        if (sigTable.get(signame).ances != null) return sigTable.get(signame).ances; // base case

        Set<String> answer = new HashSet<>();
        allParentsOfSig(signame)
                .forEach(
                        sc -> {
                            answer.add(sc);
                            answer.addAll(AncestorsHelper(sc)); // recursion
                        });

        sigTable.get(signame).ances = answer; // memoization
        return answer;
    }

    private Set<String> DescendantsHelper(String signame) {
        if (sigTable.get(signame).descs != null) return sigTable.get(signame).descs; // base case

        Set<String> answer = new HashSet<>();
        allChildrenOfSig(signame)
                .forEach(
                        sp -> {
                            answer.add(sp);
                            answer.addAll(DescendantsHelper(sp)); // recursion
                        });

        sigTable.get(signame).descs = answer; // memoization
        return answer;
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

    private Set<String> helperInParents(String sig) {
        var empty = new HashSet<String>();
        return sigTable.get(sig)
                .para
                .rel
                .map(
                        r -> {
                            if (r instanceof AlloySigPara.In) {
                                var answer = new HashSet<String>();
                                answer.addAll(
                                        mapBy(((AlloySigPara.In) r).sigRefs, sr -> sr.toString()));
                                return answer;
                            }
                            return empty;
                        })
                .orElse(empty);
    }

    private Optional<String> helperExtendsParent(String sig) {
        return Optional.ofNullable(
                sigTable.get(sig)
                        .para
                        .rel
                        .map(
                                r -> {
                                    if (r instanceof AlloySigPara.Extends)
                                        return ((AlloySigPara.Extends) r).sigRef.toString();
                                    return null;
                                })
                        .orElse(null));
    }

    private void populateParentsChildren() {

        for (String signame : this.sigTable.keySet()) {
            var entry = this.sigTable.get(signame);
            entry.extendsParent = helperExtendsParent(signame);
            entry.inParents = helperInParents(signame);
            entry.inChildren = new HashSet<>();
            entry.extendsChildren = new HashSet<>();
        }

        for (String signame : this.sigTable.keySet()) {
            this.sigTable
                    .get(signame)
                    .extendsParent
                    .ifPresent(
                            parent -> {
                                this.sigTable.get(parent).extendsChildren.add(signame);
                            });

            for (var inParent : this.sigTable.get(signame).inParents) {
                this.sigTable.get(inParent).inChildren.add(signame);
            }
        }
    }
}
