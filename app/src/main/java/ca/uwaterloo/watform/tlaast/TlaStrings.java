package ca.uwaterloo.watform.tlaast;

import ca.uwaterloo.watform.utils.*;

public final class TlaStrings extends CommonStrings {
    // top-level keywords
    public static final String MODULE = "MODULE";
    public static final String EXTENDS = "EXTENDS";
    public static final String VARIABLES = "VARIABLES";
    public static final String CONSTANTS = "CONSTANTS";

    // cfg keywords
    public static final String INVARIANTS = "INVARIANTS";
    public static final String PROPERTIES = "PROPERTIES";
    public static final String SPECIFICATION = "SPECIFICATION";
    public static final String INIT = "INIT";
    public static final String NEXT = "NEXT";

    // top-level structure
    public static final String HEAD_DELIMITER = "----";
    public static final String BODY_DELIMITER = "====";
    public static final String SPACE = " ";

    // comments
    public static final String MULTI_COMMENT_START = "(*";
    public static final String MULTI_COMMENT_END = "*)";
    public static final String SINGLE_COMMENT = "\\*";

    // strings
    public static final String STRING_START = "\"";
    public static final String STRING_END = STRING_START;

    // sets
    public static final String SET_START = "{";
    public static final String SET_END = "}";
    public static final String SET_IN = "\\in";
    public static final String SET_NOT_IN = "\\notin";
    public static final String SET_INTERSECTION = "\\intersect";
    public static final String SET_UNION = "\\union";
    public static final String SET_SUBSET_EQ = "\\subseteq";
    public static final String SET_DIFFERENCE = "\\";
    public static final String SET_PRODUCT = "\\X";

    // unary set ops
    public static final String SET_UNION_UNARY = "UNION";
    public static final String SET_SUBSET_UNARY = "SUBSET";

    // functions
    public static final String EXISTS = "\\E";
    public static final String FOR_ALL = "\\A";
    public static final String MAP = "|->";

    // booleans
    public static final String BOOLEAN = "BOOLEAN";
    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";
    public static final String AND = "/\\";
    public static final String OR = "\\/";
    public static final String NOT = "~";
    public static final String IMPLICATION = "=>";
    public static final String EQUIVALENCE = "<=>";

    // integers
    public static final String RANGE = "..";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String TIMES = "*";
    public static final String LESSER_THAN = "<";
    public static final String LESSER_THAN_EQUALS = "<=";
    public static final String GREATER_THAN = ">";
    public static final String GREATER_THAN_EQUALS = ">=";
    public static final String EQUALS = "=";
    public static final String NOT_EQUALS = "/=";

    public static final String SEQUENCE_OPEN = "<<";
    public static final String SEQUENCE_CLOSE = ">>";
    public static final String TUPLE_OPEN = SEQUENCE_OPEN;
    public static final String TUPLE_CLOSE = SEQUENCE_CLOSE;

    public static final String COMMA = ",";
    public static final String COLON = ":";
    public static final String SQUARE_BRACKET_OPEN = "[";
    public static final String SQUARE_BRACKET_CLOSE = "]";
    public static final String BRACKET_OPEN = "(";
    public static final String BRACKET_CLOSE = ")";

    public static final String PRIME = "'";
    public static final String UNCHANGED = "UNCHANGED";
    public static final String DOMAIN = "DOMAIN";

    public static final String LET = "LET";
    public static final String IN = "IN";
    public static final String IF = "IF";
    public static final String THEN = "\nTHEN";
    public static final String ELSE = "\nELSE";
    public static final String DEFINITION = "==";

    // inbuilt functions
    public static final String HEAD = "Head";
    public static final String TAIL = "Tail";
    public static final String LEN = "Len";
    public static final String APPEND = "Append";
    public static final String CONCATENATE = "\\o";
    public static final String CARDINALITY = "Cardinality";

    // Standard Modules
    public static final String BAGS = "Bags";
    public static final String FINITE_SETS = "FiniteSets";
    public static final String INTEGERS = "Integers";
    public static final String SEQUENCES = "Sequences";
    public static final String NATURALS = "Naturals";

    // infinite sets
    public static final String STRING_SET = "STRING";
    public static final String INT_SET = "Int";

    // comments
    public static final String MULTI_LINE_COMMENT_START = "(*";
    public static final String MULTI_LINE_COMMENT_END = "*)";
    public static final String SINGLE_LINE_COMMENT_START = "\\*";

    // public static final String = "";
}
