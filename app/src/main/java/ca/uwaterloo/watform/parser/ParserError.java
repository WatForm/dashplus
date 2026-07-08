package ca.uwaterloo.watform.parser;

import ca.uwaterloo.watform.utils.*;
import java.util.List;

public final class ParserError extends UserOrImplError {

    private ParserError(String msg) {
        super(msg);
    }

    private ParserError(Pos pos, String msg) {
        super(pos, msg);
    }

    private ParserError(List<Pos> posList, String msg) {
        super(posList, msg);
    }

    public static ParserError notUtilFile(Pos pos, String fileName) {
        throw new ParserError(pos, "Not a util file: fileName");
    }

    public static ParserError utilFileNotFound(Pos pos, String msg) {
        return new ParserError(pos, "Util file not found in jar: " + msg);
    }
}
