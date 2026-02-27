package ca.uwaterloo.watform.cli;

import ca.uwaterloo.watform.dashtoalloy.DashToAlloy;

public class Constants {
    public static int firstCmdIdx = 0;
    public static int noCmdValue = -1;
    public static int intArgNotPresent = -100;
    public static String stringArgNotPresent =
            String.valueOf(intArgNotPresent); // a stupid filename

    // did -alloy appear on the cmd-line
    public static boolean alloyPresent(DashToAlloy.Options o) {
        return !(o.equals(DashToAlloy.Options.nothing));
    }

    // did -cmd appear on the cmd-line
    public static boolean cmdPresent(Integer i) {
        return !(i.equals(intArgNotPresent));
    }

    // did -gen appear on the cmd-line
    public static boolean genPresent(Integer i) {
        return !(i.equals(intArgNotPresent));
    }

    // did -xml appear on the cmd-line
    public static boolean xmlPresent(String s) {
        return !(s.equals(stringArgNotPresent));
    }

    // does the cmdIdx arg hold a useful value
    public static boolean cmdIdxUseful(Integer i) {
        return (i != intArgNotPresent && i != noCmdValue && i >= firstCmdIdx);
    }
}
