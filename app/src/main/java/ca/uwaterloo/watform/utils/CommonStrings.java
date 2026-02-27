package ca.uwaterloo.watform.utils;

public class CommonStrings {
    public static final String TAB = "    ";
    public static final String SPACE = " ";
    public static final String NEWLINE = "\n";
    public static final String DIVIDER = "=====================================";

    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ANSI_BLUE_BOLD = "\u001B[1;34m";

    public static void dashOutput(String x) {
        System.out.println(ANSI_BLUE + x + ANSI_RESET);
    }

    public static void dashOutputBold(String x) {
        System.out.println(ANSI_BLUE_BOLD + x + ANSI_RESET);
    }
}
