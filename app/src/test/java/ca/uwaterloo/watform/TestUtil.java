package ca.uwaterloo.watform;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.utils.*;

public final class TestUtil {

    /**
     * Change Reporter.INSTANCE.exitFunction, so we don't System::exit
     *
     * @return int[] exitCode: where the new Reporter.INSTANCE.exitFunction stores exit code
     */
    public static int[] changeReporterExitFn() {
        final int[] exitCode = {-1};
        Reporter.INSTANCE.exitFunction = (code -> exitCode[0] = code);
        return exitCode;
    }

    /**
     * Assert that ErrorUser is collected and Reporter should exit on exitIfHasErrors
     *
     * @param exitCode
     */
    public static void assertExited(int[] exitCode) {
        Reporter.INSTANCE.exitIfHasErrors();
        assertEquals(1, exitCode[0], "Exit code should have been set to 1");
        assertTrue(Reporter.INSTANCE.hasErrors(), "Reporter should still have errors recorded");
    }
}
