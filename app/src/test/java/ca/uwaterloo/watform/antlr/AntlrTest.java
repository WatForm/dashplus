package ca.uwaterloo.watform.antlr;

import ca.uwaterloo.watform.test.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AntlrTest {

	@Test
	@Order(1)
	public void parseCatalystQuickTests() throws Exception {
		Path p = Paths.get("src/test/resources/antlr/catalyst/quick-tests");
		new AntlrTestUtil().recurParseDir(p, 5*1000);
	}

	@Test
	@Order(2)
	public void parse() throws Exception {
		Path p = Paths.get("src/test/resources/antlr/util");
		new AntlrTestUtil().recurParseDir(p, 5*1000);
	}

	// test catalyst corpus with main/java/ca/uwaterloo/watform/test/Main.java
}
