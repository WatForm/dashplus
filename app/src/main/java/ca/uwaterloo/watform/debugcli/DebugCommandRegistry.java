package ca.uwaterloo.watform.debugcli;

public final class DebugCommandRegistry {
    public static final DebugCommand NOT_FOUND = new NotFoundCommand();
    public static final DebugCommand EMPTY = new EmptyCommand();

    private static final DebugCommand[] COMMANDS = {
        new AliasCommand(),
        new AltCommand(),
        new BreakCommand(),
        new CurrentCommand(),
        new DotCommand(),
        new HelpCommand(),
        new HistoryCommand(),
        new InitCommand(),
        new LoadCommand(),
        new QuitCommand(),
        new ReverseStepCommand(),
        new ScopeCommand(),
        new SetCommand(),
        new StepCommand(),
        new TraceCommand(),
        new UntilCommand(),
        new ShowCommand(),
        new GotoCommand(),
        new ForceCommand(),
    };

    private DebugCommandRegistry() {}

    public static DebugCommand commandForString(String string) {
        if (string.isEmpty()) {
            return EMPTY;
        }
        for (DebugCommand command : COMMANDS) {
            if (command.getName().equals(string)) {
                return command;
            }

            String[] shorthand = command.getShorthand();
            if (shorthand != null) {
                for (String s : shorthand) {
                    if (s.equals(string)) {
                        return command;
                    }
                }
            }
        }

        return NOT_FOUND;
    }

    public static DebugCommand[] getAllCommands() {
        return COMMANDS;
    }
}
