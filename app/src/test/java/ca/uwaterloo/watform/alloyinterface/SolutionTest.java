package ca.uwaterloo.watform.alloyinterface;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.TestUtil;
import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNumExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloySigIntExpr;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SolutionTest {
    @Test
    @Order(1)
    @DisplayName("empty S1")
    public void test1() throws Exception {
        String S1 = "S1";
        AlloyFile alloyFile = new AlloyFile(TestUtil.createSig(S1));
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        Solution solution = AlloyInterface.checkModelSatisfiability(alloyModel);
        System.out.println(solution);
        assertTrue(solution.contains(AlloyStrings.THIS + AlloyStrings.SLASH + S1));
        assertTrue(solution.get(AlloyStrings.THIS + AlloyStrings.SLASH + S1).isEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("two S1s")
    public void test2() throws Exception {
        String S1 = "S1";
        AlloyFile alloyFile =
                new AlloyFile(
                        List.of(
                                TestUtil.createSig(S1),
                                new AlloyCmdPara(
                                        new AlloyCmdPara.CommandDecl(
                                                AlloyCmdPara.CommandDecl.CmdType.RUN,
                                                null,
                                                TestUtil.createBlock(),
                                                new AlloyCmdPara.CommandDecl.Scope(
                                                        new AlloyCmdPara.CommandDecl.Scope
                                                                .Typescope(false, 2, 2, 1, S1))))));
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        Solution solution = AlloyInterface.checkModelSatisfiability(alloyModel);
        assertTrue(solution.contains(AlloyStrings.THIS + AlloyStrings.SLASH + S1));
        assertEquals(2, solution.get(AlloyStrings.THIS + AlloyStrings.SLASH + S1).size());
    }

    @Test
    @Order(3)
    @DisplayName("Solution.next")
    public void test3() throws Exception {
        String S1 = "S1";
        AlloyFile alloyFile = new AlloyFile(TestUtil.createSig(S1));
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        Solution solution = AlloyInterface.checkModelSatisfiability(alloyModel);
        assertTrue(solution.contains(AlloyStrings.THIS + AlloyStrings.SLASH + S1));
        assertTrue(solution.get(AlloyStrings.THIS + AlloyStrings.SLASH + S1).isEmpty());
        solution.next();
        assertTrue(solution.contains(AlloyStrings.THIS + AlloyStrings.SLASH + S1));
        // no longer empty
        assertTrue(!solution.get(AlloyStrings.THIS + AlloyStrings.SLASH + S1).isEmpty());
    }

    @Test
    @Order(4)
    @DisplayName("Solution.eval(sigName)")
    public void test4() throws Exception {
        String S1 = "S1";
        AlloySigPara S1Sig = new AlloySigPara(new AlloyQnameExpr(S1), TestUtil.createBlock());
        AlloyCmdPara cmdPara =
                new AlloyCmdPara(
                        new AlloyCmdPara.CommandDecl(
                                AlloyCmdPara.CommandDecl.CmdType.RUN,
                                null,
                                TestUtil.createBlock(),
                                new AlloyCmdPara.CommandDecl.Scope(
                                        new AlloyCmdPara.CommandDecl.Scope.Typescope(
                                                false, 2, 2, 1, S1))));
        AlloyFile alloyFile = new AlloyFile(List.of(S1Sig, cmdPara));
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        Solution solution = AlloyInterface.checkModelSatisfiability(alloyModel);
        assertEquals(2, solution.get(AlloyStrings.THIS + AlloyStrings.SLASH + S1).size());
        assertEquals(2, solution.eval(S1Sig).size());
        assertEquals(
                solution.get(AlloyStrings.THIS + AlloyStrings.SLASH + S1), solution.eval(S1Sig));
    }

    @Test
    @Order(5)
    @DisplayName("Solution.eval(sigName, fieldName)")
    public void test5() throws Exception {
        String S1 = "S1";
        String F = "F";
        AlloySigPara S1Sig =
                new AlloySigPara(
                        new AlloyQnameExpr(S1),
                        List.of(
                                new AlloyDecl(
                                        new AlloyQnameExpr(F),
                                        AlloyQtEnum.ONE,
                                        new AlloySigIntExpr())),
                        TestUtil.createBlock());
        AlloyCmdPara cmdPara =
                new AlloyCmdPara(
                        new AlloyCmdPara.CommandDecl(
                                AlloyCmdPara.CommandDecl.CmdType.RUN,
                                null,
                                TestUtil.createBlock(),
                                new AlloyCmdPara.CommandDecl.Scope(
                                        new AlloyCmdPara.CommandDecl.Scope.Typescope(
                                                false, 2, 2, 1, S1))));
        AlloyFile alloyFile = new AlloyFile(List.of(S1Sig, cmdPara));
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        Solution solution = AlloyInterface.checkModelSatisfiability(alloyModel);

        assertEquals(2, solution.get(AlloyStrings.THIS + AlloyStrings.SLASH + S1).size());
        assertEquals(2, solution.eval(S1Sig).size());
        assertEquals(
                solution.get(AlloyStrings.THIS + AlloyStrings.SLASH + S1), solution.eval(S1Sig));

        assertEquals(
                2,
                solution.get(AlloyStrings.THIS + AlloyStrings.SLASH + S1 + AlloyStrings.DOT + F)
                        .size());
        assertEquals(2, solution.eval(S1Sig, S1Sig.fields.getFirst()).size());
        assertEquals(
                solution.get(AlloyStrings.THIS + AlloyStrings.SLASH + S1 + AlloyStrings.DOT + F),
                solution.eval(S1Sig, S1Sig.fields.getFirst()));
    }

    @Test
    @Order(6)
    @DisplayName("Solution.eval(AlloyExpr alloyExpr)")
    public void test6() {
        String S1 = "S1";
        String F = "F";
        AlloySigPara S1Sig =
                new AlloySigPara(
                        new AlloyQnameExpr(S1),
                        List.of(
                                new AlloyDecl(
                                        new AlloyQnameExpr(F),
                                        AlloyQtEnum.ONE,
                                        new AlloySigIntExpr())),
                        TestUtil.createBlock());
        AlloyCmdPara cmdPara =
                new AlloyCmdPara(
                        new AlloyCmdPara.CommandDecl(
                                AlloyCmdPara.CommandDecl.CmdType.RUN,
                                null,
                                TestUtil.createBlock(),
                                new AlloyCmdPara.CommandDecl.Scope(
                                        new AlloyCmdPara.CommandDecl.Scope.Typescope(
                                                false, 1, 1, 1, S1))));
        AlloyFile alloyFile = new AlloyFile(List.of(S1Sig, cmdPara));
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        Solution solution = AlloyInterface.checkModelSatisfiability(alloyModel);

        Solution.EvalRes evalRes =
                solution.eval(
                        new AlloyCardExpr(
                                new AlloyDotExpr(new AlloyQnameExpr(S1), new AlloyQnameExpr(F))));
        assertTrue(evalRes.isInt());
        assertEquals(1, evalRes.intVal());
        evalRes =
                solution.eval(
                        new AlloyEqualsExpr(
                                new AlloyNumExpr(1),
                                new AlloyCardExpr(
                                        new AlloyDotExpr(
                                                new AlloyQnameExpr(S1), new AlloyQnameExpr(F)))));
        assertTrue(evalRes.isBool());
        assertTrue(evalRes.boolVal());
        evalRes = solution.eval(new AlloyDotExpr(new AlloyQnameExpr(S1), new AlloyQnameExpr(F)));
        assertTrue(evalRes.isSet());
        assertEquals(1, evalRes.setVal().size());
    }
}
