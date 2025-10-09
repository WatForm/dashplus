package ca.uwaterloo.watform.test;

import ca.uwaterloo.watform.antlr.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.antlr.v4.runtime.*;

public class Main {
	public static void main(String[] args) throws Exception {
		parseCatalystCorpus();
		parseTimeout();
	}

	public static void parseTimeout() throws Exception {
		Path p = Paths.get("src/test/resources/antlr/catalyst/timeout");
		new AntlrTestUtil().recurParseDir(p, 120 * 1000);
	}

	public static void parseCatalystCorpus() throws Exception {
		Path p = Paths.get("src/test/resources/antlr/catalyst/catalyst-corpus");
		new AntlrTestUtil().recurParseDir(p, 20 * 1000);
	}
}
