package ca.uwaterloo.watform.alloyast;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.utils.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlloyNameExprTest {
    @AfterEach
    void cleanUp() {
        Reporter.INSTANCE.reset();
    }

    @Test
    @Order(1)
    @DisplayName("Throw when AlloyNameExpr.label is invalid (null or blank)")
    public void invalidNameLabel() throws Exception {
        assertThrows(AlloyASTImplError.class, () -> new AlloyNameExpr(null));
        assertThrows(AlloyASTImplError.class, () -> new AlloyNameExpr(""));
        assertThrows(AlloyASTImplError.class, () -> new AlloyNameExpr("  "));
    }
}
