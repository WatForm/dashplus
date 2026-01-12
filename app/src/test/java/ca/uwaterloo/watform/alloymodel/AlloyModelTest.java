package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.utils.ParserUtil.*;
import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.TestUtil;
import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.utils.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
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
        Path dir = Paths.get("src/test/resources/parsevis/jackson");
        List<Path> paths = recurGetFiles(dir, ".als");
        dir = Paths.get("src/test/resources/parsevis/watformals");
        paths.addAll(recurGetFiles(dir, ".als"));
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

    @Test
    @Order(9)
    @DisplayName("AlloyModel.declaredIds (1)")
    public void test9() {
        String s1 = "s1";
        String s2 = "s2";
        String s3 = "s3";
        String s4p1 = "s4p1";
        String s4p2 = "s4p2";
        String s5 = "s5";
        String f1 = "f1";
        String f2 = "f2";

        AlloyDecl decl1 = new AlloyDecl(new AlloyQnameExpr(f1), new AlloyQnameExpr("F1"));
        AlloyDecl decl2 = new AlloyDecl(new AlloyQnameExpr(f2), new AlloyQnameExpr("F2"));
        AlloySigPara sig1 = new AlloySigPara(s1, List.of(decl1), new AlloyBlock());
        AlloySigPara sig2 = new AlloySigPara(s2);
        AlloyFile alloyFile = new AlloyFile(List.of(sig1, sig2));
        AlloyModel alloyModel = new AlloyModel(alloyFile);

        assertTrue(alloyModel.containsId(s1));
        assertTrue(alloyModel.containsId(s2));
        assertFalse(alloyModel.containsId(s3));
        assertFalse(alloyModel.containsId(s4p1));
        assertFalse(alloyModel.containsId(s4p2));
        assertFalse(alloyModel.containsId(s5));
        assertTrue(alloyModel.containsId(f1));
        assertFalse(alloyModel.containsId(f2));

        AlloySigPara sig3 = new AlloySigPara(s3, List.of(decl2), new AlloyBlock());
        alloyModel.addPara(sig3);

        assertTrue(alloyModel.containsId(s1));
        assertTrue(alloyModel.containsId(s2));
        assertTrue(alloyModel.containsId(s3));
        assertFalse(alloyModel.containsId(s4p1));
        assertFalse(alloyModel.containsId(s4p2));
        assertFalse(alloyModel.containsId(s5));
        assertTrue(alloyModel.containsId(f1));
        assertTrue(alloyModel.containsId(f2));

        AlloySigPara sig4and5 =
                new AlloySigPara(
                        List.of(
                                new AlloyQnameExpr(
                                        List.of(new AlloyNameExpr(s4p1), new AlloyNameExpr(s4p2))),
                                new AlloyQnameExpr(s5)),
                        Collections.emptyList(),
                        new AlloyBlock());
        alloyModel.addPara(sig4and5);

        assertTrue(alloyModel.containsId(s1));
        assertTrue(alloyModel.containsId(s2));
        assertTrue(alloyModel.containsId(s3));
        assertTrue(alloyModel.containsId(s4p1));
        assertTrue(alloyModel.containsId(s4p2));
        assertTrue(alloyModel.containsId(s5));
        assertTrue(alloyModel.containsId(f1));
        assertTrue(alloyModel.containsId(f2));
    }

    @Test
    @Order(10)
    @DisplayName("AlloyModel.declaredIds (2)")
    public void test10() {
        String s1 = "Chairs";
        String s2 = "Players";

        AlloySigPara sig1 =
                new AlloySigPara(
                        List.of(new AlloyQnameExpr(s1), new AlloyQnameExpr(s2)), new AlloyBlock());
        AlloyFile alloyFile = new AlloyFile(sig1);
        AlloyModel alloyModel = new AlloyModel(alloyFile);

        assertTrue(alloyModel.containsId(s1));
        assertTrue(alloyModel.containsId(s2));
    }
}
