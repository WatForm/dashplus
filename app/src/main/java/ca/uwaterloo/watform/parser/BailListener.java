package ca.uwaterloo.watform.parser;

import ca.uwaterloo.watform.utils.*;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

// We want our lexer and parser to bail early and not try any error recovery.
// We achieve this by throwing ParseCancellationException when the first
// syntax error is being recorded.
public final class BailListener extends BaseErrorListener {
    @Override
    public void syntaxError(
            Recognizer<?, ?> recognizer,
            Object offendingSymbol,
            int line,
            int charPositionInLine,
            String msg,
            RecognitionException e) {
        throw new Reporter.ErrorUser(
                new Pos(line, charPositionInLine, line, charPositionInLine), msg);
    }
}
