package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TLAPlusSTL extends TLAPlusASTNode { // enums used for extensibility

    public static enum LIBRARIES {
        STL_FiniteSets,
        STL_Naturals,
        STL_Integers,
        STL_Sequences
    };

    private LIBRARIES library;

    public TLAPlusSTL(LIBRARIES library) {
        this.library = library;
    }

    @Override
    public List<String> toStringList() {
        String s = "Unknown";
        if (this.library == LIBRARIES.STL_FiniteSets) s = TLAPlusStrings.FINITE_SETS;
        else if (this.library == LIBRARIES.STL_Naturals) s = TLAPlusStrings.NATURALS;
        else if (this.library == LIBRARIES.STL_Integers) s = TLAPlusStrings.INTEGERS;
        else if (this.library == LIBRARIES.STL_Sequences) s = TLAPlusStrings.SEQUENCES;

        List<String> t = new ArrayList<String>();
        t.add(s);
        return t;
    }

    public static TLAPlusFormula Cardinality(TLAPlusASTNode arg) {
        List<TLAPlusASTNode> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormula(TLAPlusStrings.CARDINALITY, children);
    }

    public static TLAPlusFormula Len(TLAPlusASTNode arg) {
        List<TLAPlusASTNode> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormula(TLAPlusStrings.LEN, children);
    }

    public static TLAPlusFormula Head(TLAPlusASTNode arg) {
        List<TLAPlusASTNode> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormula(TLAPlusStrings.HEAD, children);
    }

    public static TLAPlusFormula Tail(TLAPlusASTNode arg) {
        List<TLAPlusASTNode> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormula(TLAPlusStrings.TAIL, children);
    }

    public static TLAPlusFormula Append(TLAPlusASTNode sequence, TLAPlusASTNode element) {
        List<TLAPlusASTNode> children = new ArrayList<>();
        children.add(sequence);
        children.add(element);
        return new TLAPlusFormula(TLAPlusStrings.APPEND, children);
    }
}
