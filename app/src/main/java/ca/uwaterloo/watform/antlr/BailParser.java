package ca.uwaterloo.watform.antlr;

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.BailErrorStrategy;
import antlr.generated.AlloyParser;

public class BailParser extends AlloyParser {
	public BailParser(TokenStream tokens) {
		super(tokens);
		this._errHandler = new BailErrorStrategy();
	}
}
