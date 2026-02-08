package ca.uwaterloo.watform.alloyast.expr;

import ca.uwaterloo.watform.alloyast.AlloyASTNode;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public abstract class AlloyExpr extends AlloyASTNode {
    public static final int NO_PAREN = 0;

    public static final int STATE_SEQ_PREC = 30;
    public static final int LET_PREC = 35;
    public static final int QUANTIFICATION_PREC = 35;
    public static final int OR_PREC = 40;
    public static final int IFF_PREC = 45;
    public static final int IMPLIES_PREC = 55;
    public static final int ITE_PREC = 60;
    public static final int AND_PREC = 65;
    public static final int BIN_TEMP_PREC = 70;
    public static final int UNI_TEMP_PREC = 75;
    public static final int COMP_PREC = 80;
    public static final int QUANTIFIED_PREC = 85;
    public static final int SHIFT_PREC = 90;
    public static final int PLUS_MINUS_PREC = 95;
    public static final int MUL_DIV_REM_PREC = 100;
    public static final int NUMERIC_PREC = 105;
    public static final int REL_OVERRIDE_PREC = 110;
    public static final int INTERSECT_PREC = 115;
    public static final int ARROW_PREC = 120;
    public static final int DOM_RESTR_PREC = 125;
    public static final int RNG_RESTR_PREC = 130;
    public static final int BRACKET_PREC = 135;
    // same b/c dot(a, b[c]) -> a.(b[c]) even
    // with the same precedence b/c
    // right child case of infix binOp handles it
    // And making them the same avoids
    // redundant brackets
    // like a[b].c $\rightarrow$ (a[b]).c
    public static final int DOT_PREC = 135;
    public static final int PRIME_PREC = 140;
    public static final int TRANS_PREC = 145;

    public static final int ALWAYS_PAREN = 150;

    public AlloyExpr(Pos pos) {
        super(pos);
    }

    public AlloyExpr() {
        super();
    }

    public abstract <T> T accept(AlloyExprVis<T> visitor);

    public abstract int getPrec();
}
