package ca.uwaterloo.watform.alloyast;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlloyBlockTest {
    @AfterEach
    void cleanUp() {
        Reporter.INSTANCE.reset();
    }

    // @Test
    @Order(1)
    @DisplayName("Changing ctor arg doesn't change state of instance")
    public void test1() throws Exception {
        List<AlloyExpr> li = new ArrayList<>();
        li.add(new AlloyQnameExpr("A"));
        AlloyBlock alloyBlock = new AlloyBlock(li);
        assertEquals(1, alloyBlock.exprs.size());
        li.add(new AlloyQnameExpr("B"));
        assertEquals(1, alloyBlock.exprs.size());
    }
}
