package ca.uwaterloo.watform.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import antlr.generated.AlloyLexer;

public class BailLexer extends AlloyLexer {
	public BailLexer(CharStream input) {
		super(input);
	}

	@Override
	public void recover(RecognitionException e) {
		throw new ParseCancellationException(e);
	}
}

