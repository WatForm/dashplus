package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara.Qual;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import java.util.*;

public class AlloyModelResolved extends AlloyModel {

    public AlloyModelResolved(AlloyFile alloyFile) {
        super(alloyFile);
        resolve();
    }

    private static record SignatureRecord(
            AlloySigPara para,
            List<String> inParents,
            Optional<String> extendsParent,
            List<String> children,
            List<String> topLevelParents,
            List<String> descs,
            List<String> ances) {}

    private static record FieldRecord(String sigParent, AlloyDecl decl) {}

    private HashMap<String, SignatureRecord> sigTable;
    private HashMap<String, FieldRecord> fieldTable;

    private void resolve() {
        populateNames();
    }

    private void populateNames() {
        this.getParas(AlloySigPara.class)
                .forEach(
                        sp -> {
                            String name = sp.qnames.get(0).toString();
                            SignatureRecord r =
                                    new SignatureRecord(sp, null, null, null, null, null, null);
                            this.sigTable.put(name, r);
                        });
    }

    private void populateInParents() {}

    public List<String> getAllSigNames(String signame) {
        return new ArrayList<>(this.sigTable.keySet());
    }

    public List<String> getTopLevelSigNames(String signame) {
        return filterBy(getAllSigNames(signame), s -> this.isTopLevelSig(s));
    }

    public List<String> getDescendedSigNames(String signame) {
        return filterBy(getAllSigNames(signame), s -> !this.isTopLevelSig(s));
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
