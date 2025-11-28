package ca.uwaterloo.watform.alloyast.paragraph;

import ca.uwaterloo.watform.alloyast.AlloyASTNode;
import ca.uwaterloo.watform.utils.*;
import java.util.Optional;

public abstract class AlloyParagraph extends AlloyASTNode {
    public AlloyParagraph(Pos pos) {
        super(pos);
    }

    public AlloyParagraph() {
        super();
    }

    public abstract Optional<String> getName();
}
