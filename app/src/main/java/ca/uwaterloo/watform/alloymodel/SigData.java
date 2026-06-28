package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class SigData {

    public Boolean isTopLevelSig = false;
    public Boolean isAbstractSig = false;
    public Boolean isOneSig = false;
    public Boolean isSomeSig = false;
    public Boolean isLoneSig = false;

    // created on initialization and never modified
    // but can't make final w/o rearranging code in constructor
    public List<String> inParents = emptyList();
    public Optional<String> extendsParent = Optional.empty();

    // this is not used so let's leave it out for now
    // private List<String> fields;

    // populated after multiple sigs loaded
    private List<String> inChildren = emptyList();
    private List<String> extendsChildren = emptyList();

    public SigData() {
        // returns a SigData with all the default values
    }

    public SigData(AlloySigPara p) {
        // this is data for a single sigPara
        if (p.rel.isPresent()) {
            if (p.rel.get() instanceof AlloySigPara.Extends e) {
                // this includes one sigs because they are extensions
                this.extendsParent = Optional.of(e.sigRef.getName());
                this.inParents = emptyList();
            } else if (p.rel.get() instanceof AlloySigPara.In e) {
                this.extendsParent = Optional.empty();
                this.inParents = mapBy(e.sigRefs, s -> s.getName());
                if (p.quals.contains(AlloySigPara.Qual.ABSTRACT)) {
                    throw AlloyModelError.subsetSigsCannotBeAbstrast(p.pos, p.toString());
                }
            } else if (p.rel.get() instanceof AlloySigPara.Equal e) {
                // sig A = C + C {}
                // means
                // sig A in B + C {}
                // fact { A = B + C }
                // it is not extends
                this.extendsParent = Optional.empty();
                this.inParents = mapBy(e.sigRefs, s -> s.getName());
            } else {
                this.isTopLevelSig = true;
            }
        } else {
            this.extendsParent = Optional.empty();
            this.inParents = emptyList();
            this.isTopLevelSig = true;
        }
        if (p.quals.contains(AlloySigPara.Qual.ABSTRACT)) this.isAbstractSig = true;
        if (p.quals.contains(AlloySigPara.Qual.ONE)) this.isOneSig = true;
        if (p.quals.contains(AlloySigPara.Qual.SOME)) this.isSomeSig = true;
        if (p.quals.contains(AlloySigPara.Qual.LONE)) this.isLoneSig = true;
    }

    // this is used when adding parts of an AlloyEnumPara
    public static SigData abstractSigData() {
        SigData sd = new SigData();
        sd.isAbstractSig = true;
        sd.isTopLevelSig = true;
        return sd;
    }

    public static SigData oneSigData(String parent) {
        SigData sd = new SigData();
        sd.isOneSig = true;
        sd.extendsParent = Optional.of(parent);
        return sd;
    }

    // getters

    public List<String> inParents() {
        return this.inParents;
    }

    public Optional<String> extendsParent() {
        return this.extendsParent;
    }

    public List<String> allParents() {
        return concat(
                this.inParents(), this.extendsParent().map(p -> List.of(p)).orElse(emptyList()));
    }

    public List<String> inChildren() {
        return this.inChildren;
    }

    public List<String> extendsChildren() {
        return this.extendsChildren;
    }

    public List<String> allChildren() {
        return concat(this.inChildren(), this.extendsChildren());
    }

    // setters; note there is no ability to add a parent!

    public void addInChild(String sigName) {
        this.inChildren.add(sigName);
    }

    public void addExtendsChild(String sigName) {
        this.extendsChildren.add(sigName);
    }

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
                + "\n";
    }
}
