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

    public List<TLAPlusExpression> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public void toString(StringBuilder sb, int ident) {

        String s = "Unknown";
        if (this.library == LIBRARIES.STL_FiniteSets) s = TLAPlusStrings.FINITE_SETS;
        else if (this.library == LIBRARIES.STL_Naturals) s = TLAPlusStrings.NATURALS;
        else if (this.library == LIBRARIES.STL_Integers) s = TLAPlusStrings.INTEGERS;
        else if (this.library == LIBRARIES.STL_Sequences) s = TLAPlusStrings.SEQUENCES;

        sb.append(s);
        return;
    }

    public static TLAPlusFormulaApplication Cardinality(TLAPlusVariable arg) {
        List<TLAPlusExpression> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormulaApplication(TLAPlusStrings.CARDINALITY, children);
    }

    public static TLAPlusFormulaApplication Len(TLAPlusVariable arg) {
        List<TLAPlusExpression> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormulaApplication(TLAPlusStrings.LEN, children);
    }

    public static TLAPlusFormulaApplication Head(TLAPlusVariable arg) {
        List<TLAPlusExpression> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormulaApplication(TLAPlusStrings.HEAD, children);
    }

    public static TLAPlusFormulaApplication Tail(TLAPlusVariable arg) {
        List<TLAPlusExpression> children = new ArrayList<>();
        children.add(arg);
        return new TLAPlusFormulaApplication(TLAPlusStrings.TAIL, children);
    }

    public static TLAPlusFormulaApplication Append(
            TLAPlusVariable sequence, TLAPlusVariable element) {
        List<TLAPlusExpression> children = new ArrayList<>();
        children.add(sequence);
        children.add(element);
        return new TLAPlusFormulaApplication(TLAPlusStrings.APPEND, children);
    }
}
