package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.utils.ParserUtil.*;
import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.TestUtil;
import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.utils.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
        List<Path> paths = recurGetFiles(dir, ".als");
        for (Path filePath : paths) {
            try {

                System.out.println(filePath);
                System.out.println("--- File Content ---");

                String originalStr = Files.readString(filePath);

                System.out.println(originalStr);

                AlloyFile af = assertDoesNotThrow(() -> (parse(filePath)));
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
        alloyModel.addPara(s2);
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
        assertThrows(AlloyModelError.class, () -> alloyModel.addPara(s1Again));
    }

    @Test
    @Order(4)
    @DisplayName("duplicateInstanceThrows")
    public void duplicateInstanceThrows() throws Exception {
        AlloySigPara s1 = TestUtil.createSig("s1");
        AlloyFile alloyFile = new AlloyFile(s1);
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        assertThrows(AlloyModelError.class, () -> alloyModel.addPara(s1));
    }

    @Test
    @Order(5)
    @DisplayName("empty AlloyModel.ctor doesn't throw")
    public void test5() throws Exception {
        assertDoesNotThrow(() -> new AlloyModel());
    }

    @Test
    @Order(6)
    @DisplayName("AlloyModel.getParas(Class<? extends AlloyParagraph> typeToken)")
    public void test6() throws Exception {
        AlloyCmdPara cmdPara1 =
                new AlloyCmdPara(
                        new AlloyCmdPara.CommandDecl(
                                AlloyCmdPara.CommandDecl.CmdType.RUN,
                                new AlloyQnameExpr("pred1"),
                                null,
                                null));
        AlloyFile alloyFile = new AlloyFile(cmdPara1);
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        assertEquals(1, alloyModel.getParas(AlloyCmdPara.class).size());
        assertTrue(alloyModel.getParas(AlloyCmdPara.class).contains(cmdPara1));
        AlloyCmdPara cmdPara2 =
                new AlloyCmdPara(
                        new AlloyCmdPara.CommandDecl(
                                AlloyCmdPara.CommandDecl.CmdType.RUN,
                                new AlloyQnameExpr("pred2"),
                                null,
                                null));
        alloyModel.addPara(cmdPara2);
        assertEquals(2, alloyModel.getParas(AlloyCmdPara.class).size());
        assertTrue(alloyModel.getParas(AlloyCmdPara.class).contains(cmdPara1));
        assertTrue(alloyModel.getParas(AlloyCmdPara.class).contains(cmdPara2));
    }

    @Test
    @Order(7)
    @DisplayName("allow duplicate AlloyCmdPara")
    public void test7() throws Exception {
        AlloyCmdPara cmdPara1 =
                new AlloyCmdPara(
                        new AlloyCmdPara.CommandDecl(
                                AlloyCmdPara.CommandDecl.CmdType.RUN,
                                new AlloyQnameExpr("cmd1"),
                                new AlloyQnameExpr("pred1"),
                                null,
                                null));
        AlloyFile alloyFile = new AlloyFile(cmdPara1);
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        assertEquals(1, alloyModel.getParas(AlloyCmdPara.class).size());
        assertEquals(cmdPara1, alloyModel.getParas(AlloyCmdPara.class).get(0));
        AlloyCmdPara cmdPara1Again =
                new AlloyCmdPara(
                        new AlloyCmdPara.CommandDecl(
                                AlloyCmdPara.CommandDecl.CmdType.RUN,
                                new AlloyQnameExpr("cmd1"),
                                new AlloyQnameExpr("pred1"),
                                null,
                                null));
        assertDoesNotThrow(() -> alloyModel.addPara(cmdPara1Again));
    }

    @Test
    @Order(8)
    @DisplayName("AlloyModel.getParas(Class<? extends AlloyParagraph> typeToken)")
    public void test8() throws Exception {
        AlloyPredPara pred1 = new AlloyPredPara("pred1", TestUtil.createBlock());
        AlloyPredPara pred2 = new AlloyPredPara("pred2", TestUtil.createBlock());
        AlloyFile alloyFile = new AlloyFile(List.of(pred1, pred2));
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        assertEquals(2, alloyModel.getParas(AlloyPredPara.class).size());
        assertTrue(alloyModel.getParas(AlloyPredPara.class).contains(pred1));
        assertTrue(alloyModel.getParas(AlloyPredPara.class).contains(pred2));

        assertEquals(pred1, alloyModel.getPara(AlloyPredPara.class, "pred1"));
        assertEquals(pred2, alloyModel.getPara(AlloyPredPara.class, "pred2"));
    }
}
