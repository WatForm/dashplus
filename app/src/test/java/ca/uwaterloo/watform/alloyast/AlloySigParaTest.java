package ca.uwaterloo.watform.alloyast;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.TestUtil;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlloySigParaTest {
    @Test
    @Order(1)
    @DisplayName("AlloySigPara.expand returns a list of AlloySigPara with " + "individual qname")
    public void test1() throws Exception {
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
        assertEquals(sigABC.fields, expanded.get(0).fields);
        assertEquals(sigABC.block, expanded.get(0).block);

        assertEquals(sigABC.quals, expanded.get(1).quals);
        assertEquals(sigABC.rel, expanded.get(1).rel);
        assertEquals(sigABC.fields, expanded.get(1).fields);
        assertEquals(sigABC.block, expanded.get(1).block);

        assertEquals(sigABC.quals, expanded.get(2).quals);
        assertEquals(sigABC.rel, expanded.get(2).rel);
        assertEquals(sigABC.fields, expanded.get(2).fields);
        assertEquals(sigABC.block, expanded.get(2).block);

        assertDoesNotThrow(() -> expanded.get(0).getName());
        assertDoesNotThrow(() -> expanded.get(1).getName());
        assertDoesNotThrow(() -> expanded.get(2).getName());
    }

    @Test
    @Order(2)
    @DisplayName("AlloySigPara.expand also expands fields")
    public void test2() throws Exception {
        AlloyDecl d12 =
                new AlloyDecl(
                        List.of(new AlloyQnameExpr("f1"), new AlloyQnameExpr("f2")),
                        new AlloyQnameExpr("F12"));
        AlloyDecl d34 =
                new AlloyDecl(
                        List.of(new AlloyQnameExpr("f3"), new AlloyQnameExpr("f4")),
                        new AlloyQnameExpr("F34"));
        AlloySigPara sigABC =
                new AlloySigPara(
                        List.of(
                                new AlloyQnameExpr("A"),
                                new AlloyQnameExpr("B"),
                                new AlloyQnameExpr("C")),
                        List.of(d12, d34),
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
        assertEquals(sigABC.block, expanded.get(0).block);

        assertEquals(sigABC.quals, expanded.get(1).quals);
        assertEquals(sigABC.rel, expanded.get(1).rel);
        assertEquals(sigABC.block, expanded.get(1).block);

        assertEquals(sigABC.quals, expanded.get(2).quals);
        assertEquals(sigABC.rel, expanded.get(2).rel);
        assertEquals(sigABC.block, expanded.get(2).block);

        assertEquals("f1", expanded.get(0).fields.get(0).getName().get());
        assertEquals("f2", expanded.get(0).fields.get(1).getName().get());
        assertEquals("f3", expanded.get(0).fields.get(2).getName().get());
        assertEquals("f4", expanded.get(0).fields.get(3).getName().get());
        assertEquals(new AlloyQnameExpr("F12"), expanded.get(0).fields.get(0).expr);
        assertEquals(new AlloyQnameExpr("F12"), expanded.get(0).fields.get(1).expr);
        assertEquals(new AlloyQnameExpr("F34"), expanded.get(0).fields.get(2).expr);
        assertEquals(new AlloyQnameExpr("F34"), expanded.get(0).fields.get(3).expr);

        assertEquals("f1", expanded.get(1).fields.get(0).getName().get());
        assertEquals("f2", expanded.get(1).fields.get(1).getName().get());
        assertEquals("f3", expanded.get(1).fields.get(2).getName().get());
        assertEquals("f4", expanded.get(1).fields.get(3).getName().get());
        assertEquals(new AlloyQnameExpr("F12"), expanded.get(1).fields.get(0).expr);
        assertEquals(new AlloyQnameExpr("F12"), expanded.get(1).fields.get(1).expr);
        assertEquals(new AlloyQnameExpr("F34"), expanded.get(1).fields.get(2).expr);
        assertEquals(new AlloyQnameExpr("F34"), expanded.get(1).fields.get(3).expr);

        assertEquals("f1", expanded.get(2).fields.get(0).getName().get());
        assertEquals("f2", expanded.get(2).fields.get(1).getName().get());
        assertEquals("f3", expanded.get(2).fields.get(2).getName().get());
        assertEquals("f4", expanded.get(2).fields.get(3).getName().get());
        assertEquals(new AlloyQnameExpr("F12"), expanded.get(2).fields.get(0).expr);
        assertEquals(new AlloyQnameExpr("F12"), expanded.get(2).fields.get(1).expr);
        assertEquals(new AlloyQnameExpr("F34"), expanded.get(2).fields.get(2).expr);
        assertEquals(new AlloyQnameExpr("F34"), expanded.get(2).fields.get(3).expr);

        assertDoesNotThrow(() -> expanded.get(0).getName());
        assertDoesNotThrow(() -> expanded.get(1).getName());
        assertDoesNotThrow(() -> expanded.get(2).getName());
    }

    @Test
    @Order(3)
    @DisplayName("isTopLevel, isSubType, isSubset")
    public void test3() throws Exception {
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
    @Order(4)
    @DisplayName("invalid ctor args")
    public void test4() throws Exception {
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

        assertThrows(
                AlloyCtorError.class,
                () ->
                        new AlloySigPara(
                                List.of(),
                                List.of(new AlloyQnameExpr("A")),
                                new AlloySigPara.Extends(new AlloySigIntExpr()),
                                Collections.emptyList(),
                                TestUtil.createBlock()));
        assertThrows(
                AlloyCtorError.class,
                () ->
                        new AlloySigPara(
                                List.of(),
                                List.of(new AlloyQnameExpr("A")),
                                new AlloySigPara.Extends(new AlloySeqIntExpr()),
                                Collections.emptyList(),
                                TestUtil.createBlock()));
        assertThrows(
                AlloyCtorError.class,
                () ->
                        new AlloySigPara(
                                List.of(),
                                List.of(new AlloyQnameExpr("A")),
                                new AlloySigPara.Extends(new AlloyStringExpr()),
                                Collections.emptyList(),
                                TestUtil.createBlock()));
        assertThrows(
                AlloyCtorError.class,
                () ->
                        new AlloySigPara(
                                List.of(),
                                List.of(new AlloyQnameExpr("A")),
                                new AlloySigPara.Extends(new AlloyNoneExpr()),
                                Collections.emptyList(),
                                TestUtil.createBlock()));
    }

    @Test
    @Order(5)
    @DisplayName("isVar")
    public void test5() {
        assertTrue(
                new AlloySigPara(
                                List.of(AlloySigPara.Qual.ONE, AlloySigPara.Qual.VAR),
                                List.of(new AlloyQnameExpr("A")),
                                null,
                                Collections.emptyList(),
                                TestUtil.createBlock())
                        .isVar());
        assertFalse(
                new AlloySigPara(
                                List.of(AlloySigPara.Qual.ONE),
                                List.of(new AlloyQnameExpr("A")),
                                null,
                                Collections.emptyList(),
                                TestUtil.createBlock())
                        .isVar());
    }
}
