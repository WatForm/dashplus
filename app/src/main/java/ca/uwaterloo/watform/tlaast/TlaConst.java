package ca.uwaterloo.watform.tlaast;

public class TlaConst extends TlaSimpleExp {

    /*

    CONSTANTS A, B

    G == A + B

    here, A and B are represented by this node

    */

    public TlaConst(String name) {
        super(name);
    }
}
