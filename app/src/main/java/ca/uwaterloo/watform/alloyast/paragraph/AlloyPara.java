package ca.uwaterloo.watform.alloyast.paragraph;

import ca.uwaterloo.watform.alloyast.AlloyASTNode;
import ca.uwaterloo.watform.utils.*;
import java.util.Optional;

public abstract class AlloyPara extends AlloyASTNode {
    public AlloyPara(Pos pos) {
        super(pos);
    }

    public AlloyPara() {
        super();
    }

    public abstract Optional<String> getName();
}
