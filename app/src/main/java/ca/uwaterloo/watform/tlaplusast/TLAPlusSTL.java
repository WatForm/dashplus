package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TLAPlusSTL extends TLAPlusExpression { // enums used for extensibility

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

    public static TLAPlusFormulaDeclaration Cardinality(TLAPlusVariable arg) {
        List<TLAPlusVariable> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormulaDeclaration(TLAPlusStrings.CARDINALITY, children);
    }

    public static TLAPlusFormulaDeclaration Len(TLAPlusVariable arg) {
        List<TLAPlusVariable> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormulaDeclaration(TLAPlusStrings.LEN, children);
    }

    public static TLAPlusFormulaDeclaration Head(TLAPlusVariable arg) {
        List<TLAPlusVariable> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormulaDeclaration(TLAPlusStrings.HEAD, children);
    }

    public static TLAPlusFormulaDeclaration Tail(TLAPlusVariable arg) {
        List<TLAPlusVariable> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormulaDeclaration(TLAPlusStrings.TAIL, children);
    }

    public static TLAPlusFormulaDeclaration Append(
            TLAPlusVariable sequence, TLAPlusVariable element) {
        List<TLAPlusVariable> children = new ArrayList<>();
        children.add(sequence);
        children.add(element);
        return new TLAPlusFormulaDeclaration(TLAPlusStrings.APPEND, children);
    }
}
