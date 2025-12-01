package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TLAPlusStdLibs extends TLAPlusExp { // enums used for extensibility

    public static enum LIBRARIES {
        STL_FiniteSets,
        STL_Naturals,
        STL_Integers,
        STL_Sequences
    };

    private LIBRARIES library;

    public TLAPlusStdLibs(LIBRARIES library) {
        this.library = library;
    }

    public List<TLAPlusExp> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public String toTLAPlusSnippetCore() {

        if (this.library == LIBRARIES.STL_FiniteSets) return TLAPlusStrings.FINITE_SETS;
        else if (this.library == LIBRARIES.STL_Naturals) return TLAPlusStrings.NATURALS;
        else if (this.library == LIBRARIES.STL_Integers) return TLAPlusStrings.INTEGERS;
        else if (this.library == LIBRARIES.STL_Sequences) return TLAPlusStrings.SEQUENCES;

        return "Unknown"; // implementation error TODO
    }

    public static TLAPlusFormulaAppl Cardinality(TLAPlusVar arg) {
        List<TLAPlusExp> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormulaAppl(TLAPlusStrings.CARDINALITY, children);
    }

    public static TLAPlusFormulaAppl Len(TLAPlusVar arg) {
        List<TLAPlusExp> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormulaAppl(TLAPlusStrings.LEN, children);
    }

    public static TLAPlusFormulaAppl Head(TLAPlusVar arg) {
        List<TLAPlusExp> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormulaAppl(TLAPlusStrings.HEAD, children);
    }

    public static TLAPlusFormulaAppl Tail(TLAPlusVar arg) {
        List<TLAPlusExp> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormulaAppl(TLAPlusStrings.TAIL, children);
    }

    public static TLAPlusFormulaAppl Append(
            TLAPlusVar sequence, TLAPlusVar element) {
        List<TLAPlusExp> children = new ArrayList<>();
        children.add(sequence);
        children.add(element);
        return new TLAPlusFormulaAppl(TLAPlusStrings.APPEND, children);
    }
}
