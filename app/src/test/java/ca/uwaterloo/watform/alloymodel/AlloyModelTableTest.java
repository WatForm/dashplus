package ca.uwaterloo.watform.alloymodel;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.TestUtil;
import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.utils.ImplementationError;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlloyModelTableTest {
    @Test
    @Order(1)
    @DisplayName("Add one sig para")
    public void addOneSig() throws Exception {
        AlloySigPara sigS = TestUtil.createSig("s");
        AlloyFile alloyFile = new AlloyFile(sigS);
        AlloyModelTable<AlloySigPara> alloyModelTable =
                new AlloyModelTable<AlloySigPara>(alloyFile, AlloySigPara.class);

        assertTrue(alloyModelTable.containsName(sigS.getName().get()));
        assertEquals(sigS, alloyModelTable.getPara(sigS.getName().get()));
        assertEquals(1, alloyModelTable.getAllParas().size());
        assertTrue(alloyModelTable.getUnnamedParas().isEmpty());
        assertTrue(alloyModelTable.getNames().contains(sigS.getName().get()));
    }

    @Test
    @Order(2)
    @DisplayName("Add a fact with no name")
    public void addNoName() throws Exception {
        AlloyFactPara namelessFact = TestUtil.createNamelessFact();
        AlloyFile alloyFile = new AlloyFile(namelessFact);
        AlloyModelTable<AlloyFactPara> alloyModelTable =
                new AlloyModelTable<AlloyFactPara>(alloyFile, AlloyFactPara.class);

        assertThrows(ImplementationError.class, () -> alloyModelTable.containsName(""));
        assertThrows(ImplementationError.class, () -> alloyModelTable.getPara(""));
        assertEquals(1, alloyModelTable.getAllParas().size());
        assertEquals(1, alloyModelTable.getUnnamedParas().size());
        assertEquals(namelessFact, alloyModelTable.getUnnamedParas().get(0));
        assertTrue(alloyModelTable.getNames().isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("Adding same instance twice throws AlloyModelTable.Error")
    public void addSameInstanceTwice() throws Exception {
        AlloySigPara sigS = TestUtil.createSig("s");
        AlloyFile alloyFile = new AlloyFile(sigS);
        AlloyModelTable<AlloySigPara> alloyModelTable =
                new AlloyModelTable<AlloySigPara>(alloyFile, AlloySigPara.class);
        assertThrows(AlloyModelError.class, () -> alloyModelTable.addPara(sigS, new ArrayList<>()));
    }

    @Test
    @Order(4)
    @DisplayName("Adding sig with same name throws AlloyModelError")
    public void addSameNameSigTwice() throws Exception {
        AlloySigPara sigS1 = TestUtil.createSig("s");
        AlloyFile alloyFile = new AlloyFile(sigS1);
        AlloyModelTable<AlloySigPara> alloyModelTable =
                new AlloyModelTable<AlloySigPara>(alloyFile, AlloySigPara.class);
        AlloySigPara sigS2 = TestUtil.createSig("s");
        assertThrows(
                AlloyModelError.class, () -> alloyModelTable.addPara(sigS2, new ArrayList<>()));
    }

    @Test
    @Order(5)
    @DisplayName("Getting a paragraph that doesn't exist throws AlloyModelError")
    public void getDNEName() throws Exception {
        AlloyFile alloyFile = new AlloyFile(Collections.emptyList());
        AlloyModelTable<AlloySigPara> alloyModelTable =
                new AlloyModelTable<AlloySigPara>(alloyFile, AlloySigPara.class);
        assertThrows(AlloyModelError.class, () -> alloyModelTable.getPara("s"));
    }

    @Test
    @Order(6)
    @DisplayName("Add two sig para")
    public void addTwoSig() throws Exception {
        AlloySigPara sigS1 = TestUtil.createSig("s1");
        AlloySigPara sigS2 = TestUtil.createSig("s2");
        AlloyFile alloyFile = new AlloyFile(new ArrayList<>(List.of(sigS1, sigS2)));
        AlloyModelTable<AlloySigPara> alloyModelTable =
                new AlloyModelTable<AlloySigPara>(alloyFile, AlloySigPara.class);

        assertTrue(alloyModelTable.containsName(sigS1.getName().get()));
        assertTrue(alloyModelTable.containsName(sigS2.getName().get()));

        assertEquals(sigS1, alloyModelTable.getPara(sigS1.getName().get()));
        assertEquals(sigS2, alloyModelTable.getPara(sigS2.getName().get()));

        assertEquals(2, alloyModelTable.getAllParas().size());

        assertTrue(alloyModelTable.getUnnamedParas().isEmpty());

        assertTrue(alloyModelTable.getNames().contains(sigS1.getName().get()));
        assertTrue(alloyModelTable.getNames().contains(sigS2.getName().get()));
    }

    @Test
    @Order(7)
    @DisplayName("AlloyModelTable.addParagraph also adds to param list")
    public void addViaAddParagraph() throws Exception {
        AlloySigPara sigS1 = TestUtil.createSig("s1");
        AlloyFile alloyFile = new AlloyFile(sigS1);
        AlloyModelTable<AlloySigPara> alloyModelTable =
                new AlloyModelTable<AlloySigPara>(alloyFile, AlloySigPara.class);
        AlloySigPara sigS2 = TestUtil.createSig("s2");
        List<AlloyPara> additionalParas = new ArrayList<>();
        assertDoesNotThrow(() -> alloyModelTable.addPara(sigS2, additionalParas));
        assertEquals(1, additionalParas.size());
        assertEquals(sigS2, additionalParas.get(0));

        assertTrue(alloyModelTable.containsName(sigS1.getName().get()));
        assertTrue(alloyModelTable.containsName(sigS2.getName().get()));

        assertEquals(sigS1, alloyModelTable.getPara(sigS1.getName().get()));
        assertEquals(sigS2, alloyModelTable.getPara(sigS2.getName().get()));

        assertEquals(2, alloyModelTable.getAllParas().size());

        assertTrue(alloyModelTable.getUnnamedParas().isEmpty());

        assertTrue(alloyModelTable.getNames().contains(sigS1.getName().get()));
        assertTrue(alloyModelTable.getNames().contains(sigS2.getName().get()));
    }

    @Test
    @Order(8)
    @DisplayName("AlloyModelTable.addParagraph also adds to param list")
    public void addViaAddParagraphs() throws Exception {
        AlloyFile alloyFile = new AlloyFile(Collections.emptyList());
        AlloyModelTable<AlloySigPara> alloyModelTable =
                new AlloyModelTable<AlloySigPara>(alloyFile, AlloySigPara.class);
        AlloySigPara sigS1 = TestUtil.createSig("s1");
        AlloySigPara sigS2 = TestUtil.createSig("s2");
        List<AlloySigPara> paras = List.of(sigS1, sigS2);
        List<AlloyPara> additionalParas = new ArrayList<>();
        assertDoesNotThrow(() -> alloyModelTable.addParas(paras, additionalParas));
        assertEquals(2, additionalParas.size());
        assertEquals(sigS1, additionalParas.get(0));
        assertEquals(sigS2, additionalParas.get(1));

        assertTrue(alloyModelTable.containsName(sigS1.getName().get()));
        assertTrue(alloyModelTable.containsName(sigS2.getName().get()));

        assertEquals(sigS1, alloyModelTable.getPara(sigS1.getName().get()));
        assertEquals(sigS2, alloyModelTable.getPara(sigS2.getName().get()));

        assertEquals(2, alloyModelTable.getAllParas().size());

        assertTrue(alloyModelTable.getUnnamedParas().isEmpty());

        assertTrue(alloyModelTable.getNames().contains(sigS1.getName().get()));
        assertTrue(alloyModelTable.getNames().contains(sigS2.getName().get()));
    }
}
