package ca.uwaterloo.watform.tlaast;

public abstract class TlaOperator extends TlaExp {

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
        AND_OR, // 3
        NOT, // 4
        IMPLICATION, // => and <=> 2
        PREDICATE, // Exists and For-all
        AND_OR_LIST,

        // set
        SET_OPERATORS,

        SET_MEMBERSHIP, // 5

        COMPARISON, // 5

        RANGE,
        CONCAT
    }

    public static final PrecedenceGroup[] PRECEDENCE_ORDER =
            new PrecedenceGroup[] {

                // lowest goes here
                PrecedenceGroup.AND_OR,
                PrecedenceGroup.AND_OR_LIST,
                PrecedenceGroup.NOT,
                PrecedenceGroup.COMPARISON,
                PrecedenceGroup.SET_MEMBERSHIP,
                PrecedenceGroup.SET_OPERATORS,
                // highest goes here
            };

    public final Associativity associativity;
    public final PrecedenceGroup precedenceGroup;

    public TlaOperator(Associativity associativity, PrecedenceGroup precedenceGroup) {
        this.associativity = associativity;
        this.precedenceGroup = precedenceGroup;
    }

    public static boolean childNeedsParenthesis(TlaExp parent, TlaExp child) {
        // TODO implement this to improve readability of generated code

        // this is a conservative implementation - by default, parentheses are needed

        // if either one is not an operator, no brackets are needed
        if (!(parent instanceof TlaOperator && child instanceof TlaOperator)) return false;

        PrecedenceGroup parentPrecedenceGroup = ((TlaOperator) parent).precedenceGroup;
        PrecedenceGroup childPrecedenceGroup = ((TlaOperator) child).precedenceGroup;
        Associativity parentAssociativity = ((TlaOperator) parent).associativity;
        // Associativity childAssociativity = ((TlaOperator) child).associativity;

        // if either one is SAFE, no brackets are needed
        if (parentPrecedenceGroup == PrecedenceGroup.SAFE) return false;
        if (childPrecedenceGroup == PrecedenceGroup.SAFE) return false;

        // if either one is UNSAFE, brackets are needed, no more questions
        if (parentPrecedenceGroup == TlaOperator.PrecedenceGroup.UNSAFE) return true;
        if (childPrecedenceGroup == TlaOperator.PrecedenceGroup.UNSAFE) return true;

        // if parent and child are the same operator and associativity is irrelevant, then no need
        // brackets - however, if they are different operators, then brackets needed
        if (parent.getClass() == child.getClass()
                && parentAssociativity == Associativity.IRRELEVANT) return false;

        // if the priority of the parent is less than that of the child, no brackets are needed
        int parentPriority = PRECEDENCE_ORDER.length;
        int childPriority = -1;
        for (int i = 0; i < PRECEDENCE_ORDER.length; i++) {
            if (PRECEDENCE_ORDER[i] == parentPrecedenceGroup) parentPriority = i;
            if (PRECEDENCE_ORDER[i] == childPrecedenceGroup) childPriority = i;
        }
        if (parentPriority < childPriority) return false;

        // if the priority of the parent is the same as that of the child and the parent is a binary
        // operator:
        // 1) if parent is left associative and child is the left child, no brackets needed
        // 2) if parent is right associative and child is the right child, no brackets needed
        // 3) if the parent is irrelevant associative, no brackets needed

        // if the parent node is an operator with a higher precedence than a lower node, brackets
        // are necessary
        return true;
    }

    public String getTLASnippetOfChild(TlaExp child) {
        return child.toTLAPlusSnippet(TlaOperator.childNeedsParenthesis(this, child));
    }

    public Associativity getAssociativity() {
        return this.associativity;
    }

    public PrecedenceGroup getPrecedenceGroup() {
        return this.precedenceGroup;
    }
}
