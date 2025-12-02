package ca.uwaterloo.watform.alloyast;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.TestUtil;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.utils.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlloyCmdParaTest {
    @AfterEach
    void cleanUp() {
        Reporter.INSTANCE.reset();
    }

    @Test
    @Order(1)
    @DisplayName("AlloyCmdPara ctor input validation")
    public void invalidCtorArgs() throws Exception {
        assertThrows(
                AlloyCtorError.class,
                () ->
                        new AlloyCmdPara(
                                new AlloyCmdPara.CommandDecl(
                                        AlloyCmdPara.CommandDecl.CmdType.RUN,
                                        null,
                                        TestUtil.createBlock(),
                                        new AlloyCmdPara.CommandDecl.Scope(
                                                new AlloyCmdPara.CommandDecl.Scope.Typescope(
                                                        false, -1, 2, 1, "sig")))));
        assertThrows(
                AlloyCtorError.class,
                () ->
                        new AlloyCmdPara(
                                new AlloyCmdPara.CommandDecl(
                                        AlloyCmdPara.CommandDecl.CmdType.RUN,
                                        null,
                                        TestUtil.createBlock(),
                                        new AlloyCmdPara.CommandDecl.Scope(
                                                new AlloyCmdPara.CommandDecl.Scope.Typescope(
                                                        false, 1, -1, 1, "sig")))));

        assertThrows(
                AlloyCtorError.class,
                () ->
                        new AlloyCmdPara(
                                new AlloyCmdPara.CommandDecl(
                                        AlloyCmdPara.CommandDecl.CmdType.RUN,
                                        null,
                                        TestUtil.createBlock(),
                                        new AlloyCmdPara.CommandDecl.Scope(
                                                new AlloyCmdPara.CommandDecl.Scope.Typescope(
                                                        false, 1, 0, 1, "sig")))));
        assertThrows(
                AlloyCtorError.class,
                () ->
                        new AlloyCmdPara(
                                new AlloyCmdPara.CommandDecl(
                                        AlloyCmdPara.CommandDecl.CmdType.RUN,
                                        null,
                                        TestUtil.createBlock(),
                                        new AlloyCmdPara.CommandDecl.Scope(
                                                new AlloyCmdPara.CommandDecl.Scope.Typescope(
                                                        false, 1, 4, 0, "sig")))));

        assertThrows(
                AlloyCtorError.class,
                () ->
                        new AlloyCmdPara(
                                new AlloyCmdPara.CommandDecl(
                                        AlloyCmdPara.CommandDecl.CmdType.RUN,
                                        null,
                                        TestUtil.createBlock(),
                                        new AlloyCmdPara.CommandDecl.Scope(
                                                new AlloyCmdPara.CommandDecl.Scope.Typescope(
                                                        false, 1, 4, 0, "sig")))));

        assertThrows(
                AlloyCtorError.class,
                () ->
                        new AlloyCmdPara(
                                new AlloyCmdPara.CommandDecl(
                                        AlloyCmdPara.CommandDecl.CmdType.RUN,
                                        null,
                                        TestUtil.createBlock(),
                                        new AlloyCmdPara.CommandDecl.Scope(
                                                new AlloyCmdPara.CommandDecl.Scope.Typescope(
                                                        false, 1, 4, 0, "sig")))));

        assertThrows(
                AlloyCtorError.class,
                () ->
                        new AlloyCmdPara(
                                new AlloyCmdPara.CommandDecl(
                                        AlloyCmdPara.CommandDecl.CmdType.RUN,
                                        null,
                                        TestUtil.createBlock(),
                                        new AlloyCmdPara.CommandDecl.Scope(
                                                new AlloyCmdPara.CommandDecl.Scope.Typescope(
                                                        true,
                                                        31,
                                                        true,
                                                        31,
                                                        1,
                                                        new AlloySigIntExpr())))));
    }
}
