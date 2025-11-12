package ca.uwaterloo.watform.alloyast;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uwaterloo.watform.TestUtil;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.parser.*;
import ca.uwaterloo.watform.test.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlloySigParaTest {
    @Test
    @Order(1)
    @DisplayName("AlloySigPara.expand returns a list of AlloySigPara with " + "individual qname")
    public void sigExpand() throws Exception {
        AlloySigPara sigABC =
                new AlloySigPara(
                        List.of(
                                new AlloyQnameExpr("A"),
                                new AlloyQnameExpr("B"),
                                new AlloyQnameExpr("C")),
                        new AlloyBlock(new AlloyQnameExpr("a")));
        List<AlloySigPara> expanded = sigABC.expand();
        assertEquals(3, expanded.size());

        assertEquals(1, expanded.get(0).qnames.size());
        assertEquals(1, expanded.get(1).qnames.size());
        assertEquals(1, expanded.get(2).qnames.size());

        assertEquals("A", expanded.get(0).qnames.get(0).toString());
        assertEquals("B", expanded.get(1).qnames.get(0).toString());
        assertEquals("C", expanded.get(2).qnames.get(0).toString());

        assertEquals(sigABC.quals, expanded.get(0).quals);
        assertEquals(sigABC.rel, expanded.get(0).rel);
        assertEquals(sigABC.decls, expanded.get(0).decls);
        assertEquals(sigABC.block, expanded.get(0).block);

        assertEquals(sigABC.quals, expanded.get(1).quals);
        assertEquals(sigABC.rel, expanded.get(1).rel);
        assertEquals(sigABC.decls, expanded.get(1).decls);
        assertEquals(sigABC.block, expanded.get(1).block);

        assertEquals(sigABC.quals, expanded.get(2).quals);
        assertEquals(sigABC.rel, expanded.get(2).rel);
        assertEquals(sigABC.decls, expanded.get(2).decls);
        assertEquals(sigABC.block, expanded.get(2).block);

        assertDoesNotThrow(() -> expanded.get(0).getName());
        assertDoesNotThrow(() -> expanded.get(1).getName());
        assertDoesNotThrow(() -> expanded.get(2).getName());
    }

    @Test
    @Order(2)
    @DisplayName("isTopLevel, isSubType, isSubset")
    public void test2() throws Exception {
        AlloySigPara topLevel =
                new AlloySigPara(
                        List.of(new AlloyQnameExpr("A")),
                        null,
                        Collections.emptyList(),
                        TestUtil.createBlock());
        assertTrue(topLevel.isTopLevel());
        assertFalse(topLevel.isSubsig());
        assertFalse(topLevel.isSubset());

        AlloySigPara subType =
                new AlloySigPara(
                        List.of(new AlloyQnameExpr("B")),
                        new AlloySigPara.Extends(new AlloyQnameExpr("A")),
                        Collections.emptyList(),
                        TestUtil.createBlock());
        assertFalse(subType.isTopLevel());
        assertTrue(subType.isSubsig());
        assertFalse(subType.isSubset());

        AlloySigPara subset1 =
                new AlloySigPara(
                        List.of(new AlloyQnameExpr("C")),
                        new AlloySigPara.In(new AlloyQnameExpr("A")),
                        Collections.emptyList(),
                        TestUtil.createBlock());
        assertFalse(subset1.isTopLevel());
        assertFalse(subset1.isSubsig());
        assertTrue(subset1.isSubset());

        AlloySigPara subset2 =
                new AlloySigPara(
                        List.of(new AlloyQnameExpr("D")),
                        new AlloySigPara.Equal(new AlloyQnameExpr("A")),
                        Collections.emptyList(),
                        TestUtil.createBlock());
        assertFalse(subset2.isTopLevel());
        assertFalse(subset2.isSubsig());
        assertTrue(subset2.isSubset());
    }

    @Test
    @Order(3)
    @DisplayName("invalid ctor args")
    public void test3() throws Exception {
        assertThrows(
                AlloyCtorError.class,
                () ->
                        new AlloySigPara(
                                List.of(AlloySigPara.Qual.LONE, AlloySigPara.Qual.ONE),
                                List.of(new AlloyQnameExpr("A")),
                                null,
                                Collections.emptyList(),
                                TestUtil.createBlock()));
        assertThrows(
                AlloyCtorError.class,
                () ->
                        new AlloySigPara(
                                List.of(AlloySigPara.Qual.LONE, AlloySigPara.Qual.SOME),
                                List.of(new AlloyQnameExpr("A")),
                                null,
                                Collections.emptyList(),
                                TestUtil.createBlock()));
        assertThrows(
                AlloyCtorError.class,
                () ->
                        new AlloySigPara(
                                List.of(AlloySigPara.Qual.ONE, AlloySigPara.Qual.SOME),
                                List.of(new AlloyQnameExpr("A")),
                                null,
                                Collections.emptyList(),
                                TestUtil.createBlock()));
        assertThrows(
                AlloyCtorError.class,
                () ->
                        new AlloySigPara(
                                List.of(AlloySigPara.Qual.ABSTRACT),
                                List.of(new AlloyQnameExpr("A")),
                                new AlloySigPara.In(new AlloyQnameExpr("B")),
                                Collections.emptyList(),
                                TestUtil.createBlock()));
    }
}
