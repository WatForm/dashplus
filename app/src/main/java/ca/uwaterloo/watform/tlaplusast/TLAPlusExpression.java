package ca.uwaterloo.watform.tlaplusast;

import ca.uwaterloo.watform.utils.*;
import java.util.List;

public abstract class TLAPlusExpression extends ASTNode {
    public abstract List<TLAPlusExpression> getChildren();
}
