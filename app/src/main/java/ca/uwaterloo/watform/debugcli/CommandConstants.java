package ca.uwaterloo.watform.debugcli;

public class CommandConstants {
    public static final String DONE = "done.";
    public static final String READING_MODEL = "Reading model from %s...";
    public static final String READING_DASH_MODEL = "Reading dash model from %s... \n";
    public static final String READING_TRACE = "Reading trace from %s...";
    public static final String WRITING_DOT_GRAPH = "Writing DOT graph to %s...";
    public static final String NO_SUCH_FILE = "%s: No such file.\n";
    public static final String NO_FILE_SPECIFIED = "Error. No file specified.";
    public static final String IO_FAILED = "error. I/O failed.";
    public static final String FAILED_TO_READ_FILE = "error. Failed to read file.";
    public static final String FAILED_TO_READ_CONF = "error. Invalid configuration.";
    public static final String SETTING_PARSING_OPTIONS = "Setting default parsing options...";
    public static final String SETTING_PARSING_OPTIONS_FROM = "Setting parsing options from %s...";
    public static final String UNDEFINED_COMMAND = "Undefined command: \"%s\". Try \"help\".\n";
    public static final String AVAILABLE_COMMANDS_STR = "Available commands:\n\n";
    public static final String COMMAND_HELP_DELIMITER = "%-15s-- %s\n";
    public static final String INTEGER_ERROR = "Error. Input must be an integer.";
    public static final String GR_ONE_ERROR = "Error. Input must be >= 1.";
    public static final String SIG_NOT_FOUND = "Signature not found.";
    public static final String ALT_UNAVAILABLE = "No alternate execution paths.";
    public static final String UNTIL_FAILED = "Unable to find satisfying solution.";
    public static final String NO_MODEL_LOADED =
            "No model file specified.\nUse the \"load\" command.";

    public static final String ALIAS_NAME = "alias";
    public static final String ALIAS_DESCRIPTION = "Control the set of aliases used";
    public static final String ALIAS_HELP =
            "Control the set of formula aliases used.\n\n"
                    + "Usage:\n\n"
                    + "alias <alias> <formula>    -- Add an alias\n"
                    + "alias -rm <alias>          -- Remove an alias\n"
                    + "alias -l                   -- List all aliases\n"
                    + "alias -c                   -- Clear aliases";
    public static final String ALIAS_DNE = "Alias \"%s\" does not exist.";
    public static final String[] ALIAS_SHORTHAND = {};

    public static final String CURRENT_NAME = "current";
    public static final String CURRENT_DESCRIPTION = "Display the current state";
    public static final String CURRENT_HELP =
            "Display the current state.\n\n"
                    + "Usage: current [property]\n\n"
                    + "By default, all properties are printed.";
    public static final String[] CURRENT_SHORTHAND = {"c", "curr"};

    public static final String SHOW_NAME = "show";
    public static final String SHOW_DESCRIPTION = "Display a specified state";
    public static final String SHOW_HELP =
            "Display a specified state.\n\n"
                    + "Usage: show [state name]\n\n"
                    + "By default, all properties are printed.";
    public static final String[] SHOW_SHORTHAND = {"s"};

    public static final String HELP_NAME = "help";
    public static final String HELP_DESCRIPTION = "Display the list of available commands";
    public static final String[] HELP_SHORTHAND = {"h"};
    public static final String HELP_COMMAND_END_STR =
            "\nType \"help\" followed by a command name for full documentation.";

    public static final String INIT_NAME = "init";
    public static final String INIT_DESCRIPTION = "Return to the initial state of the active model";
    public static final String INIT_HELP =
            "Return to the initial state of the active model.\n\nUsage: init";
    public static final String[] INIT_SHORTHAND = {"i"};

    public static final String GOTO_NAME = "goto";
    public static final String GOTO_DESCRIPTION = "Goto a specified state";
    public static final String GOTO_HELP = "Goto a specified state.\n\nUsage: goto [state name]";

    public static final String LOAD_NAME = "load";
    public static final String LOAD_DESCRIPTION = "Load an Alloy model";
    public static final String LOAD_HELP =
            "Load an Alloy model.\n\n"
                    + "Usage: load <filename>\n\n"
                    + "The specified file must be an Alloy (.als) file.\n\n"
                    + "You can also specify custom parsing options for this Alloy model as a comment.\n"
                    + "The comment block needs to have a header as: BEGIN_ALDB_CONF and a footer as: END_ALDB_CONF.\n"
                    + "The configuration format follows the YAML format for the set conf command.";
    public static final String[] LOAD_SHORTHAND = {"l", "ld"};

    public static final String QUIT_NAME = "quit";
    public static final String QUIT_DESCRIPTION = "Exit ALDB";
    public static final String QUIT_HELP =
            "Exit ALDB\n\n"
                    + "Confirmation is required when attempting to quit during an active debugging session.";
    public static final String[] QUIT_SHORTHAND = {"q", "exit"};
    public static final String QUIT_USER_PROMPT =
            "A debugging session is active.\nQuit anyway? (y or n) ";
    public static final String[] QUIT_ACCEPTED_RESPONSES = {"y", "Y", "yes", "Yes"};

    public static final String[] REVERSE_STEP_SHORTHAND = {"rs", "r"};
    public static final String REVERSE_STEP_NAME = "reverse-step";
    public static final String REVERSE_STEP_DESCRIPTION =
            "Go back n steps in the current state traversal path";
    public static final String REVERSE_STEP_HELP =
            "Go back n steps in the current state traversal path.\n\n"
                    + "Usage: reverse-step [n]\n\n"
                    + "n is an integer >= 1. By default, n = 1.";

    public static final String SET_NAME = "set";
    public static final String SET_DESCRIPTION = "Set ALDB options";
    public static final String SET_HELP =
            "Set ALDB options.\n\n"
                    + "Available options:\n\n"
                    + "set conf [filename]\n\n"
                    + "    Set the parsing configuration for the current session.\n\n"
                    + "    The specified file must be in YAML. The following (customizable) properties are set by default:\n\n"
                    + "    # Name of the sig representing the main state in the Alloy model.\n"
                    + "    stateSigName: State\n"
                    + "    # Name of the predicate which defines the initial state in the Alloy model.\n"
                    + "    initPredicateName: init\n"
                    + "    # Name of the transition relation in the Alloy model.\n"
                    + "    transitionRelationName: next\n"
                    + "    # Additional Alloy sig scopes to specify.\n"
                    + "    additionalSigScopes: {}\n\n"
                    + "    Running set conf with no filename will set the above default options.\n\n"
                    + "set diff <on | off>\n\n"
                    + "    Turn on/off differential output mode.\n\n"
                    + "    When enabled, only fields that have changed between the previous and current state are displayed when executing step or alt.\n"
                    + "    By default, this option is enabled.";

    public static final String STEP_NAME = "step";
    public static final String STEP_DESCRIPTION = "Perform a state transition of n steps";
    public static final String STEP_HELP =
            "Perform a state transition of n steps.\n\n"
                    + "Usage: step [n | constraints]\n\n"
                    + "n must be an integer >= 1. By default, n = 1.\n\n"
                    + "Alternatively, step constraints can be specified, as a comma-separated list enclosed by square brackets.\n"
                    + "n is equal to the number of items in the list.\n"
                    + "The i-th constraint is applied when performing the i-th transition.";
    public static final String[] STEP_SHORTHAND = {"s", "st"};

    public static final String TRACE_NAME = "trace";
    public static final String TRACE_DESCRIPTION = "Load a saved Alloy XML instance";
    public static final String TRACE_HELP =
            "Load a saved Alloy XML instance.\n\n"
                    + "Usage: trace <filename>\n\n"
                    + "The specified file should be in the Alloy XML format.";
    public static final String[] TRACE_SHORTHAND = {"t"};

    public static final String HISTORY_NAME = "history";
    public static final String HISTORY_DESCRIPTION = "Display past states";
    public static final String HISTORY_HELP =
            "Display the past n consecutive states of the active execution path.\n\n"
                    + "Usage: history [n]\n\n"
                    + "n must be an integer >= 1. By default, n = 3.";

    public static final String SCOPE_NAME = "scope";
    public static final String SCOPE_DESCRIPTION = "Display scope set";
    public static final String SCOPE_HELP =
            "Display the scope set of the active model.\n\n"
                    + "Usage: scope [sig-name]\n\n"
                    + "By default, scope sets are displayed for all signatures in the model.";

    public static final String ALT_NAME = "alt";
    public static final String ALT_DESCRIPTION = "Select an alternate execution path";
    public static final String ALT_HELP =
            "Select an alternate execution path.\n\n"
                    + "Usage: alt [-r]\n\n"
                    + "If \"-r\" is specified, the previous execution path is selected.";
    public static final String[] ALT_SHORTHAND = {"a"};

    public static final String BREAK_NAME = "break";
    public static final String BREAK_DESCRIPTION = "Control the set of constraints used";
    public static final String BREAK_HELP =
            "Control the set of constraints used.\n\n"
                    + "Usage:\n\n"
                    + "break <constraint>  -- Add a constraint\n"
                    + "break -rm <num>     -- Remove a constraint\n"
                    + "break -l            -- List all constraints\n"
                    + "break -c            -- Clear constraints";
    public static final String CONSTRAINT_REGEX = "([^\"]\\S*|\".+?\")\\s*";
    public static final String INVALID_CONSTRAINT_ID = "No constraint number %d.";
    public static final String INVALID_CONSTRAINT = "Invalid constraint: \"%s\".";
    public static final String[] BREAK_SHORTHAND = {"b"};

    public static final String UNTIL_NAME = "until";
    public static final String UNTIL_DESCRIPTION = "Run until constraints are met";
    public static final String UNTIL_HELP =
            "Run until constraints are met.\n\n"
                    + "Usage: until [limit]\n\n"
                    + "limit must be an integer >= 1. By default, limit = 10.";
    public static final String[] UNTIL_SHORTHAND = {"u"};

    public static final String DOT_NAME = "dot";
    public static final String DOT_DESCRIPTION = "Dump DOT graph to disk";
    public static final String DOT_HELP =
            "Dump DOT graph to the current working directory.\n\nUsage: dot";
    public static final String[] DOT_SHORTHAND = {"d"};

    public static final String TEST_NAME = "test";
    public static final String[] TEST_SHORTHAND = {"T"};

    public static final String Force_NAME = "force";
    public static final String Force_DESCRIPTION =
            "force a constraint to happen in a specified number of steps (10 by default)";
    public static final String Force_HELP =
            "force a constraint to happen in a specified number of steps (10 by default).\n\nUsage: force [transition name] [max-steps]";

    public static final String[] Force_SHORTHAND = {"f"};
}
