package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class SigData {

    protected Boolean isTopLevelSig = false;
    protected Boolean isAbstractSig = false;
    protected Boolean isOneSig = false;
    protected Boolean isSomeSig = false;
    protected Boolean isLoneSig = false;
    protected Pos pos = Pos.UNKNOWN;

    // created on initialization and never modified
    // but can't make final w/o rearranging code in constructor
    protected List<Qname> inParents = emptyList();
    protected Optional<Qname> extendsParent = Optional.empty();

    // this is not used so let's leave it out for now
    // private List<String> fields;

    // populated after multiple sigs loaded
    protected List<Qname> inChildren = emptyList();
    protected List<Qname> extendsChildren = emptyList();

    protected SigData() {
        // returns a SigData with all the default values
    }

    // this is used when adding parts of an AlloyEnumPara
    protected static SigData abstractSigData(Pos pos) {
        SigData sd = new SigData();
        sd.pos = pos;
        sd.isAbstractSig = true;
        sd.isTopLevelSig = true;
        return sd;
    }

    protected static SigData oneSigData(Pos pos, Qname parent) {
        SigData sd = new SigData();
        sd.pos = pos;
        sd.isOneSig = true;
        sd.extendsParent = Optional.of(parent);
        return sd;
    }

    // getters

    /*
    protected Pos pos() {
        return this.pos;
    }

    public List<Qname> inParents() {
        return this.inParents;
    }

    public Optional<Qname> extendsParent() {
        return this.extendsParent;
    }

    public List<Qname> allParents() {
        return concat(
                this.inParents(), this.extendsParent().map(p -> List.of(p)).orElse(emptyList()));
    }

    public List<Qname> inChildren() {
        return this.inChildren;
    }

    public List<Qname> extendsChildren() {
        return this.extendsChildren;
    }

    public List<String> allChildren() {
        return concat(this.inChildren(), this.extendsChildren());
    }
    */
    // setters; note there is no ability to add a parent!

    /*
    public void addInChild(Qname qname) {
        this.inChildren.add(qname);
    }

    public void addExtendsChild(Qname qame) {
        this.extendsChildren.add(qname);
    }
    */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (isTopLevelSig) sb.append("topLevel=true, ");
        if (isAbstractSig) sb.append("abstract=true, ");
        if (isOneSig) sb.append("one=true, ");
        if (isSomeSig) sb.append("some=true, ");
        if (isLoneSig) sb.append("lone=true, ");
        // if (!Pos.UNKNOWN.equals(pos)) sb.append("pos=").append(pos).append(", ");
        if (!inParents.isEmpty()) sb.append("inParents=").append(inParents).append(", ");
        if (extendsParent.isPresent())
            sb.append("extendsParent=").append(extendsParent).append(", ");
        if (!inChildren.isEmpty()) sb.append("inChildren=").append(inChildren).append(", ");
        if (!extendsChildren.isEmpty())
            sb.append("extendsChildren=").append(extendsChildren).append(", ");

        if (sb.length() > "SigData{".length())
            sb.setLength(sb.length() - 2); // remove trailing ", "

        return sb.toString();
    }
}
