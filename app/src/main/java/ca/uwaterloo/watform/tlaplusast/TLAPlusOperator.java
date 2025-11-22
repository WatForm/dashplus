package ca.uwaterloo.watform.tlaplusast;

public abstract class TLAPlusOperator extends TLAPlusExpression {

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

    private final Associativity associativity;
    private final PrecedenceGroup precedenceGroup;

    public TLAPlusOperator(Associativity associativity, PrecedenceGroup precedenceGroup) {
        this.associativity = associativity;
        this.precedenceGroup = precedenceGroup;
    }

    /*
    public abstract String toTLAPlusSnippet(Class parentClass);

    @Override
    public void toString(StringBuilder sb, int ident)
    {
    	sb.append(this.toTLAPlusSnippet(null));
    	return;
    }
    */

}
