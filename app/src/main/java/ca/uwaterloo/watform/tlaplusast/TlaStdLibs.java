package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TlaStdLibs extends TlaExp { // enums used for extensibility

    public static enum LIBRARIES {
        STL_FiniteSets,
        STL_Naturals,
        STL_Integers,
        STL_Sequences
    };

    private LIBRARIES library;

    public TlaStdLibs(LIBRARIES library) {
        this.library = library;
    }

    public List<TlaExp> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public String toTLAPlusSnippetCore() {

        if (this.library == LIBRARIES.STL_FiniteSets) return TlaStrings.FINITE_SETS;
        else if (this.library == LIBRARIES.STL_Naturals) return TlaStrings.NATURALS;
        else if (this.library == LIBRARIES.STL_Integers) return TlaStrings.INTEGERS;
        else if (this.library == LIBRARIES.STL_Sequences) return TlaStrings.SEQUENCES;

        return "Unknown"; // implementation error TODO
    }

    public static TlaFormulaAppl Cardinality(TlaVar arg) {
        List<TlaExp> children = new ArrayList<>();
        children.add(arg);
        return new TlaFormulaAppl(TlaStrings.CARDINALITY, children);
    }

    public static TlaFormulaAppl Len(TlaVar arg) {
        List<TlaExp> children = new ArrayList<>();
        children.add(arg);
        return new TlaFormulaAppl(TlaStrings.LEN, children);
    }

    public static TlaFormulaAppl Head(TlaVar arg) {
        List<TlaExp> children = new ArrayList<>();
        children.add(arg);
        return new TlaFormulaAppl(TlaStrings.HEAD, children);
    }

    public static TlaFormulaAppl Tail(TlaVar arg) {
        List<TlaExp> children = new ArrayList<>();
        children.add(arg);
        return new TlaFormulaAppl(TlaStrings.TAIL, children);
    }

    public static TlaFormulaAppl Append(
            TlaVar sequence, TlaVar element) {
        List<TlaExp> children = new ArrayList<>();
        children.add(sequence);
        children.add(element);
        return new TlaFormulaAppl(TlaStrings.APPEND, children);
    }
}
