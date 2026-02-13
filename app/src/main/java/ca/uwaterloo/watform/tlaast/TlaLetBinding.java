package ca.uwaterloo.watform.tlaast;

import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import java.util.ArrayList;
import java.util.List;

public class TlaLetBinding extends TlaOperator {

    /*
    LET x == e1 IN e2
    */

    public final List<TlaDefn> definitions; // x == e1
    public final TlaExp expression; // e2

    public TlaLetBinding(List<TlaDefn> definitions, TlaExp expression) {
        super(TlaOperator.Associativity.IRRELEVANT, TlaOperator.PrecedenceGroup.SAFE);
        this.definitions = definitions;
        this.expression = expression;
    }

    @Override
    public List<TlaExp> getChildren() {
        List<TlaExp> answer = new ArrayList<>();
        answer.addAll(this.definitions);
        answer.add(this.expression);
        return answer;
    }

    @Override
    public String toTLAPlusSnippetCore() {
        return TlaStrings.LET
                + TlaStrings.SPACE
                + String.join(
                        TlaStrings.SPACE,
                        mapBy(this.definitions, d -> this.getTLASnippetOfChild(d)))
                + TlaStrings.SPACE
                + TlaStrings.IN
                + TlaStrings.SPACE
                + this.getTLASnippetOfChild(expression);
    }
}
