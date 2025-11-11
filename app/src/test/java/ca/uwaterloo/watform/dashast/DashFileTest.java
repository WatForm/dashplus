package ca.uwaterloo.watform.dashast;

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
import java.util.List;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DashFileTest {
    @AfterEach
    void cleanUp() {
        Reporter.INSTANCE.reset();
    }

    @Test
    @Order(1)
    @DisplayName("Throw when file doesn't have exactly one state root")
    public void notExactlyOneStateRootThrows() throws Exception {
        assertThrows(
                DashCtorError.class,
                () ->
                        new DashFile(
                                List.of(TestUtil.createDashState(), TestUtil.createDashState())));
        assertThrows(DashCtorError.class, () -> new DashFile(Collections.emptyList()));
    }

    @Test
    @Order(2)
    @DisplayName("DashFile.super(), which is an AlloyFile, doesn't contain DashParagraph")
    public void superAlloyFileNoDashPara() throws Exception {
        DashFile dashFile = new DashFile(List.of(TestUtil.createDashState()));
        assertEquals(Collections.emptyList(), dashFile.getAlloyParagraphs());
    }
}
