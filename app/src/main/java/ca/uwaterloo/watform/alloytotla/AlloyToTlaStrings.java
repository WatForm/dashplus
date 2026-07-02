package ca.uwaterloo.watform.alloytotla;

public class AlloyToTlaStrings {

    public static final String SPECIAL = "_";

    // common formulae without arguments
    public static final String INIT = SPECIAL + "Init";
    public static final String NEXT = SPECIAL + "Next";
    public static final String SIG_SETS_UNPRIMED =
            SPECIAL + "sig" + SPECIAL + "sets" + SPECIAL + "unprimed";
    public static final String FIELD_TYPES = SPECIAL + "field" + SPECIAL + "types";
    public static final String SIG_SETS_PRIMED =
            SPECIAL + "sig" + SPECIAL + "sets" + SPECIAL + "primed";
    public static final String ALL_SIG_CONSTRAINTS =
            SPECIAL + "all" + SPECIAL + "sig" + SPECIAL + "constraints";

    public static final String ALL_FACTS = SPECIAL + "all" + SPECIAL + "facts";

    // boilerplate macro formulae
    public static final String SOME = SPECIAL + "some";
    public static final String ONE = SPECIAL + "one";
    public static final String LONE = SPECIAL + "lone";
    public static final String NO = SPECIAL + "no";

    // macro - constants
    public static final String UNIV = SPECIAL + "univ";
    public static final String IDEN = SPECIAL + "iden";
    public static final String NONE = SPECIAL + "none";

    // macro operations
    public static final String RANGE_RESTRICTION = SPECIAL + "range" + SPECIAL + "restriction";
    public static final String DOMAIN_RESTRICTION = SPECIAL + "domain" + SPECIAL + "restriction";
    public static final String RELATIONAL_OVERRIDE = SPECIAL + "relational" + SPECIAL + "override";
    public static final String DOT_MACRO = SPECIAL + "dot";
    public static final String DOT_MAP = SPECIAL + "dot" + SPECIAL + "map";
    public static final String DOT_FILTER = SPECIAL + "dot" + SPECIAL + "filter";
    public static final String TRANSPOSE = SPECIAL + "transpose";
    public static final String CROSS = SPECIAL + "cross";

    public static final String DOLLAR = "$";

    //
    public static final String SIG_CONSTRAINT_SUFFIX = SPECIAL + "sig" + SPECIAL + "constraints";
    public static final String UNNAMED_FACT_PREFIX = SPECIAL + "fact" + SPECIAL;
    public static final String COMMAND = SPECIAL + "command";
    public static final String SCOPE = SPECIAL + "scope";

    public static final String ORDERING_MODULE_ALLOY = "util/ordering";
}
