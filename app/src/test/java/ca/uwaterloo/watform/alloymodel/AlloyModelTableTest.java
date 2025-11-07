package ca.uwaterloo.watform.alloymodel;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.test.*;
import ca.uwaterloo.watform.utils.ImplementationError;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlloyModelTableTest {
    private AlloyFactPara createNamelessFact() {
        return new AlloyFactPara(new AlloyBlock(new AlloyQnameExpr("a")));
    }

    private AlloySigPara createSig(String name) {
        return new AlloySigPara(new AlloyQnameExpr(name), new AlloyBlock(new AlloyQnameExpr("a")));
    }

    @Test
    @Order(1)
    @DisplayName("Add one sig para")
    public void addOneSig() throws Exception {
        AlloyModelTable<AlloySigPara> alloyModelTable = new AlloyModelTable<AlloySigPara>();
        AlloySigPara sigS = this.createSig("s");
        assertDoesNotThrow(() -> alloyModelTable.addParagraph(sigS));

        assertTrue(alloyModelTable.containsName(sigS.getName().get()));
        assertEquals(sigS, alloyModelTable.getParagraph(sigS.getName().get()));
        assertEquals(1, alloyModelTable.getAllParagraphs().size());
        assertTrue(alloyModelTable.getUnnamedParagraphs().isEmpty());
        assertTrue(alloyModelTable.getNames().contains(sigS.getName().get()));
    }

    @Test
    @Order(2)
    @DisplayName("Add a fact with no name")
    public void addNoName() throws Exception {
        AlloyModelTable<AlloyFactPara> alloyModelTable = new AlloyModelTable<AlloyFactPara>();
        AlloyFactPara namelessFact = this.createNamelessFact();
        assertDoesNotThrow(() -> alloyModelTable.addParagraph(namelessFact));

        assertThrows(ImplementationError.class, () -> alloyModelTable.containsName(""));
        assertThrows(ImplementationError.class, () -> alloyModelTable.getParagraph(""));
        assertEquals(1, alloyModelTable.getAllParagraphs().size());
        assertEquals(1, alloyModelTable.getUnnamedParagraphs().size());
        assertEquals(namelessFact, alloyModelTable.getUnnamedParagraphs().get(0));
        assertTrue(alloyModelTable.getNames().isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("Adding same instance twice throws ImplementationError")
    public void addSameInstanceTwice() throws Exception {
        AlloyModelTable<AlloySigPara> alloyModelTable = new AlloyModelTable<AlloySigPara>();
        AlloySigPara sigS = this.createSig("s");
        assertDoesNotThrow(() -> alloyModelTable.addParagraph(sigS));
        assertThrows(ImplementationError.class, () -> alloyModelTable.addParagraph(sigS));
    }

    @Test
    @Order(4)
    @DisplayName("Adding sig with same name throws AlloyModelError")
    public void addSameNameSigTwice() throws Exception {
        AlloyModelTable<AlloySigPara> alloyModelTable = new AlloyModelTable<AlloySigPara>();
        AlloySigPara sigS1 = this.createSig("s");
        assertDoesNotThrow(() -> alloyModelTable.addParagraph(sigS1));
        AlloySigPara sigS2 = this.createSig("s");
        assertThrows(AlloyModelErrors.class, () -> alloyModelTable.addParagraph(sigS2));
    }

    @Test
    @Order(5)
    @DisplayName("Getting a paragraph that doesn't exist throws AlloyModelError")
    public void getDNEName() throws Exception {
        AlloyModelTable<AlloySigPara> alloyModelTable = new AlloyModelTable<AlloySigPara>();
        assertThrows(AlloyModelErrors.class, () -> alloyModelTable.getParagraph("s"));
    }

    @Test
    @Order(6)
    @DisplayName("Add two sig para")
    public void addTwoSig() throws Exception {
        AlloyModelTable<AlloySigPara> alloyModelTable = new AlloyModelTable<AlloySigPara>();
        AlloySigPara sigS1 = this.createSig("s1");
        assertDoesNotThrow(() -> alloyModelTable.addParagraph(sigS1));
        AlloySigPara sigS2 = this.createSig("s2");
        assertDoesNotThrow(() -> alloyModelTable.addParagraph(sigS2));

        assertTrue(alloyModelTable.containsName(sigS1.getName().get()));
        assertTrue(alloyModelTable.containsName(sigS2.getName().get()));

        assertEquals(sigS1, alloyModelTable.getParagraph(sigS1.getName().get()));
        assertEquals(sigS2, alloyModelTable.getParagraph(sigS2.getName().get()));

        assertEquals(2, alloyModelTable.getAllParagraphs().size());

        assertTrue(alloyModelTable.getUnnamedParagraphs().isEmpty());

        assertTrue(alloyModelTable.getNames().contains(sigS1.getName().get()));
        assertTrue(alloyModelTable.getNames().contains(sigS2.getName().get()));
    }
}
