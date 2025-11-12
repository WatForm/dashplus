package ca.uwaterloo.watform.alloyast;

import ca.uwaterloo.watform.utils.ImplementationError;
import ca.uwaterloo.watform.utils.Pos;

public final class AlloyASTImplError extends ImplementationError {
    private AlloyASTImplError(Pos pos, String msg) {
        super(pos, msg);
    }

    private AlloyASTImplError(String msg) {
        this(Pos.UNKNOWN, msg);
    }

    /**
     * A WFF error, but it cannot occur through the ANTLR parser. So it cannot occur during parsing;
     * it must be an ImplementationError
     *
     * @param pos
     * @param field1
     * @param field2
     * @param className
     * @return AlloyASTImplError
     */
    public static AlloyASTImplError xorFields(
            Pos pos, String field1, String field2, String className) {
        return new AlloyASTImplError(
                pos,
                field1
                        + " and "
                        + field2
                        + " are mutually-exclusive fields in "
                        + className
                        + ". It must contain exactly one of them: "
                        + pos.toString());
    }

    /**
     * A WFF error, but it cannot occur through the ANTLR parser. So it cannot occur during parsing;
     * it must be an ImplementationError
     *
     * @param pos
     * @param field1
     * @param field2
     * @param className
     * @return AlloyASTImplError
     */
    public static AlloyASTImplError bothNull(
            Pos pos, String field1, String field2, String className) {
        return new AlloyASTImplError(
                pos,
                field1
                        + " and "
                        + field2
                        + " cannot both be null in "
                        + className
                        + ". It must contain at least one of them: "
                        + pos.toString());
    }

    /**
     * A WFF error, but it cannot occur through the ANTLR parser. So it cannot occur during parsing;
     * it must be an ImplementationError
     *
     * @param pos
     * @param field
     * @return
     */
    public static AlloyASTImplError nullOrBlankField(Pos pos, String field) {
        return new AlloyASTImplError(pos, field + " cannot be null or blank: " + pos.toString());
    }

    // ====================================================================================
    // AlloyParseVis
    // ====================================================================================
    public static AlloyASTImplError invalidCase(Pos pos) {
        return new AlloyASTImplError(
                pos, "Alloy parser visitor arrived at an invalid case: " + pos.toString());
    }

    // ====================================================================================
    // AlloyFile
    // ====================================================================================
    public static AlloyASTImplError dashParagraphInAlloyFile(Pos pos) {
        return new AlloyASTImplError(
                pos, "AlloyFile should not contain a DashParagraph: " + pos.toString());
    }
}
