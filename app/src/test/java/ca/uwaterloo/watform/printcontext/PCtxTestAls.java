package ca.uwaterloo.watform.printcontext;

import ca.uwaterloo.watform.TestUtil;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyLetExpr.AlloyLetAsn;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyFunPara.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara.CommandDecl.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara.CommandDecl.Scope.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara.*;
import ca.uwaterloo.watform.utils.*;
import java.util.List;
import org.junit.jupiter.api.*;

public class PCtxTestAls {
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

    public static AlloyCmdPara.CommandDecl.Scope.Typescope typescope1() {
        return new Typescope(false, 1, 3, 1, "sigName");
    }

    public static AlloyCmdPara.CommandDecl cmdDecl1() {
        return new CommandDecl(CmdType.RUN, TestUtil.createBlock(), new Scope(typescope1()));
    }

    public static CommandDecl cmdDecl2() {
        return new CommandDecl(
                CmdType.RUN,
                new AlloyBlock(ite1()),
                new AlloyCmdPara.CommandDecl.Scope(typescope1()));
    }

    public static CommandDecl cmdDecl3() {
        return new CommandDecl(CmdType.RUN, longAQname, new Scope(typescope1()));
    }

    public static CommandDecl cmdDecl4() {
        return new CommandDecl(
                Pos.UNKNOWN,
                CmdType.RUN,
                longAQname,
                longBQname,
                null,
                new Scope(typescope1()),
                new AlloyNumExpr(3));
    }

    public static CommandDecl cmdDecl5() {
        return new CommandDecl(
                Pos.UNKNOWN,
                CmdType.RUN,
                longAQname,
                null,
                new AlloyBlock(ite1()),
                new Scope(
                        new AlloyNumExpr(3),
                        List.of(
                                typescope1(), typescope1(),
                                typescope1(), typescope1())),
                new AlloyNumExpr(3));
    }

    @Test
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
                                                                                longBQname),
                                                                        new AlloyAndExpr(
                                                                                longAQname,
                                                                                longBQname),
                                                                        new AlloyAndExpr(
                                                                                longAQname,
                                                                                longBQname),
                                                                        ite1())))))));
        System.out.println(alloyFile.toPrettyString(10, 4));
    }

    @Test
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
    @DisplayName("Command")
    public void test11() {
        AlloyCmdPara cmd1 = new AlloyCmdPara(cmdDecl1());
        AlloyCmdPara cmd2 = new AlloyCmdPara(List.of(cmdDecl1(), cmdDecl1(), cmdDecl1()));
        AlloyCmdPara cmd3 = new AlloyCmdPara(List.of(cmdDecl2()));
        AlloyCmdPara cmd4 = new AlloyCmdPara(List.of(cmdDecl3()));
        AlloyCmdPara cmd5 = new AlloyCmdPara(List.of(cmdDecl4()));
        AlloyCmdPara cmd6 = new AlloyCmdPara(List.of(cmdDecl5()));
        AlloyFile alloyFile = new AlloyFile(List.of(cmd1, cmd2, cmd3, cmd4, cmd5, cmd6));
        System.out.println(alloyFile.toPrettyString(30, 4));
    }

    @Test
    @DisplayName("module")
    public void test12() {
        AlloyModulePara mod1 =
                new AlloyModulePara(
                        longAQname,
                        List.of(
                                new AlloyModuleArg(false, shortBQname),
                                new AlloyModuleArg(false, shortBQname),
                                new AlloyModuleArg(false, shortBQname)));
        AlloyFile alloyFile = new AlloyFile(List.of(mod1));
        System.out.println(alloyFile.toPrettyString(30, 4));

        AlloyModulePara mod2 =
                new AlloyModulePara(
                        longAQname,
                        List.of(
                                new AlloyModuleArg(true, longBQname),
                                new AlloyModuleArg(true, longBQname),
                                new AlloyModuleArg(true, longBQname)));
        alloyFile = new AlloyFile(List.of(mod2));
        System.out.println(alloyFile.toPrettyString(30, 4));
    }

    @Test
    @DisplayName("sig")
    public void test13() {
        AlloySigPara sig1 = new AlloySigPara(longAQname);
        AlloySigPara sig2 = new AlloySigPara(longAQname, new AlloyBlock(ite1()));
        AlloySigPara sig3 =
                new AlloySigPara(
                        List.of(Qual.ONE, Qual.VAR, Qual.PRIVATE),
                        List.of(longAQname, longBQname, longCQname),
                        new In(List.of(longAQname, longBQname, longCQname)),
                        List.of(decl1(), decl2(), decl1()),
                        new AlloyBlock(ite1()));
        AlloySigPara sig4 =
                new AlloySigPara(
                        List.of(shortAQname), List.of(decl1(), decl2(), decl1()), new AlloyBlock());
        AlloySigPara sig5 =
                new AlloySigPara(
                        List.of(shortAQname), List.of(decl1()), new AlloyBlock(shortBQname));
        AlloyFile alloyFile = new AlloyFile(List.of(sig1, sig2, sig3, sig4, sig5));
        System.out.println(alloyFile.toPrettyString(30, 4));
    }

    @Test
    @DisplayName("assert")
    public void test14() {
        AlloyAssertPara assert1 =
                new AlloyAssertPara(new AlloyStrLiteralExpr("name"), new AlloyBlock(ite1()));
        AlloyAssertPara assert2 = new AlloyAssertPara(longAQname, new AlloyBlock(ite1()));
        AlloyFile alloyFile = new AlloyFile(List.of(assert1, assert2));
        System.out.println(alloyFile.toPrettyString(30, 4));
    }

    @Test
    @DisplayName("assert")
    public void test15() {
        AlloyEnumPara enum1 =
                new AlloyEnumPara(true, longAQname, List.of(longBQname, longCQname, longDQname));
        AlloyEnumPara enum2 =
                new AlloyEnumPara(
                        true, shortAQname, List.of(shortBQname, shortCQname, shortDQname));
        AlloyFile alloyFile = new AlloyFile(List.of(enum1, enum2));
        System.out.println(alloyFile.toPrettyString(30, 4));
    }

    @Test
    @DisplayName("function")
    public void test16() {
        AlloyFunPara fun1 =
                new AlloyFunPara(
                        true,
                        longAQname,
                        longBQname,
                        List.of(decl1(), decl2()),
                        Mul.SET,
                        and1(),
                        new AlloyBlock(ite1()));
        AlloyFunPara fun2 =
                new AlloyFunPara(
                        false,
                        shortAQname,
                        shortBQname,
                        List.of(decl1()),
                        Mul.DEFAULTSET,
                        shortCQname,
                        new AlloyBlock(and1()));
        AlloyFile alloyFile = new AlloyFile(List.of(fun1, fun2));
        System.out.println(alloyFile.toPrettyString(30, 4));
    }

    @Test
    @DisplayName("import")
    public void test17() {
        AlloyImportPara imp1 =
                new AlloyImportPara(
                        true, longAQname, List.of(longBQname, longCQname, longDQname), longAQname);
        AlloyImportPara imp2 =
                new AlloyImportPara(
                        true,
                        shortAQname,
                        List.of(shortBQname, shortCQname, shortDQname),
                        shortAQname);
        AlloyFile alloyFile = new AlloyFile(List.of(imp1, imp2));
        System.out.println(alloyFile.toPrettyString(30, 4));
    }

    @Test
    @DisplayName("macro")
    public void test18() {
        AlloyMacroPara mac1 =
                new AlloyMacroPara(
                        true, longAQname, List.of(longBQname, longCQname, longDQname), ite1());
        AlloyMacroPara mac2 =
                new AlloyMacroPara(
                        true,
                        longAQname,
                        List.of(longBQname, longCQname, longDQname),
                        new AlloyBlock(ite1()));
        AlloyMacroPara mac3 =
                new AlloyMacroPara(
                        true, shortAQname, List.of(shortBQname, shortCQname, shortDQname), ite1());
        AlloyMacroPara mac4 =
                new AlloyMacroPara(
                        true,
                        shortAQname,
                        List.of(shortBQname, shortCQname, shortDQname),
                        new AlloyBlock(ite1()));
        AlloyFile alloyFile = new AlloyFile(List.of(mac1, mac2, mac3, mac4));
        System.out.println(alloyFile.toPrettyString(30, 4));
    }

    @Test
    @DisplayName("pred")
    public void test19() {
        AlloyPredPara pred1 =
                new AlloyPredPara(
                        true,
                        longAQname,
                        longBQname,
                        List.of(decl1(), decl2()),
                        new AlloyBlock(List.of(ite1(), and1())));
        AlloyPredPara pred2 =
                new AlloyPredPara(
                        true, shortAQname, shortBQname, List.of(decl1()), new AlloyBlock(and1()));
        AlloyFile alloyFile = new AlloyFile(List.of(pred1, pred2));
        System.out.println(alloyFile.toPrettyString(30, 4));
    }
}
