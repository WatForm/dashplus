package ca.uwaterloo.watform.tlaast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TlaStdLibs extends TlaExp { // enums used for extensibility

    /*

    EXTENDS FiniteSets

    Here, FiniteSets is a TlaStdLibs object whose "library" has the enum value STL_FiniteSets

    Note that EXTENDS is not part of the object

    */

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

        return switch (library) {
            case LIBRARIES.STL_FiniteSets -> TlaStrings.FINITE_SETS;
            case LIBRARIES.STL_Naturals -> TlaStrings.NATURALS;
            case LIBRARIES.STL_Integers -> TlaStrings.INTEGERS;
            case LIBRARIES.STL_Sequences -> TlaStrings.SEQUENCES;
        };
    }

    public static TlaAppl Cardinality(TlaExp arg) {
        return new TlaAppl(TlaStrings.CARDINALITY, Arrays.asList(arg));
    }

    public static TlaAppl Len(TlaVar arg) {

        return new TlaAppl(TlaStrings.LEN, Arrays.asList(arg));
    }

    public static TlaAppl Head(TlaVar arg) {

        return new TlaAppl(TlaStrings.HEAD, Arrays.asList(arg));
    }

    public static TlaAppl Tail(TlaVar arg) {

        return new TlaAppl(TlaStrings.TAIL, Arrays.asList(arg));
    }

    public static TlaAppl Append(TlaVar sequence, TlaVar element) {

        return new TlaAppl(TlaStrings.APPEND, Arrays.asList(sequence, element));
    }

    public static TlaAppl SubSeq(TlaExp sequence, TlaExp start, TlaExp end) {
        return new TlaAppl(TlaStrings.SUBSEQ, Arrays.asList(sequence, start, end));
    }
}
