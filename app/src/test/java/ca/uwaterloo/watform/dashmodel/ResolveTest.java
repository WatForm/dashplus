package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.utils.ParserUtil.*;
import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.dashast.DashFile;
import ca.uwaterloo.watform.test.*;
import ca.uwaterloo.watform.utils.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ResolveTest {

	@Test
    @Order(1)
    @DisplayName("parse and resolve a minimal Dash file")
    public void resolveMinimal() throws Exception {
        Path p = Paths.get("src/test/resources/dashmodel/minimal.dsh");
        // DashFile dashFile = (DashFile)parse(p);
		// new DashModel(dashFile);
    }
	
}
