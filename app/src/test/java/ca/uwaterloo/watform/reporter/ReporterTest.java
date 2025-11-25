package ca.uwaterloo.watform.reporter;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.TestUtil;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.utils.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReporterTest {

    private Reporter reporter;

    // This will reset the report before each test
    @BeforeEach
    void setUp() {
        reporter = Reporter.INSTANCE;
        reporter.reset();
    }

    @AfterEach
    void cleanUp() {
        reporter.reset();
    }

    @Test
    @Order(1)
    @DisplayName("Singleton instance should be non-null and consistent")
    public void singleton() {
        assertNotNull(reporter, "Singleton instance should not be null.");
        Reporter anotherInstance = Reporter.INSTANCE;
        assertSame(reporter, anotherInstance, "Multiple accesses should return the same instance.");
    }

    @Test
    @Order(2)
    @DisplayName("Should return empty lists initially")
    public void initialEmptyState() {
        assertTrue(reporter.getErrors().isEmpty(), "Errors list should be empty initially.");
        assertTrue(reporter.getComments().isEmpty(), "Comments list should be empty initially.");
    }

    @Test
    @Order(3)
    @DisplayName("Should add an error correctly using object")
    public void addErrorObject() {
        Reporter.ErrorUser error = new Reporter.ErrorUser(Pos.UNKNOWN, "Test error message");
        reporter.addError(error);

        List<Reporter.ErrorUser> errors = reporter.getErrors();
        assertEquals(1, errors.size(), "Errors list should contain one error.");
        assertSame(error, errors.get(0), "The added error should be in the list.");
        assertTrue(reporter.getComments().isEmpty(), "Comments list should still be empty.");
    }

    @Test
    @Order(4)
    @DisplayName("Should add a comment correctly using object")
    public void addCommentObject() {
        Reporter.CommentUser comment =
                new Reporter.CommentUser(Pos.UNKNOWN, "Test comment message");
        reporter.addComment(comment);

        List<Reporter.CommentUser> comments = reporter.getComments();
        assertEquals(1, comments.size(), "Comments list should contain one comment.");
        assertSame(comment, comments.get(0), "The added comment should be in the list.");
        assertTrue(reporter.getErrors().isEmpty(), "Errors list should still be empty.");
    }

    @Test
    @Order(9)
    @DisplayName("Should add multiple errors and comments")
    public void addMultiple() {
        Reporter.ErrorUser error1 = new Reporter.ErrorUser(Pos.UNKNOWN, "Error 1");
        Reporter.ErrorUser error2 = new Reporter.ErrorUser(Pos.UNKNOWN, "Error 2");
        Reporter.CommentUser comment1 = new Reporter.CommentUser(Pos.UNKNOWN, "Comment 1");

        reporter.addError(error1);
        reporter.addComment(comment1);
        reporter.addError(error2);

        List<Reporter.ErrorUser> errors = reporter.getErrors();
        List<Reporter.CommentUser> comments = reporter.getComments();

        assertEquals(2, errors.size(), "Should have 2 errors.");
        assertEquals(1, comments.size(), "Should have 1 comment.");
        assertTrue(errors.contains(error1), "Errors list should contain error1.");
        assertTrue(errors.contains(error2), "Errors list should contain error2.");
        assertTrue(comments.contains(comment1), "Comments list should contain comment1.");
    }

    @Test
    @Order(10)
    @DisplayName("Returned error list should be unmodifiable")
    public void errorsUnmodifiable() {
        Reporter.ErrorUser error = new Reporter.ErrorUser(Pos.UNKNOWN, "Temporary error");
        List<Reporter.ErrorUser> errors = reporter.getErrors();

        assertThrows(
                UnsupportedOperationException.class,
                () -> {
                    errors.add(error);
                },
                "Should throw UnsupportedOperationException when trying "
                        + "to add to the error list.");
    }

    @Test
    @Order(11)
    @DisplayName("Returned comment list should be unmodifiable")
    public void commentsUnmodifiable() {
        Reporter.CommentUser comment = new Reporter.CommentUser(Pos.UNKNOWN, "Temporary comment");
        List<Reporter.CommentUser> comments = reporter.getComments();

        assertThrows(
                UnsupportedOperationException.class,
                () -> {
                    comments.add(comment);
                },
                "Should throw UnsupportedOperationException when trying "
                        + "to add to the comment list.");
    }

    @Test
    @Order(17)
    @DisplayName("handle Alloy Cmd Syntax Error")
    public void throwCatchErrorForAlloyCmd() throws IOException {
        int[] exitCode = TestUtil.changeReporterExitFn();
        ParserUtil.parse(Paths.get("src/test/resources/reporter/badCmd.als"));
        TestUtil.assertExited(exitCode);
    }

    @Test
    @Order(18)
    @DisplayName("printing more than one pos: see the testing report")
    public void test18() throws IOException {
        int[] exitCode = TestUtil.changeReporterExitFn();
        Path filePath = Paths.get("src/test/resources/alloyast/paragraph/twoModules.als");
        AlloyFile af = assertDoesNotThrow(() -> (ParserUtil.parse(filePath)));
        TestUtil.assertExited(exitCode);
    }
}
