package ca.uwaterloo.watform.tlaplusast;

public abstract class TLAPlusOp extends TLAPlusExp {

    public static enum Associativity {
        LEFT, // a.b.c is parsed as (a.b).c  e.g. subtraction
        RIGHT, // a.b.c is parsed as a.(b.c) e.g. exponentiation
        IRRELEVANT, // when a.(b.c) = (a.b).c semantically  e.g. addition
        UNSAFE // use parenthesis in all cases
    }

    public static enum PrecedenceGroup {
        SAFE, // this is for operators which never needs brackets, either for them or their children
        UNSAFE, // this is for operators that do need brackets for both them and their children

        // arithmetic
        ADD_SUB,
        MULT,

        // logical
        OR,
        AND,
        NOT,
        IMPLICATION, // => and <=>
        PREDICATE, // Exists and For-all
        AND_LIST,
        OR_LIST,

        // set
        SET_UNION,
        SET_INTERSECTION,
        SET_DIFFERENCE,
        SET_PRODUCT,

        SET_MEMBERSHIP,

        COMAPRISON,

        RANGE,
        CONCAT
    }

    public static final PrecedenceGroup[] PRECEDENCE_ORDER =
            new PrecedenceGroup[] {

                // lowest goes here

                PrecedenceGroup.ADD_SUB, PrecedenceGroup.MULT,
                // highest goes here
            };

    private final Associativity associativity;
    private final PrecedenceGroup precedenceGroup;

    public TLAPlusOp(Associativity associativity, PrecedenceGroup precedenceGroup) {
        this.associativity = associativity;
        this.precedenceGroup = precedenceGroup;
    }

    public static boolean childNeedsParenthesis(TLAPlusExp parent, TLAPlusExp child) {
        // TODO implement this to improve readability of generated code

        // this is a conservative implementation - by default, parentheses are needed

        // if either one is not an operator, no brackets are needed

        // if either one is SAFE, no brackets are needed

        // if either one is UNSAFE, brackets are needed, no more questions

        // if the priority of the parent is less than that of the child, no brackets are needed

        // if the priority of the parent is the same as that of the child and the parent is a binary
        // operator:
        // 1) if parent is left associative and child is the left child, no brackets needed
        // 2) if parent is right associative and child is the right child, no brackets needed
        // 3) if the parent is irrelevant associative, no brackets needed

        // if the parent node is an operator with a higher precedence than a lower node, brackets
        // are necessary
        return true;
    }

    public String getTLASnippetOfChild(TLAPlusExp child) {
        return child.toTLAPlusSnippet(TLAPlusOp.childNeedsParenthesis(this, child));
    }

    public Associativity getAssociativity() {
        return this.associativity;
    }

    public PrecedenceGroup getPrecedenceGroup() {
        return this.precedenceGroup;
    }
}
