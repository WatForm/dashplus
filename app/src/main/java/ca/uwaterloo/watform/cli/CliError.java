package ca.uwaterloo.watform.cli;

import ca.uwaterloo.watform.utils.DashPlusException;

public final class CliError extends DashPlusException {
    private CliError(String msg) {
        super(msg);
    }

    public static CliError invalidParams() {
        return new CliError("Invalid parameters. Please see cli helper message. ");
    }

    public static CliError invalidFile(String filename) {
        return new CliError("Input file must end with .dsh or .als: " + filename);
    }
}
