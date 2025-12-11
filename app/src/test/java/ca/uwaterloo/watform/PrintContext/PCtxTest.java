package ca.uwaterloo.watform.PrintContext;

import static ca.uwaterloo.watform.utils.ParserUtil.*;
import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyFactPara;
import ca.uwaterloo.watform.utils.*;
import java.util.List;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PCtxTest {
    private static final AlloyQnameExpr shortAQname = new AlloyQnameExpr("A");
    private static final AlloyQnameExpr shortBQname = new AlloyQnameExpr("B");
    private static final AlloyQnameExpr shortCQname = new AlloyQnameExpr("C");
    private static final AlloyQnameExpr shortDQname = new AlloyQnameExpr("D");

    private static final AlloyQnameExpr longAQname = new AlloyQnameExpr("A".repeat(10));
    private static final AlloyQnameExpr longBQname = new AlloyQnameExpr("B".repeat(10));
    private static final AlloyQnameExpr longCQname = new AlloyQnameExpr("C".repeat(10));
    private static final AlloyQnameExpr longDQname = new AlloyQnameExpr("D".repeat(10));

    @Test
    @Order(1)
    @DisplayName("fact with line width 40")
    public void test1() {
        AlloyFile alloyFile =
                new AlloyFile(
                        List.of(
                                new AlloyFactPara(
                                        new AlloyQnameExpr("name"),
                                        new AlloyBlock(
                                                List.of(
                                                        new AlloyAndExpr(longAQname, longBQname),
                                                        new AlloyAndExpr(longAQname, longBQname),
                                                        new AlloyAndExpr(longAQname, longBQname),
                                                        new AlloyBlock(
                                                                List.of(
                                                                        new AlloyAndExpr(
                                                                                longAQname,
                                                                                PCtxTest
                                                                                        .longBQname),
                                                                        new AlloyAndExpr(
                                                                                longAQname,
                                                                                PCtxTest
                                                                                        .longBQname),
                                                                        new AlloyAndExpr(
                                                                                longAQname,
                                                                                PCtxTest
                                                                                        .longBQname))))))));
        System.out.println(alloyFile.toPrettyString(40, 4));
    }

    @Test
    @Order(2)
    @DisplayName("fact with line width 10")
    public void test2() {
        AlloyFile alloyFile =
                new AlloyFile(
                        List.of(
                                new AlloyFactPara(
                                        new AlloyQnameExpr("name"),
                                        new AlloyBlock(
                                                List.of(
                                                        new AlloyAndExpr(longAQname, longBQname),
                                                        new AlloyAndExpr(longAQname, longBQname),
                                                        new AlloyAndExpr(longAQname, longBQname),
                                                        new AlloyBlock(
                                                                List.of(
                                                                        new AlloyAndExpr(
                                                                                longAQname,
                                                                                PCtxTest
                                                                                        .longBQname),
                                                                        new AlloyAndExpr(
                                                                                longAQname,
                                                                                PCtxTest
                                                                                        .longBQname),
                                                                        new AlloyAndExpr(
                                                                                longAQname,
                                                                                PCtxTest
                                                                                        .longBQname))))))));
        System.out.println(alloyFile.toPrettyString(10, 4));
    }

    @Test
    @Order(3)
    @DisplayName("Unary expr")
    public void test3() {
        AlloyFile alloyFile =
                new AlloyFile(
                        List.of(
                                new AlloyFactPara(
                                        new AlloyQnameExpr("name"),
                                        new AlloyBlock(
                                                List.of(
                                                        new AlloyPrimeExpr(longAQname),
                                                        new AlloyCardExpr(longAQname))))));
        System.out.println(alloyFile.toPrettyString(10, 4));
    }

    @Test
    @Order(4)
    @DisplayName("AlloyBracketExpr")
    public void test4() {
        AlloyFile alloyFile =
                new AlloyFile(
                        List.of(
                                new AlloyFactPara(
                                        new AlloyQnameExpr("name"),
                                        new AlloyBlock(
                                                List.of(
                                                        new AlloyBracketExpr(
                                                                shortAQname,
                                                                List.of(
                                                                        shortBQname,
                                                                        shortCQname,
                                                                        shortDQname)),
                                                        new AlloyBracketExpr(
                                                                longAQname,
                                                                List.of(
                                                                        longBQname,
                                                                        longCQname,
                                                                        longDQname)))))));
        System.out.println(alloyFile.toPrettyString(20, 4));
    }

    @Test
    @Order(5)
    @DisplayName("AlloyIteExpr")
    public void test5() {
        AlloyFile alloyFile =
                new AlloyFile(
                        List.of(
                                new AlloyFactPara(
                                        new AlloyQnameExpr("name"),
                                        new AlloyBlock(
                                                List.of(
                                                        new AlloyIteExpr(
                                                                shortAQname,
                                                                shortBQname,
                                                                shortCQname),
                                                        new AlloyIteExpr(
                                                                longAQname,
                                                                longBQname,
                                                                longCQname))))));
        System.out.println(alloyFile.toPrettyString(30, 4));
    }

    @Test
    @Order(6)
    @DisplayName("decl")
    public void test6() {
        AlloyFile alloyFile =
                new AlloyFile(
                        List.of(
                                new AlloyFactPara(
                                        new AlloyQnameExpr("name"),
                                        new AlloyBlock(
                                                List.of(
                                                        new AlloyDecl(shortAQname, shortBQname),
                                                        new AlloyDecl(
                                                                longAQname,
                                                                new AlloyOrExpr(
                                                                        longBQname, longCQname)),
                                                        new AlloyDecl(
                                                                List.of(
                                                                        shortAQname,
                                                                        shortBQname,
                                                                        shortCQname,
                                                                        shortDQname),
                                                                shortAQname),
                                                        new AlloyDecl(
                                                                true,
                                                                true,
                                                                true,
                                                                List.of(
                                                                        shortAQname,
                                                                        shortBQname,
                                                                        shortCQname,
                                                                        shortDQname),
                                                                true,
                                                                AlloyDecl.Quant.LONE,
                                                                shortAQname),
                                                        new AlloyDecl(
                                                                List.of(
                                                                        longAQname,
                                                                        longBQname,
                                                                        longCQname),
                                                                shortBQname),
                                                        new AlloyDecl(
                                                                true,
                                                                true,
                                                                true,
                                                                List.of(
                                                                        longAQname,
                                                                        longBQname,
                                                                        longCQname),
                                                                true,
                                                                AlloyDecl.Quant.LONE,
                                                                longDQname))))));
        System.out.println(alloyFile.toPrettyString(30, 4));
    }

    @Test
    @Order(7)
    @DisplayName("ComprehensionExpr")
    public void test7() {
        AlloyFile alloyFile =
                new AlloyFile(
                        List.of(
                                new AlloyFactPara(
                                        new AlloyQnameExpr("name"),
                                        new AlloyBlock(
                                                List.of(
                                                        new AlloyCphExpr(
                                                                List.of(
                                                                        new AlloyDecl(
                                                                                shortAQname,
                                                                                shortBQname),
                                                                        new AlloyDecl(
                                                                                List.of(
                                                                                        shortAQname,
                                                                                        shortBQname,
                                                                                        shortCQname,
                                                                                        shortDQname),
                                                                                shortAQname),
                                                                        new AlloyDecl(
                                                                                true,
                                                                                true,
                                                                                true,
                                                                                List.of(
                                                                                        shortAQname,
                                                                                        shortBQname,
                                                                                        shortCQname,
                                                                                        shortDQname),
                                                                                true,
                                                                                AlloyDecl.Quant
                                                                                        .LONE,
                                                                                shortAQname),
                                                                        new AlloyDecl(
                                                                                List.of(
                                                                                        longAQname,
                                                                                        longBQname,
                                                                                        longCQname),
                                                                                shortBQname),
                                                                        new AlloyDecl(
                                                                                true,
                                                                                true,
                                                                                true,
                                                                                List.of(
                                                                                        longAQname,
                                                                                        longBQname,
                                                                                        longCQname),
                                                                                true,
                                                                                AlloyDecl.Quant
                                                                                        .LONE,
                                                                                longDQname)),
                                                                shortCQname),
                                                        new AlloyCphExpr(
                                                                List.of(
                                                                        new AlloyDecl(
                                                                                shortAQname,
                                                                                shortBQname)),
                                                                shortCQname),
                                                        new AlloyCphExpr(
                                                                List.of(
                                                                        new AlloyDecl(
                                                                                shortAQname,
                                                                                shortBQname)),
                                                                new AlloyAndExpr(
                                                                        longCQname,
                                                                        new AlloyAndExpr(
                                                                                longCQname,
                                                                                longDQname))))))));
        System.out.println(alloyFile.toPrettyString(40, 4));
    }
}
