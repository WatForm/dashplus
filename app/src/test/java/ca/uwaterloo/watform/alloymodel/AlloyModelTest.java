package ca.uwaterloo.watform.alloymodel;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.TestUtil;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.parser.*;
import ca.uwaterloo.watform.test.*;
import ca.uwaterloo.watform.utils.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlloyModelTest {
    @BeforeEach
    void setUp() {
        Reporter.INSTANCE.reset();
    }

    @AfterEach
    void cleanUp() {
        Reporter.INSTANCE.reset();
    }

    @Test
    @Order(1)
    @DisplayName("parseCatalystDirToAlloyModelThenToString")
    public void alloyModelCatalyst() throws Exception {
        Path dir = Paths.get("src/test/resources/parsevis/catalyst");
        List<Path> paths = ParserUtil.recurGetFiles(dir, ".als");
        for (Path filePath : paths) {
            try {

                System.out.println(filePath);
                System.out.println("--- File Content ---");

                String originalStr = Files.readString(filePath);

                System.out.println(originalStr);

                AlloyFile af = assertDoesNotThrow(() -> (ParserUtil.parse(filePath)));
                AlloyModel alloyModel = new AlloyModel(af);
                String parsedStr = assertDoesNotThrow(() -> alloyModel.toString());

                System.out.println("--- Parse & toString ---");
                System.out.println(parsedStr);
                System.out.println("--------------------");

            } catch (IOException e) {
                fail("Error reading file: " + e.getMessage(), e);
            } catch (RecognitionException | ParseCancellationException e) {
                fail("Error occurred during parsing: " + e.getMessage(), e);
            } catch (Exception e) {
                fail(
                        "Error occurred during parsing or in parser visitors or "
                                + "in AlloyModel while testing "
                                + filePath
                                + ": "
                                + e.getMessage(),
                        e);
            }
        }
    }

    @Test
    @Order(2)
    @DisplayName("addedParagraphsGetPrintedInToString")
    public void addedParagraphsGetPrintedInToString() throws Exception {
        AlloySigPara s1 = TestUtil.createSig("s1");
        AlloyFile alloyFile = new AlloyFile(s1);
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        AlloySigPara s2 = TestUtil.createSig("s2");
        alloyModel.addParagraph(s2);
        assertTrue(alloyModel.toString().contains(s1.toString()));
        assertTrue(alloyModel.toString().contains(s2.toString()));
    }

    @Test
    @Order(3)
    @DisplayName("duplicateNameThrows")
    public void duplicateNameThrows() throws Exception {
        AlloySigPara s1 = TestUtil.createSig("s1");
        AlloyFile alloyFile = new AlloyFile(s1);
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        AlloySigPara s1Again = TestUtil.createSig("s1");
        assertThrows(AlloyModelError.class, () -> alloyModel.addParagraph(s1Again));
    }

    @Test
    @Order(3)
    @DisplayName("duplicateInstanceThrows")
    public void duplicateInstanceThrows() throws Exception {
        AlloySigPara s1 = TestUtil.createSig("s1");
        AlloyFile alloyFile = new AlloyFile(s1);
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        assertThrows(ImplementationError.class, () -> alloyModel.addParagraph(s1));
    }
}
