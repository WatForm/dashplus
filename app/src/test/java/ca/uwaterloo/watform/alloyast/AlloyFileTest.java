package ca.uwaterloo.watform.alloyast;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.parser.*;
import ca.uwaterloo.watform.test.*;
import ca.uwaterloo.watform.utils.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlloyFileTest {
    @AfterEach
    void cleanUp() {
        Reporter.INSTANCE.reset();
    }

    @Test
    @Order(1)
    @DisplayName("Throw when file has two modules")
    public void twoModulesThrows() throws Exception {
        Path filePath = Paths.get("src/test/resources/alloyast/paragraph/twoModules.als");
        final int[] exitCode = {-1};
        Reporter.INSTANCE.exitFunction = (code -> exitCode[0] = code);
        AlloyFile af = assertDoesNotThrow(() -> (ParserUtil.parse(filePath)));
        assertEquals(1, exitCode[0], "Exit code should have been set to 1");
        assertTrue(Reporter.INSTANCE.hasErrors(), "Reporter should still have errors recorded");
    }

    @Test
    @Order(2)
    @DisplayName("Throw when Module is not declared at the top")
    public void moduleNotTopThrows() throws Exception {
        Path filePath = Paths.get("src/test/resources/alloyast/paragraph/moduleNotTop.als");
        final int[] exitCode = {-1};
        Reporter.INSTANCE.exitFunction = (code -> exitCode[0] = code);
        AlloyFile af = assertDoesNotThrow(() -> (ParserUtil.parse(filePath)));
        assertEquals(1, exitCode[0], "Exit code should have been set to 1");
        assertTrue(Reporter.INSTANCE.hasErrors(), "Reporter should still have errors recorded");
    }
}
