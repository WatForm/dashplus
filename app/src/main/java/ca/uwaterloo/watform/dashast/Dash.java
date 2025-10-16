package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.alloyast.Pos;

public abstract class Dash  {
	// methods that all of Dash AST should have

   /**
     * The filename, line, and column position in the original Alloy model file
     * (cannot be null).
     */
    public Pos pos;

    public final Pos getPos() {
        return pos;
    }
    public abstract String toString(Integer indent);

}