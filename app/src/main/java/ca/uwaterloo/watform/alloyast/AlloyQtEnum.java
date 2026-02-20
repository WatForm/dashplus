package ca.uwaterloo.watform.alloyast;

import java.util.EnumSet;

// all quntification/multiplicity operators Alloy needs
// AlloyArrowExpr, DashVarDecls needs LONE, ONE, SOME, SET
// AlloyDecl needs LONE, ONE, SOME, SET, and EXACTLY
// AlloyQtExpr needs all of them
public enum AlloyQtEnum {
    ALL(AlloyStrings.ALL),
    NO(AlloyStrings.NO),
    SOME(AlloyStrings.SOME),
    LONE(AlloyStrings.LONE),
    ONE(AlloyStrings.ONE),
    SET(AlloyStrings.SET),
    SEQ(AlloyStrings.SEQ),
    EXACTLY(AlloyStrings.EXACTLY);

    public final String label;

    private AlloyQtEnum(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }

    public static final EnumSet<AlloyQtEnum> MUL = EnumSet.of(LONE, ONE, SOME, SET);
}
