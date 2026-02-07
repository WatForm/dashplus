package ca.uwaterloo.watform.alloyast.expr;

import ca.uwaterloo.watform.alloyast.AlloyASTNode;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;

public abstract class AlloyExpr extends AlloyASTNode {
    public static final int NO_PAREN = 0;

    public static final int STATE_SEQ = 30;
    public static final int LET = 35;
    public static final int QUANTIFICATION = 35;
    public static final int OR = 40;
    public static final int IFF = 45;
    public static final int IMPLIES = 55;
    public static final int ITE = 60;
    public static final int AND = 65;
    public static final int BIN_TEMP = 70;
    public static final int UNI_TEMP = 75;
    public static final int COMP = 80;
    public static final int QUANTIFIED = 85;
    public static final int SHIFT = 90;
    public static final int PLUS_MINUS = 95;
    public static final int MUL_DIV_REM = 100;
    public static final int NUMERIC = 105;
    public static final int REL_OVERRIDE = 110;
    public static final int INTERSECT = 115;
    public static final int ARROW = 120;
    public static final int DOM_RESTR = 125;
    public static final int RNG_RESTR = 130;
    public static final int BRACKET = 135;
    // same b/c dot(a, b[c]) -> a.(b[c]) even
    // with the same precedence b/c
    // right child case of infix binOp handles it
    // And making them the same avoids
    // redundant brackets
    // like a[b].c $\rightarrow$ (a[b]).c
    public static final int DOT = 135;
    public static final int PRIME = 140;
    public static final int TRANS = 145;

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
