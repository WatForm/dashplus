package ca.uwaterloo.watform.antlr;

import antlr.generated.DashParser;
import org.antlr.v4.runtime.TokenStream;

public class BailParser extends DashParser {
    public BailParser(TokenStream tokens) {
        super(tokens);
        removeErrorListeners();
        addErrorListener(new BailListener());
    }
}
