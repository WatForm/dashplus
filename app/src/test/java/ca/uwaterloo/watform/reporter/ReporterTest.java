package ca.uwaterloo.watform.reporter;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.utils.*;
import java.io.IOException;
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
    @Order(5)
    @DisplayName("Should add an error correctly using Pos and msg")
    public void addErrorPosMsg() {
        reporter.addError(Pos.UNKNOWN, "Error from Pos/Msg");

        List<Reporter.ErrorUser> errors = reporter.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Error from Pos/Msg", errors.get(0).getMessage());
        assertEquals(Pos.UNKNOWN, errors.get(0).pos);
    }

    @Test
    @Order(6)
    @DisplayName("Should add a comment correctly using Pos and msg")
    public void addCommentPosMsg() {
        reporter.addComment(Pos.UNKNOWN, "Comment from Pos/Msg");

        List<Reporter.CommentUser> comments = reporter.getComments();
        assertEquals(1, comments.size());
        assertEquals("Comment from Pos/Msg", comments.get(0).getMessage());
        assertEquals(Pos.UNKNOWN, comments.get(0).pos);
    }

    @Test
    @Order(7)
    @DisplayName("Should add an error correctly using only msg")
    public void addErrorMsg() {
        reporter.addError("Error from Msg");
        List<Reporter.ErrorUser> errors = reporter.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Error from Msg", errors.get(0).getMessage());
        assertEquals(Pos.UNKNOWN, errors.get(0).pos);
    }

    @Test
    @Order(8)
    @DisplayName("Should add a comment correctly using only msg")
    public void addCommentMsg() {
        reporter.addComment("Comment from Msg");
        List<Reporter.CommentUser> comments = reporter.getComments();
        assertEquals(1, comments.size());
        assertEquals("Comment from Msg", comments.get(0).getMessage());
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
    @Order(12)
    @DisplayName("hasErrors should return true when errors exist")
    public void hasErrorsTrue() {
        reporter.addError(Pos.UNKNOWN, "An error");
        assertTrue(reporter.hasErrors());
    }

    @Test
    @Order(13)
    @DisplayName("hasErrors should return false when no errors exist")
    public void hasErrorsFalse() {
        assertFalse(reporter.hasErrors());
        reporter.addComment(Pos.UNKNOWN, "Just a comment");
        assertFalse(reporter.hasErrors());
    }

    @Test
    @Order(14)
    @DisplayName("hasComments should return true when comments exist")
    public void hasCommentsTrue() {
        reporter.addComment(Pos.UNKNOWN, "A comment");
        assertTrue(reporter.hasComments());
    }

    @Test
    @Order(15)
    @DisplayName("hasComments should return false when no comments exist")
    public void hasCommentsFalse() {
        assertFalse(reporter.hasComments());
        reporter.addError(Pos.UNKNOWN, "Just an error");
        assertFalse(reporter.hasComments());
    }

    @Test
    @Order(16)
    @DisplayName("exitIfHasError exits if has error")
    public void exitOnError() {
        final int[] exitCode = {-1};
        reporter.addError("An Error");
        reporter.exitFunction = (code -> exitCode[0] = code);
        reporter.exitIfHasErrors();
        assertEquals(1, exitCode[0], "Exit code should have been set to 1");
        assertTrue(reporter.hasErrors(), "Reporter should still have errors recorded");
    }

    @Test
    @Order(17)
    @DisplayName("handle Alloy Cmd Syntax Error")
    public void throwCatchErrorForAlloyCmd() throws IOException {
        final int[] exitCode = {-1};
        reporter.exitFunction = (code -> exitCode[0] = code);
        ParserUtil.parse(Paths.get("src/test/resources/reporter/badCmd.als"));
        assertEquals(1, exitCode[0], "Exit code should have been set to 1");
        assertTrue(reporter.hasErrors(), "Reporter should still have errors recorded");
    }
}
