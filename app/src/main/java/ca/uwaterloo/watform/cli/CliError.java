/*
    Errors that can occur in Main.java
    and added directly to the Reporter in Main.java.
*/

package ca.uwaterloo.watform.cli;

import ca.uwaterloo.watform.utils.UserError;

public final class CliError extends UserError {
    private CliError(String msg) {
        super(msg);
    }

    public static CliError invalidParams() {
        return new CliError("Invalid parameters. Please see cli helper message. ");
    }

    public static CliError invalidParams(String s) {
        return new CliError("Invalid parameters. " + s);
    }

    public static CliError invalidFile(String filename) {
        return new CliError("Input file must end with .dsh or .als: " + filename);
    }
}
