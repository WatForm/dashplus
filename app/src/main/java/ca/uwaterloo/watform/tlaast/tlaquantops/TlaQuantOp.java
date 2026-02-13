package ca.uwaterloo.watform.tlaast.tlaquantops;

import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaTuple;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;
import static ca.uwaterloo.watform.utils.GeneralUtil.strCommaList;

import ca.uwaterloo.watform.tlaast.*;
import java.util.ArrayList;
import java.util.List;

public abstract class TlaQuantOp extends TlaOperator {

    /*
    this produces expressions with quantification
    */

    public static class TlaQuantOpHead {
        /*

        is of the form:
        1) v1,v1,v... \in S (where type is FLAT)
        2) <<v1,v2,v3...>> \in S (where type is TUPLE)

        */
        public static enum Type {
            FLAT,
            TUPLE
        }

        public final Type type;
        public final List<TlaVar> variables;
        public final TlaExp set;

        public TlaQuantOpHead(Type type, List<TlaVar> variables, TlaExp set) {
            this.type = type;
            this.variables = variables;
            this.set = set;
        }

        public String toTLAPlusSnippetCore(TlaQuantOp parent) {
            return (this.type == Type.FLAT
                            ? strCommaList(mapBy(this.variables, v -> v.toTLAPlusSnippetCore()))
                            : TlaTuple(this.variables).toTLAPlusSnippetCore())
                    + TlaStrings.SPACE
                    + TlaStrings.SET_IN
                    + TlaStrings.SPACE
                    + parent.getTLASnippetOfChild(this.set);
        }

        public List<TlaExp> children() {
            List<TlaExp> answer = new ArrayList<>();
            variables.forEach(v -> answer.add(v));
            answer.add(set);
            return answer;
        }
    }

    public final List<TlaQuantOpHead> heads;
    public final TlaExp expression; // expression used

    public TlaQuantOp(
            List<TlaQuantOpHead> heads,
            TlaExp expression,
            TlaOperator.PrecedenceGroup precedenceGroup) {
        super(TlaOperator.Associativity.IRRELEVANT, precedenceGroup);
        this.heads = heads;
        this.expression = expression;
    }

    public List<TlaExp> getChildren() {
        List<TlaExp> answer = new ArrayList<>();
        answer.add(this.expression);
        this.heads.forEach(h -> answer.addAll(h.children()));
        return answer;
    }

    // this is a common style for exists and for-all
    static String predicateSnippetCore(TlaQuantOp o, String symbol) {
        return symbol
                + TlaStrings.SPACE
                + strCommaList(mapBy(o.heads, h -> h.toTLAPlusSnippetCore(o)))
                + TlaStrings.SPACE
                + TlaStrings.COLON
                + TlaStrings.SPACE
                + o.getTLASnippetOfChild(o.expression);
    }
}
