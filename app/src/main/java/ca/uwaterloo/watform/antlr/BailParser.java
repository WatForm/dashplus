package ca.uwaterloo.watform.antlr;

import antlr.generated.AlloyParser;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.TokenStream;

public class BailParser extends AlloyParser {
    public BailParser(TokenStream tokens) {
        super(tokens);
        this._errHandler = new BailErrorStrategy();
    }
}
