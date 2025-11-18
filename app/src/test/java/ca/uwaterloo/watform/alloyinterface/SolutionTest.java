package ca.uwaterloo.watform.alloyinterface;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.TestUtil;
import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.utils.*;
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
        Solution solution = AlloyInterface.executeCommand(alloyModel);
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
                                                                .Typescope(
                                                                false, 2, true, 2, 1, "S1"))))));
        AlloyModel alloyModel = new AlloyModel(alloyFile);
        Solution solution = AlloyInterface.executeCommand(alloyModel);
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
        Solution solution = AlloyInterface.executeCommand(alloyModel);
        assertTrue(solution.contains(AlloyStrings.THIS + AlloyStrings.SLASH + S1));
        assertTrue(solution.get(AlloyStrings.THIS + AlloyStrings.SLASH + S1).isEmpty());
        solution.next();
        assertTrue(solution.contains(AlloyStrings.THIS + AlloyStrings.SLASH + S1));
        // no longer empty
        assertTrue(!solution.get(AlloyStrings.THIS + AlloyStrings.SLASH + S1).isEmpty());
    }
}
