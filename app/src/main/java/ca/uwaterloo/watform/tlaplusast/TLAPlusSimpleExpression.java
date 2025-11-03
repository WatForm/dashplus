package ca.uwaterloo.watform.tlaplusast;

public abstract class TLAPlusSimpleExpression extends TLAPlusExpression {

    private String core;

    public TLAPlusSimpleExpression(String core) {
        this.core = core;
    }

    @Override
    public void toString(StringBuilder sb, int ident) {
        return;
        // TODO fix this
    }
}
