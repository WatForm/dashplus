package ca.uwaterloo.watform.alloyast;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlloyDeclTest {
    @Test
    @Order(1)
    @DisplayName("AlloyDecl.expand returns a list of AlloyDecl with " + "individual qname")
    public void test1() throws Exception {
        AlloyDecl nestedDecl =
                new AlloyDecl(
                        List.of(new AlloyQnameExpr("a"), new AlloyQnameExpr("b")),
                        AlloyQtEnum.ONE,
                        new AlloySigIntExpr());
        assertEquals(2, nestedDecl.qnames.size());
        List<AlloyDecl> expandedDecls = nestedDecl.expand();
        assertEquals(2, expandedDecls.size());

        AlloyDecl aDecl = expandedDecls.get(0);
        assertEquals("a", aDecl.getName());
        assertEquals(new AlloySigIntExpr(), aDecl.expr);

        AlloyDecl bDecl = expandedDecls.get(1);
        assertEquals("b", bDecl.getName());
        assertEquals(new AlloySigIntExpr(), bDecl.expr);
    }
}
