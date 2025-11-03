package ca.uwaterloo.watform.tlaplusast;

import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

public class TLAPlusSTL extends ASTNode { // enums used for extensibility

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

    /*
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
     */

    @Override
    public void toString(StringBuilder sb, int ident) {
        return;
        // TODO fix this
    }

    public static TLAPlusFormula Cardinality(ASTNode arg) {
        List<ASTNode> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormula(TLAPlusStrings.CARDINALITY, children);
    }

    public static TLAPlusFormula Len(ASTNode arg) {
        List<ASTNode> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormula(TLAPlusStrings.LEN, children);
    }

    public static TLAPlusFormula Head(ASTNode arg) {
        List<ASTNode> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormula(TLAPlusStrings.HEAD, children);
    }

    public static TLAPlusFormula Tail(ASTNode arg) {
        List<ASTNode> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormula(TLAPlusStrings.TAIL, children);
    }

    public static TLAPlusFormula Append(ASTNode sequence, ASTNode element) {
        List<ASTNode> children = new ArrayList<>();
        children.add(sequence);
        children.add(element);
        return new TLAPlusFormula(TLAPlusStrings.APPEND, children);
    }
}
