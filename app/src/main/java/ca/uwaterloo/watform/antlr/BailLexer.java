package ca.uwaterloo.watform.antlr;

import antlr.generated.DashLexer;
import org.antlr.v4.runtime.CharStream;

public class BailLexer extends DashLexer {
    public BailLexer(CharStream input) {
        super(input);
        removeErrorListeners();
        addErrorListener(new BailListener());
    }
}
