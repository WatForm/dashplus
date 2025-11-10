package ca.uwaterloo.watform.antlr;

import antlr.generated.DashLexer;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class BailLexer extends DashLexer {
    public BailLexer(CharStream input) {
        super(input);

        removeErrorListeners();

        addErrorListener(
                new BaseErrorListener() {
                    @Override
                    public void syntaxError(
                            Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException e) {
                        throw new ParseCancellationException(
                                "line " + line + ":" + charPositionInLine + " " + msg);
                    }
                });
    }

    @Override
    public void recover(RecognitionException e) throws ParseCancellationException {
        throw new ParseCancellationException(e.getCause());
    }

    @Override
    public void recover(LexerNoViableAltException e) throws ParseCancellationException {
        throw new ParseCancellationException(e.getCause());
    }
}
