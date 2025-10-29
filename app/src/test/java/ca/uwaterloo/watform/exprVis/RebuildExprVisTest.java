package ca.uwaterloo.watform.exprVis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyAndExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyArrowExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyDotExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import ca.uwaterloo.watform.test.*;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RebuildExprVisTest {
    @Test
    @Order(1)
    @DisplayName("Rebuilding AlloyDotExpr should call visit(AlloyBinaryExpr)")
    public void dotShouldUseVisitBinary() throws Exception {
        AlloyExpr dot = new AlloyDotExpr(new AlloyNameExpr("left"), new AlloyNameExpr("right"));
        AlloyDotExpr rebuiltDot = (AlloyDotExpr) dot.accept(new RebuildExprVis());
        assertEquals("binLeft", rebuiltDot.left.toString());
        assertEquals("binRight", rebuiltDot.right.toString());
    }

    @Test
    @Order(2)
    @DisplayName("Rebuilding AlloyAndExpr should call visit(AlloyAndExpr)")
    public void andShouldUseVisitAnd() throws Exception {
        AlloyExpr and = new AlloyAndExpr(new AlloyNameExpr("left"), new AlloyNameExpr("right"));
        AlloyAndExpr rebuiltAnd = (AlloyAndExpr) and.accept(new RebuildExprVis());
        assertEquals("andLeft", rebuiltAnd.left.toString());
        assertEquals("andRight", rebuiltAnd.right.toString());
    }

    @Test
    @Order(3)
    @DisplayName("Rebuilding AlloyArrowExpr should call visit(AlloyArrowExpr)")
    public void arrowShouldUseVisitArrow() throws Exception {
        AlloyExpr arrow =
                new AlloyArrowExpr(
                        new AlloyNameExpr("left"),
                        AlloyArrowExpr.Mul.SOME,
                        AlloyArrowExpr.Mul.SOME,
                        new AlloyNameExpr("right"));
        AlloyArrowExpr rebuiltArrow = (AlloyArrowExpr) arrow.accept(new RebuildExprVis());
        assertEquals("arrowLeft", rebuiltArrow.left.toString());
        assertEquals("some", rebuiltArrow.mul1.toString());
        assertEquals("one", rebuiltArrow.mul2.toString());
        assertEquals("arrowRight", rebuiltArrow.right.toString());
    }
}
