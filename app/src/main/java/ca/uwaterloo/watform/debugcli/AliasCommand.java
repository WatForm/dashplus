package ca.uwaterloo.watform.debugcli;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AliasCommand extends DebugCommand {
    private static final String RM_FLAG = "-rm";
    private static final String L_FLAG = "-l";
    private static final String C_FLAG = "-c";

    private static final String ERROR_ADDING =
            "Error adding alias. Please ensure nested aliases are specified properly.";

    public String getName() {
        return CommandConstants.ALIAS_NAME;
    }

    public String getDescription() {
        return CommandConstants.ALIAS_DESCRIPTION;
    }

    public String getHelp() {
        return CommandConstants.ALIAS_HELP;
    }

    public String[] getShorthand() {
        return CommandConstants.ALIAS_SHORTHAND;
    }

    public void execute(String[] input, DebugSimulationManager simulationManager) {
        if (input.length == 1) {
            System.out.println(CommandConstants.ALIAS_HELP);
            return;
        }
        String arg = input[1];

        AliasManager am = simulationManager.getAliasManager();
        if (arg.equals(RM_FLAG)) {
            if (input.length != 3) {
                System.out.println(CommandConstants.ALIAS_HELP);
                return;
            }
            String alias = input[2];
            if (!am.removeAlias(alias)) {
                System.out.println(String.format(CommandConstants.ALIAS_DNE, alias));
            }
        } else if (arg.equals(L_FLAG)) {
            System.out.println(am.getFormattedAliases());
        } else if (arg.equals(C_FLAG)) {
            am.clearAliases();
        } else {
            if (input.length < 3) {
                System.out.println(CommandConstants.ALIAS_HELP);
                return;
            }

            String formula = String.join(" ", Arrays.copyOfRange(input, 2, input.length));
            Matcher m = Pattern.compile(CommandConstants.CONSTRAINT_REGEX).matcher(formula);

            if (m.find()) {
                if (!m.hitEnd()) {
                    System.out.println(CommandConstants.ALIAS_HELP);
                    return;
                }
                formula = m.group(1).replace("\"", "");

                if (!am.addAlias(arg, formula)) {
                    System.out.println(ERROR_ADDING);
                }
            }
        }
    }
}
