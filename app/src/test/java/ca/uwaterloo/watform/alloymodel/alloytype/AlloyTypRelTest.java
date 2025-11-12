package ca.uwaterloo.watform.alloymodel.alloytype;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.alloymodel.AlloyModelImplError;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlloyTypRelTest {
    @Test
    @Order(1)
    @DisplayName("Invalid ctor args")
    public void test1() throws Exception {
        assertThrows(AlloyModelImplError.class, () -> new AlloyTypRel(null));
        assertThrows(AlloyModelImplError.class, () -> new AlloyTypRel(Collections.emptySet()));
        assertThrows(AlloyModelImplError.class, () -> new AlloyTypRel(Set.of(List.of("", " "))));
    }

    @Test
    @Order(2)
    @DisplayName("equals method")
    public void test2() throws Exception {
        assertEquals(
                new AlloyTypRel(Set.of(List.of("A"), List.of("B"))),
                new AlloyTypRel(Set.of(List.of("A"), List.of("B"))));
        assertEquals(
                new AlloyTypRel(Set.of(List.of("A", "C"), List.of("B", "C"))),
                new AlloyTypRel(Set.of(List.of("A", "C"), List.of("B", "C"))));
        // order doesn't matter
        assertEquals(
                new AlloyTypRel(Set.of(List.of("B", "C"), List.of("A", "C"))),
                new AlloyTypRel(Set.of(List.of("A", "C"), List.of("B", "C"))));

        assertNotEquals(
                new AlloyTypRel(Set.of(List.of("D", "C"), List.of("B", "C"))),
                new AlloyTypRel(Set.of(List.of("A", "C"), List.of("B", "C"))));
    }
}
