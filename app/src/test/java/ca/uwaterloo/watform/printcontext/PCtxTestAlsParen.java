package ca.uwaterloo.watform.printcontext;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara.*;
import ca.uwaterloo.watform.utils.*;
import java.util.List;
import org.junit.jupiter.api.*;

public class PCtxTestAlsParen {
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
        return new AlloyDecl(shortAQname, AlloyQtEnum.ONE, shortBQname);
    }

    public static AlloyDecl decl2() {
        return new AlloyDecl(longAQname, AlloyQtEnum.ONE, longBQname);
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
    @DisplayName("Dot")
    public void test1() {
        AlloyDotExpr dot =
                new AlloyDotExpr(
                        new AlloyQnameExpr("A"),
                        new AlloyDotExpr(new AlloyQnameExpr("B"), new AlloyQnameExpr("C")));
        assertEquals(dot.toString(), "A.(B.C)");
        dot =
                new AlloyDotExpr(
                        new AlloyDotExpr(new AlloyQnameExpr("A"), new AlloyQnameExpr("B")),
                        new AlloyQnameExpr("C"));
        assertEquals(dot.toString(), "A.B.C");
    }

    @Test
    @DisplayName("Arrow")
    public void test2() {
        AlloyArrowExpr arrow =
                new AlloyArrowExpr(
                        new AlloyArrowExpr(new AlloyQnameExpr("A"), new AlloyQnameExpr("B")),
                        new AlloyQnameExpr("C"));
        assertEquals(arrow.toString(), "(A set->set B) set->set C");
        arrow =
                new AlloyArrowExpr(
                        new AlloyQnameExpr("A"),
                        new AlloyArrowExpr(new AlloyQnameExpr("B"), new AlloyQnameExpr("C")));
        assertEquals(arrow.toString(), "A set->set B set->set C");
    }

    @Test
    @DisplayName("And with Or")
    public void test3() {
        AlloyAndExpr and =
                new AlloyAndExpr(
                        new AlloyOrExpr(new AlloyQnameExpr("A"), new AlloyQnameExpr("B")),
                        new AlloyQnameExpr("C"));
        assertEquals(and.toString(), "(A or B) and C");
        AlloyOrExpr or =
                new AlloyOrExpr(
                        new AlloyAndExpr(new AlloyQnameExpr("A"), new AlloyQnameExpr("B")),
                        new AlloyQnameExpr("C"));
        assertEquals(or.toString(), "A and B or C");
    }

    @Test
    @DisplayName("Neg")
    public void test4() {
        AlloyNegExpr neg =
                new AlloyNegExpr(new AlloyOrExpr(new AlloyQnameExpr("A"), new AlloyQnameExpr("B")));
        assertEquals(neg.toString(), "! (A or B)");
    }
}
