package ca.uwaterloo.watform.alloyast;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.TestUtil;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.parser.*;
import ca.uwaterloo.watform.test.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlloyPredParaTest {
    @AfterEach
    void cleanUp() {
        Reporter.INSTANCE.reset();
    }

    @Test
    @Order(1)
    @DisplayName("AlloyPredPara ctor input validation")
    public void invalidCtorArgs() throws Exception {

        assertThrows(
                AlloyASTImplError.class,
                () ->
                        new AlloyPredPara(
                                Pos.UNKNOWN,
                                true,
                                null,
                                null,
                                Collections.emptyList(),
                                TestUtil.createBlock()));
        assertThrows(
                AlloyASTImplError.class,
                () ->
                        new AlloyPredPara(
                                Pos.UNKNOWN,
                                true,
                                null,
                                new AlloyQnameExpr("a"),
                                null,
                                TestUtil.createBlock()));
        assertThrows(
                AlloyASTImplError.class,
                () ->
                        new AlloyPredPara(
                                Pos.UNKNOWN,
                                true,
                                null,
                                new AlloyQnameExpr("a"),
                                Collections.emptyList(),
                                null));
    }
}
