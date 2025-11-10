package ca.uwaterloo.watform.alloyast;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.parser.*;
import ca.uwaterloo.watform.test.*;
import ca.uwaterloo.watform.utils.*;
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
}
