/*
    Because Solution is a class (and A4Solution is a class inside our Solution class, only one solution can exist at any time, thus
    getting a list of Solutions is not an option.  We can iterate
    soln.next() and writeXML right away but we cannot get a list of
    satisfying solutions by iterating soln.next() because it will just
    be a list of the same objects.
*/

package ca.uwaterloo.watform.alloyinterface;

import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.translator.A4Solution;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

public class Solution {
  // public Optional<CmdDecl> cmd;
  public final Optional<Instance> instance;
  private CompModule alloyCompModule;
  private A4Solution a4soln; // this gets overridden with every next!
  public final Boolean isSat;

  private Solution(Instance instance, Boolean isSat) {
    assert ((instance == null && !isSat) || (instance != null && isSat));
    this.instance = (instance == null) ? Optional.empty() : Optional.of(instance);
    this.isSat = isSat;
  }

  public static Solution UnsatSolution() {
    Solution soln = new Solution(null, false);
    soln.alloyCompModule = null;
    soln.a4soln = null;
    return soln;
  }

  public static Solution SatSolution(CompModule alloyCompModule, A4Solution a4soln) {

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    a4soln.writeXML(pw, alloyCompModule.getAllFunc(), Collections.emptyMap());
    pw.flush();
    String xml = sw.toString();
    Instance instance = new Instance(xml);
    Solution soln = new Solution(instance, true);
    soln.alloyCompModule = alloyCompModule;
    soln.a4soln = a4soln;
    return soln;
  }

  public Solution next() {

    // System.out.println(a4soln.debugExtractKInstance());
    A4Solution nextSoln = a4soln.next();
    if (nextSoln.satisfiable()) {
      return SatSolution(this.alloyCompModule, nextSoln);
    } else {
      return null;
    }
  }
}
