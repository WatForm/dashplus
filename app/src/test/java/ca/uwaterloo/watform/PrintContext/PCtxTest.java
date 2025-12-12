package ca.uwaterloo.watform.PrintContext;

import static ca.uwaterloo.watform.utils.ParserUtil.*;
import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.TestUtil;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyLetExpr.AlloyLetAsn;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.*;
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

    public static AlloyAndExpr and1() {
        return new AlloyAndExpr(longAQname, longBQname);
    }

    public static AlloyIteExpr ite1() {
        return new AlloyIteExpr(
                longAQname,
                new AlloyIteExpr(longAQname, longBQname, longCQname),
                new AlloyIteExpr(longAQname, longBQname, longCQname));
    }

    public static AlloyLetAsn letAsn1() {
        return new AlloyLetAsn(shortAQname, shortBQname);
    }

    public static AlloyDecl decl1() {
        return new AlloyDecl(shortAQname, shortBQname);
    }

    public static AlloyDecl decl2() {
        return new AlloyDecl(longAQname, longBQname);
    }

    public static AlloyCmdPara.CommandDecl cmdDecl1() {
        return new AlloyCmdPara.CommandDecl(
                AlloyCmdPara.CommandDecl.CmdType.RUN,
                null,
                TestUtil.createBlock(),
                new AlloyCmdPara.CommandDecl.Scope(
                        new AlloyCmdPara.CommandDecl.Scope.Typescope(false, 1, 3, 1, "sigName")));
    }

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
                                                        and1(),
                                                        and1(),
                                                        and1(),
                                                        ite1(),
                                                        new AlloyBlock(
                                                                List.of(
                                                                        and1(), and1(), and1(),
                                                                        ite1())))))));
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
                                                                                        .longBQname),
                                                                        ite1())))))));
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
                                                                        ite1())))))));
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
                                                                longAQname, longBQname, longCQname),
                                                        new AlloyIteExpr(
                                                                longAQname,
                                                                longBQname,
                                                                new AlloyIteExpr(
                                                                        longAQname,
                                                                        longBQname,
                                                                        longCQname)),
                                                        ite1())))));
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

    @Test
    @Order(8)
    @DisplayName("Let")
    public void test8() {
        AlloyLetExpr let1 = new AlloyLetExpr(List.of(letAsn1()), shortCQname);
        AlloyLetExpr let5 = new AlloyLetExpr(List.of(letAsn1(), letAsn1()), shortCQname);
        AlloyLetExpr let2 =
                new AlloyLetExpr(List.of(letAsn1(), letAsn1(), letAsn1(), letAsn1()), shortCQname);
        AlloyLetExpr let3 =
                new AlloyLetExpr(List.of(letAsn1(), letAsn1(), letAsn1(), letAsn1()), let2);
        AlloyLetExpr let4 =
                new AlloyLetExpr(
                        List.of(
                                letAsn1(),
                                new AlloyLetAsn(shortAQname, let2),
                                letAsn1(),
                                letAsn1()),
                        let2);
        AlloyFile alloyFile =
                new AlloyFile(
                        List.of(
                                new AlloyFactPara(
                                        new AlloyQnameExpr("name"),
                                        new AlloyBlock(List.of(let1, let5, let2, let3, let4)))));
        System.out.println(alloyFile.toPrettyString(30, 4));
    }

    @Test
    @Order(9)
    @DisplayName("Paren")
    public void test9() {
        AlloyParenExpr paren1 = new AlloyParenExpr(and1());
        AlloyParenExpr paren2 = new AlloyParenExpr(ite1());
        AlloyParenExpr paren3 = new AlloyParenExpr(shortAQname);
        AlloyFile alloyFile =
                new AlloyFile(
                        List.of(
                                new AlloyFactPara(
                                        new AlloyQnameExpr("name"),
                                        new AlloyBlock(List.of(paren3, paren1, paren2)))));
        System.out.println(alloyFile.toPrettyString(30, 4));
    }

    @Test
    @Order(10)
    @DisplayName("Quantification")
    public void test10() {
        AlloyQuantificationExpr qt1 =
                new AlloyQuantificationExpr(
                        AlloyQuantificationExpr.Quant.ALL, List.of(decl1(), decl1()), shortAQname);
        AlloyQuantificationExpr qt2 =
                new AlloyQuantificationExpr(
                        AlloyQuantificationExpr.Quant.ALL, List.of(decl2(), decl2()), ite1());
        AlloyFile alloyFile =
                new AlloyFile(
                        List.of(
                                new AlloyFactPara(
                                        new AlloyQnameExpr("name"),
                                        new AlloyBlock(List.of(qt1, qt2)))));
        System.out.println(alloyFile.toPrettyString(30, 4));
    }

    @Test
    @Order(11)
    @DisplayName("Command")
    public void test11() {
        AlloyCmdPara cmd1 = new AlloyCmdPara(cmdDecl1());
        AlloyCmdPara cmd2 = new AlloyCmdPara(List.of(cmdDecl1(), cmdDecl1(), cmdDecl1()));
        AlloyFile alloyFile = new AlloyFile(List.of(cmd1, cmd2));
        System.out.println(alloyFile.toPrettyString(30, 4));
    }
}
