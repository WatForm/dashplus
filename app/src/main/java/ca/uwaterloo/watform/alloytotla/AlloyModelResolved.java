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
        List<String> children;
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
        populateNames();
        populateExtendsParent();
        populateInParents();
    }

    private void populateNames() {
        this.getParas(AlloySigPara.class)
                .forEach(
                        sp -> {
                            String name = sp.qnames.get(0).toString();
                            SignatureRecord r = new SignatureRecord(sp);
                            this.sigTable.put(name, r);
                        });
    }

    private void populateExtendsParent() {
        this.sigTable
                .keySet()
                .forEach(
                        sn -> {
                            AtomicReference<String> answer = new AtomicReference<>(null);
                            sigTable.get(sn)
                                    .para
                                    .rel
                                    .ifPresent(
                                            x -> {
                                                if (x instanceof AlloySigPara.Extends)
                                                    answer.set(
                                                            ((AlloySigPara.Extends) x)
                                                                    .sigRef.toString());
                                            });
                            sigTable.get(sn).extendsParent = Optional.ofNullable(answer.get());
                        });
    }

    private void populateInParents() {

        this.sigTable
                .keySet()
                .forEach(
                        sn -> {
                            sigTable.get(sn)
                                    .para
                                    .rel
                                    .ifPresent(
                                            x -> {
                                                if (x instanceof AlloySigPara.In)
                                                    sigTable.get(sn).inParents =
                                                            mapBy(
                                                                    ((AlloySigPara.In) x).sigRefs,
                                                                    s -> s.toString());
                                                else sigTable.get(sn).inParents = new ArrayList<>();
                                            });
                        });
    }

    public List<String> getAllSigNames() {
        return new ArrayList<>(this.sigTable.keySet());
    }

    public List<String> getTopLevelSigNames() {
        return filterBy(getAllSigNames(), s -> this.isTopLevelSig(s));
    }

    public List<String> getDescendedSigNames() {
        return filterBy(getAllSigNames(), s -> !this.isTopLevelSig(s));
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
