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
public class PPrinterTest {
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
                                                                PPrinterTest.longAQname,
                                                                PPrinterTest.longBQname),
                                                        new AlloyAndExpr(
                                                                PPrinterTest.longAQname,
                                                                PPrinterTest.longBQname),
                                                        new AlloyAndExpr(
                                                                PPrinterTest.longAQname,
                                                                PPrinterTest.longBQname),
                                                        new AlloyBlock(
                                                                List.of(
                                                                        new AlloyAndExpr(
                                                                                PPrinterTest
                                                                                        .longAQname,
                                                                                PPrinterTest
                                                                                        .longBQname),
                                                                        new AlloyAndExpr(
                                                                                PPrinterTest
                                                                                        .longAQname,
                                                                                PPrinterTest
                                                                                        .longBQname),
                                                                        new AlloyAndExpr(
                                                                                PPrinterTest
                                                                                        .longAQname,
                                                                                PPrinterTest
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
                                                                PPrinterTest.longAQname,
                                                                PPrinterTest.longBQname),
                                                        new AlloyAndExpr(
                                                                PPrinterTest.longAQname,
                                                                PPrinterTest.longBQname),
                                                        new AlloyAndExpr(
                                                                PPrinterTest.longAQname,
                                                                PPrinterTest.longBQname),
                                                        new AlloyBlock(
                                                                List.of(
                                                                        new AlloyAndExpr(
                                                                                PPrinterTest
                                                                                        .longAQname,
                                                                                PPrinterTest
                                                                                        .longBQname),
                                                                        new AlloyAndExpr(
                                                                                PPrinterTest
                                                                                        .longAQname,
                                                                                PPrinterTest
                                                                                        .longBQname),
                                                                        new AlloyAndExpr(
                                                                                PPrinterTest
                                                                                        .longAQname,
                                                                                PPrinterTest
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
                                                        new AlloyPrimeExpr(PPrinterTest.longAQname),
                                                        new AlloyCardExpr(
                                                                PPrinterTest.longAQname))))));
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
                                                                PPrinterTest.shortAQname,
                                                                List.of(
                                                                        PPrinterTest.shortBQname,
                                                                        PPrinterTest.shortCQname,
                                                                        PPrinterTest.shortDQname)),
                                                        new AlloyBracketExpr(
                                                                PPrinterTest.longAQname,
                                                                List.of(
                                                                        PPrinterTest.longBQname,
                                                                        PPrinterTest.longCQname,
                                                                        PPrinterTest
                                                                                .longDQname)))))));
        System.out.println(alloyFile.toPrettyString(20, 4));
    }
}
