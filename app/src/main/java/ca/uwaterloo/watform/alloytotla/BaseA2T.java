package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2THelpers.*;
import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaStringLiteral;
import ca.uwaterloo.watform.tlaast.tlanaryops.TlaSet;
import ca.uwaterloo.watform.tlaast.tlanaryops.TlaTuple;
import ca.uwaterloo.watform.utils.CustomLoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BaseA2T {

  public static final int DEFAULT_SCOPE = 3;

  public final AlloyModel alloyModel;
  public final boolean verbose;
  public final boolean debug;
  public final Logger l;
  public final AlloyToTlaExprVis translator;

  static record Optimization(boolean nonExactSymmetry) {}
  public final Optimization optimization;

  // this is a buffer to hold debug data from the ExpressionVisitor
  private final StringBuilder transcriptBuffer;

  public BaseA2T(AlloyModel alloyModel, Optimization optimization, boolean verbose, boolean debug) {
    this.alloyModel = alloyModel;
    this.verbose = verbose;
    this.debug = debug;
    this.optimization = optimization;
    this.l = CustomLoggerFactory.make("AlloyToTla", debug);
    this.translator = new AlloyToTlaExprVis(alloyModel, l);
    this.transcriptBuffer = new StringBuilder("");
  }

  public TlaExp translateSnippet(AlloyExpr e) {
    return translator.extract(translator.visit(e));
  }

  // this clears the transcriptBuffer and returns the contents
  public String dump() {
    String answer = transcriptBuffer.toString();
    transcriptBuffer.setLength(0);
    return answer;
  }

  // this adds contents to the transcriptBuffer
  public void log(String s) {
    transcriptBuffer.append("\n" + s);
  }

  /*
  commonly used functions:
  */

  protected TlaStringLiteral sigAtomString(String signame, int n) {
    return TlaStringLiteral(signame + DOLLAR + n);
  }

  protected TlaTuple sigAtom(String signame, int n) {
    return TlaTuple(sigAtomString(signame, n));
  }

  protected TlaSet sigAtoms(String signame, int start, int end) {
    List<TlaTuple> atoms = new ArrayList<>();
    for (int i = start; i <= end; i++) {
      atoms.add(sigAtom(signame, i));
    }
    return TlaSet(atoms);
  }
}
