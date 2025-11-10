package ca.uwaterloo.watform.alloymodel;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
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
import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlloyModelTest {
    private AlloyFactPara createNamelessFact() {
        return new AlloyFactPara(new AlloyBlock(new AlloyQnameExpr("a")));
    }

    private AlloySigPara createSig(String name) {
        return new AlloySigPara(new AlloyQnameExpr(name), new AlloyBlock(new AlloyQnameExpr("a")));
    }

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
        AlloySigPara s1 = this.createSig("s1");
        AlloyFile alloyFile = new AlloyFile(s1);
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        AlloySigPara s2 = this.createSig("s2");
        alloyModel.addParagraph(s2);
        assertTrue(alloyModel.toString().contains(s1.toString()));
        assertTrue(alloyModel.toString().contains(s2.toString()));
    }

    @Test
    @Order(3)
    @DisplayName("duplicateNameThrows")
    public void duplicateNameThrows() throws Exception {
        AlloySigPara s1 = this.createSig("s1");
        AlloyFile alloyFile = new AlloyFile(s1);
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        AlloySigPara s1Again = this.createSig("s1");
        assertThrows(AlloyModelErrors.class, () -> alloyModel.addParagraph(s1Again));
    }

    @Test
    @Order(3)
    @DisplayName("duplicateInstanceThrows")
    public void duplicateInstanceThrows() throws Exception {
        AlloySigPara s1 = this.createSig("s1");
        AlloyFile alloyFile = new AlloyFile(s1);
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        assertThrows(ImplementationError.class, () -> alloyModel.addParagraph(s1));
    }

    @Test
    @Order(4)
    @DisplayName("DNEParagraphThrows")
    public void DNEParagraphThrows() throws Exception {
        AlloyFile alloyFile = new AlloyFile(Collections.emptyList());
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        assertThrows(AlloyModelErrors.class, () -> alloyModel.getSigs().getParagraph("s"));
    }
}
