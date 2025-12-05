package ca.uwaterloo.watform.alloyast;

import static ca.uwaterloo.watform.utils.ParserUtil.*;
import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.TestUtil;
import ca.uwaterloo.watform.utils.*;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        assertThrows(Reporter.AbortSignal.class, () -> parse(filePath));
    }

    @Test
    @Order(2)
    @DisplayName("Throw when Module is not declared at the top")
    public void moduleNotTopThrows() throws Exception {
        Path filePath = Paths.get("src/test/resources/alloyast/paragraph/moduleNotTop.als");
        assertThrows(Reporter.AbortSignal.class, () -> parse(filePath));
    }

    @Test
    @Order(3)
    @DisplayName("Throw when AlloyFile's ctor gets DashParagraph")
    public void test3() throws Exception {
        assertThrows(AlloyASTImplError.class, () -> new AlloyFile(TestUtil.createDashState()));
    }
}
