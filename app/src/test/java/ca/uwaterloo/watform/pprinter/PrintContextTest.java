package ca.uwaterloo.watform.pprinter;

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
public class PrintContextTest {
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
                                                        new AlloyAndExpr(
                                                                PrintContextTest.longAQname,
                                                                PrintContextTest.longBQname),
                                                        new AlloyAndExpr(
                                                                PrintContextTest.longAQname,
                                                                PrintContextTest.longBQname),
                                                        new AlloyAndExpr(
                                                                PrintContextTest.longAQname,
                                                                PrintContextTest.longBQname),
                                                        new AlloyBlock(
                                                                List.of(
                                                                        new AlloyAndExpr(
                                                                                PrintContextTest
                                                                                        .longAQname,
                                                                                PrintContextTest
                                                                                        .longBQname),
                                                                        new AlloyAndExpr(
                                                                                PrintContextTest
                                                                                        .longAQname,
                                                                                PrintContextTest
                                                                                        .longBQname),
                                                                        new AlloyAndExpr(
                                                                                PrintContextTest
                                                                                        .longAQname,
                                                                                PrintContextTest
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
                                                        new AlloyAndExpr(
                                                                PrintContextTest.longAQname,
                                                                PrintContextTest.longBQname),
                                                        new AlloyAndExpr(
                                                                PrintContextTest.longAQname,
                                                                PrintContextTest.longBQname),
                                                        new AlloyAndExpr(
                                                                PrintContextTest.longAQname,
                                                                PrintContextTest.longBQname),
                                                        new AlloyBlock(
                                                                List.of(
                                                                        new AlloyAndExpr(
                                                                                PrintContextTest
                                                                                        .longAQname,
                                                                                PrintContextTest
                                                                                        .longBQname),
                                                                        new AlloyAndExpr(
                                                                                PrintContextTest
                                                                                        .longAQname,
                                                                                PrintContextTest
                                                                                        .longBQname),
                                                                        new AlloyAndExpr(
                                                                                PrintContextTest
                                                                                        .longAQname,
                                                                                PrintContextTest
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
                                                        new AlloyPrimeExpr(
                                                                PrintContextTest.longAQname),
                                                        new AlloyCardExpr(
                                                                PrintContextTest.longAQname))))));
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
                                                                PrintContextTest.shortAQname,
                                                                List.of(
                                                                        PrintContextTest
                                                                                .shortBQname,
                                                                        PrintContextTest
                                                                                .shortCQname,
                                                                        PrintContextTest
                                                                                .shortDQname)),
                                                        new AlloyBracketExpr(
                                                                PrintContextTest.longAQname,
                                                                List.of(
                                                                        PrintContextTest.longBQname,
                                                                        PrintContextTest.longCQname,
                                                                        PrintContextTest
                                                                                .longDQname)))))));
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
                                                                PrintContextTest.shortAQname,
                                                                PrintContextTest.shortBQname,
                                                                PrintContextTest.shortCQname),
                                                        new AlloyIteExpr(
                                                                PrintContextTest.longAQname,
                                                                PrintContextTest.longBQname,
                                                                PrintContextTest.longCQname))))));
        System.out.println(alloyFile.toPrettyString(30, 4));
    }
}
