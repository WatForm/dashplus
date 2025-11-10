package ca.uwaterloo.watform.tlaplusast.tlaplusquantifier;

import ca.uwaterloo.watform.tlaplusast.TLAPlusExpression;
import ca.uwaterloo.watform.tlaplusast.TLAPlusVariable;

public class TLAPlusSetMap extends TLAPlusQuantifier {

    public TLAPlusSetMap(TLAPlusVariable v, TLAPlusExpression set, TLAPlusExpression exp) {
        super(v, set, exp);
    }

    /*
    @Override
    public List<String> toStringList() {
        List<String> result = new ArrayList<>();
        result.add(TLAPlusStrings.SET_START);
        result.addAll(this.children.get(0).toStringList());
        result.add(TLAPlusStrings.COLON);
        result.addAll(this.children.get(1).toStringList());
        result.add(TLAPlusStrings.SET_IN);
        result.addAll(this.children.get(2).toStringList());
        result.add(TLAPlusStrings.SET_END);
        return result;
    } */

    @Override
    public void toString(StringBuilder sb, int ident) {
        return;
        // TODO fix this
    }
}
